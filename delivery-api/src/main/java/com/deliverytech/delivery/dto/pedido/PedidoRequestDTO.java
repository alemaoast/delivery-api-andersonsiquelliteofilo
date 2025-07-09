package com.deliverytech.delivery.dto.pedido;

import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery.exception.ExceptionMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
public class PedidoRequestDTO {

  @NotBlank(message = ExceptionMessage.NumeroPedidoObrigatorio)
  private String numeroPedido;

  @Past()
  @NotNull(message = ExceptionMessage.DataPedidoObrigatoria)
  private LocalDateTime dataPedido;

  private String observacoes;

  @NotNull(message = "O endereço de entrega é obrigatório.")
  private String enderecoEntrega;

  @Min(0)
  @NotNull(message = ExceptionMessage.ClienteObrigatorio)
  private Long clienteId;

  @Min(0)
  @NotNull(message = ExceptionMessage.RestauranteObrigatorio)
  private Long restauranteId;

  @Valid
  @NotEmpty(message = ExceptionMessage.ItensPedidoObrigatorios)
  private List<ItemPedidoRequestDTO> itens;
}