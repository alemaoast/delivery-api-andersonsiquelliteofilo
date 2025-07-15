package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemPedidoResponseDTO {
  private Long id;
  private Integer quantidade;
  private BigDecimal precoUnitario;
  private BigDecimal subtotal;
  private Long pedidoId;
  private Long produtoId;
}
