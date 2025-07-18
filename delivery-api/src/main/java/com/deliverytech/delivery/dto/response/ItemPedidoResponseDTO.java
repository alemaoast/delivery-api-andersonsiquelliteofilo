package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(
    description = "Resposta quando criar um Item Pedido",
    title = "Item Pedido Response DTO")
@Data
public class ItemPedidoResponseDTO {
  private Long id;
  private Integer quantidade;
  private BigDecimal precoUnitario;
  private BigDecimal subtotal;
  private Long pedidoId;
  private Long produtoId;
}
