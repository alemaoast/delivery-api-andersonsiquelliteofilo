package com.deliverytech.delivery.dto.request;

import com.deliverytech.delivery.exception.ExceptionMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
    description = "Dados necessários para criar ou atualizar um item de pedido",
    title = "Item Pedido Request DTO")
public class ItemPedidoRequestDTO {

  @NotNull(message = ExceptionMessage.ProdutoObrigatorio)
  private Long produtoId;

  @Min(value = 1, message = ExceptionMessage.QuantidadeMaiorQueZero)
  @Max(value = 99, message = ExceptionMessage.QuantidadeMenorQueCem)
  @NotNull(message = ExceptionMessage.QuantidadeObrigatoria)
  private Integer quantidade;

}
