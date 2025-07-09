package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.pedido.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.PedidoService;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO dto) {
        // 1. Validar cliente existe e está ativo
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        if (!cliente.getAtivo())
            throw new BusinessException(ExceptionMessage.ClienteInativo);

        // 2. Validar restaurante existe e está ativo
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        if (!restaurante.getAtivo())
            throw new BusinessException(ExceptionMessage.RestauranteNaoDisponivel);

        // TODO: validar se restaurante entrega no endereço

        // 3. Validar todos os produtos existem e estão disponíveis
        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

            if (!produto.getDisponivel())
                throw new BusinessException(ExceptionMessage.ProdutoNaoDisponivel);

            if (!produto.getRestaurante().getId().equals(dto.getRestauranteId()))
                throw new BusinessException(ExceptionMessage.ProdutoNaoPertenceAoRestaurante);

            // Criar item do pedido
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            itensPedido.add(item);
            subtotal = subtotal.add(item.getSubtotal());
        }

        // 4. Calcular total do pedido
        BigDecimal taxaEntrega = restaurante.getTaxaEntrega();
        BigDecimal valorTotal = subtotal.add(taxaEntrega);
        // TODO: aplicar desconto se tiver

        // 5. Salvar pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setObservacoes(dto.getObservacoes());
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE.name());
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setTaxaEntrega(taxaEntrega);
        pedido.setValorTotal(valorTotal);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // 6. Salvar itens do pedido
        for (ItemPedido item : itensPedido) {
            item.setPedido(pedidoSalvo);
        }
        pedido.setItens(itensPedido);

        // 7. Atualizar estoque (se aplicável) - Simulação
        // Em um cenário real, aqui seria decrementado o estoque

        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PedidoNaoEncontrado));

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorCliente(Long clienteId) {

        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

        if (pedidos.isEmpty())
            throw new EntityNotFoundException(ExceptionMessage.PedidosNaoEncontradosParaCliente);

        return pedidos.stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .toList();
    }

    @Override
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PedidoNaoEncontrado));

        if (!isTransicaoValida(StatusPedido.valueOf(pedido.getStatus()), status))
            throw new BusinessException(ExceptionMessage.TransicaoStatusPedidoInvalida);

        pedido.setStatus(status.name());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    public BigDecimal calcularValorTotalPedido(List<ItemPedidoRequestDTO> itens) {

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemPedidoRequestDTO item : itens) {

            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

            valorTotal = valorTotal.add(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        return valorTotal;
    }

    @Override
    public PedidoResponseDTO cancelar(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PedidoNaoEncontrado));

        if (!pedido.getStatus().equals(StatusPedido.CANCELADO.name()))
            throw new BusinessException(ExceptionMessage.PedidoJaCancelado);

        if (!podeSerCancelado(StatusPedido.valueOf(pedido.getStatus())))
            throw new BusinessException(ExceptionMessage.PedidoNaoPodeSerCancelado + pedido.getStatus());

        pedido.setStatus(StatusPedido.CANCELADO.name());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {

        // A data de inicio não pode ser antes do delivery existir
        if (inicio.equals(LocalDate.of(2024, 12, 31)))
            throw new BusinessException("Data de inicio inválida");

        // A data de fim não pode ser futura
        if (fim.equals(LocalDateTime.now().plusDays(1)))
            throw new BusinessException("Data fim inválida");

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(inicio.atStartOfDay(),
                fim.atTime(LocalTime.of(23, 59)));
        if (pedidos.isEmpty())
            throw new EntityNotFoundException(ExceptionMessage.NenhumPedidoEncontrado);

        return pedidos.stream().map(p -> modelMapper.map(p, PedidoResponseDTO.class)).toList();
    }

    private boolean isTransicaoValida(StatusPedido statusAtual, StatusPedido novoStatus) {
        switch (statusAtual) {
            case PENDENTE:
                return novoStatus == StatusPedido.CONFIRMADO || novoStatus == StatusPedido.CANCELADO;
            case CONFIRMADO:
                return novoStatus == StatusPedido.PREPARANDO || novoStatus == StatusPedido.CANCELADO;
            case PREPARANDO:
                return novoStatus == StatusPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA:
                return novoStatus == StatusPedido.ENTREGUE;
            default:
                return false;
        }
    }

    private boolean podeSerCancelado(StatusPedido status) {
        return status == StatusPedido.PENDENTE || status == StatusPedido.CONFIRMADO;
    }
}