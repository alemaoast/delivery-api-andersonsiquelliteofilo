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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

  @NotBlank(message = ExceptionMessage.NumeroPedidoObrigatorio)
  private String numeroPedido;

  @Past()
  private LocalDateTime dataPedido;

  private String observacoes;

  @NotNull
  @Min(0)
  private Long clienteId;

  @NotNull
  @Min(0)
  private Long restauranteId;

  @Valid
  @NotEmpty
  private List<ItemPedidoRequestDTO> itens;
}