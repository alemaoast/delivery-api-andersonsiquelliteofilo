package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.exception.ResourceNotFoundException;
import com.deliverytech.delivery.repository.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    /*
     * Cadastrar um novo pedido
     */
    @Transactional
    public Pedido cadastrar(Pedido pedido) {

        // Validações de negócio
        validarDadosPedido(pedido);

        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado", "id",
                        pedido.getCliente().getId().toString()));

        if (cliente.getAtivo() == false) {
            throw new IllegalArgumentException("Cliente inativo, não é possível realizar pedidos");
        }

        pedido.setCliente(cliente);

        Restaurante restaurante = restauranteService.buscarPorId(pedido.getRestaurante().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado", "id",
                        pedido.getRestaurante().getId().toString()));

        pedido.setRestaurante(restaurante);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {

            Produto produto = produtoService.buscarPorId(item.getProduto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado", "id",
                            item.getProduto().getId().toString()));

            if (produto.getRestaurante().getId() != restaurante.getId()) {
                throw new IllegalArgumentException("Produto não pertence ao restaurante do pedido");
            }

            item.setProduto(produto);

            BigDecimal subtotal = item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade()));
            item.setSubtotal(subtotal);
            item.setPedido(pedido);

            valorTotal = valorTotal.add(subtotal);
            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(valorTotal);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);

        return pedidoRepository.save(pedido);
    }

    /*
     * Buscar pedido por ID
     */
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    /*
     * Buscar pedidos por cliente ID
     */
    public List<Pedido> buscarPorClienteId(Long clienteId, String status, LocalDateTime dataInicio,
            LocalDateTime dataFim) {

        if (status.toUpperCase().equals("TODOS")) {
            return pedidoRepository.findByClienteIdAndDataPedidoBetween(clienteId, dataInicio, dataFim);
        }

        return pedidoRepository.findByClienteIdAndStatusAndDataPedidoBetween(clienteId, status.toUpperCase(),
                dataInicio, dataFim);
    }

    /*
     * Relatório: total gasto por cliente
     */
    public List<Object[]> totalGastoPorCliente() {
        return pedidoRepository.totalGastoPorCliente();
    }

    /*
     * Atualizar status do pedido
     */
    public Pedido atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado", "id", id.toString()));

        pedido.setStatus(StatusPedido.valueOf(novoStatus.toUpperCase()));

        return pedidoRepository.save(pedido);
    }

    /*
     * Validações de dados do pedido
     */
    private void validarDadosPedido(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente não informado ou inválido");
        }
        if (pedido.getRestaurante() == null || pedido.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("Restaurante não informado ou inválido");
        }
        if (pedido.getItens() == null || pedido.getItens().size() == 0) {
            throw new IllegalArgumentException("Nenhum item de pedido informado");
        }
    }
}