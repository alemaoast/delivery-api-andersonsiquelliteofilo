package com.deliverytech.delivery.dto.restaurante;

import java.math.BigDecimal;

import com.deliverytech.delivery.exception.ExceptionMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteRequestDTO {

  @NotBlank(message = ExceptionMessage.NomeObrigatorio)
  private String nome;

  @NotBlank(message = ExceptionMessage.CategoriaObrigatoria)
  private String categoria;

  @NotBlank(message = ExceptionMessage.EnderecoObrigatorio)
  private String endereco;

  @NotBlank(message = ExceptionMessage.TelefoneObrigatorio)
  @Pattern(regexp = "\\d{10,11}", message = ExceptionMessage.TelefoneInvalido)
  private String telefone;

  private BigDecimal taxaEntrega;

  private BigDecimal avaliacao;
}
