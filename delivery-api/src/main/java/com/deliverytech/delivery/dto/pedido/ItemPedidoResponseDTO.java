package com.deliverytech.delivery.dto.pedido;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoResponseDTO {
  private Long id;
  private Integer quantidade;
  private BigDecimal precoUnitario;
  private BigDecimal subtotal;
  private Long pedidoId;
  private Long produtoId;
}
