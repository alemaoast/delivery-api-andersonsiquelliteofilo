package com.deliverytech.delivery.service.implementations;

import com.deliverytech.delivery.dto.pedido.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.service.interfaces.PedidoService;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    /*
     * Cadastrar um novo pedido
     */
    @Override
    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        if (!cliente.getAtivo())
            throw new IllegalArgumentException(ExceptionMessage.ClienteInativo);

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        if (!restaurante.getAtivo())
            throw new IllegalArgumentException(ExceptionMessage.RestauranteInativo);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        List<ItemPedido> itensPedido = new ArrayList<>();

        for (ItemPedidoRequestDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

            if (!produto.getRestaurante().getId().equals(restaurante.getId()))
                throw new IllegalArgumentException(ExceptionMessage.ProdutoNaoPertenceAoRestaurante);

            if (!produto.getDisponivel())
                throw new IllegalArgumentException(ExceptionMessage.ProdutoNaoDisponivel);

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setPedido(pedido);

            // Adicionar o item na lista
            itensPedido.add(item);
        }

        // Calcular total dos itens
        BigDecimal valorTotal = itensPedido.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setItens(itensPedido);
        pedido.setValorTotal(valorTotal);

        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    /*
     * Buscar pedido por ID
     */
    @Override
    public PedidoResponseDTO buscarPorId(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PedidoNaoEncontrado));

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    /*
     * Buscar pedidos por cliente ID
     */
    @Override
    public List<PedidoResponseDTO> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    /*
     * Atualizar status do pedido
     */
    @Override
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PedidoNaoEncontrado));

        pedido.setStatus(status);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    public BigDecimal calcularTotalPedido(List<ItemPedidoRequestDTO> itens) {

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemPedidoRequestDTO item : itens) {
            valorTotal.add(item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())));
        }
        return valorTotal;
    }

    @Override
    public PedidoResponseDTO cancelar(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PedidoNaoEncontrado));

        if (!pedido.getStatus().equals(StatusPedido.PENDENTE))
            throw new IllegalArgumentException(ExceptionMessage.PedidoNaoPodeSerCancelado);

        pedido.setStatus(StatusPedido.CANCELADO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }
}