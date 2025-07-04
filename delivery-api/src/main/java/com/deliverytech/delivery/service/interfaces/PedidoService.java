package com.deliverytech.delivery.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.dto.pedido.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery.enums.StatusPedido;

public interface PedidoService {
  public PedidoResponseDTO criar(PedidoRequestDTO dto);

  public PedidoResponseDTO buscarPorId(Long id);

  public List<PedidoResponseDTO> buscarPorCliente(Long clienteId);

  public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status);

  public BigDecimal calcularTotalPedido(List<ItemPedidoRequestDTO> itens);

  public PedidoResponseDTO cancelar(Long id);
}
