package com.deliverytech.delivery.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery.dto.restaurante.RestauranteResponseDTO;

import lombok.Data;

@Data
public class PedidoResponseDTO {
  private Long id;
  private String numeroPedido;
  private LocalDateTime dataPedido;
  private String status;
  private BigDecimal valorTotal;
  private String observacoes;
  private String enderecoEntrega;
  private BigDecimal taxaEntrega;
  private ClienteResponseDTO cliente;
  private RestauranteResponseDTO restaurante;
  private List<ItemPedidoResponseDTO> itens;
}
