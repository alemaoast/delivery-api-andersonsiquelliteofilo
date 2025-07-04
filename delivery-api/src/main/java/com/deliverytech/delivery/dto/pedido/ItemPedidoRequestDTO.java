package com.deliverytech.delivery.dto.pedido;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequestDTO {

  @NotNull
  @Min(0)
  private Integer quantidade;

  @NotNull
  @Min(0)
  private BigDecimal precoUnitario;

  @NotNull
  @Min(0)
  private Long produtoId;
}
