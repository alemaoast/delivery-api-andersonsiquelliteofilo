package com.deliverytech.delivery.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.validation.ValidCEP;

import jakarta.validation.Valid;
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

  @ValidCEP
  private String cepEntrega;

  @NotBlank(message = "Forma de pagamento é obrigatória.")
  private String formaPagamento;

  @NotNull(message = ExceptionMessage.ClienteObrigatorio)
  private Long clienteId;

  @NotNull(message = ExceptionMessage.RestauranteObrigatorio)
  private Long restauranteId;

  @Valid
  @NotEmpty(message = ExceptionMessage.ItensPedidoObrigatorios)
  private List<ItemPedidoRequestDTO> itens;
}