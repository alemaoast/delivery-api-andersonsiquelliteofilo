package com.deliverytech.delivery.dto.produto;

import java.math.BigDecimal;

import com.deliverytech.delivery.exception.ExceptionMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProdutoRequestDTO {

  @NotBlank(message = ExceptionMessage.NomeObrigatorio)
  private String nome;

  @NotBlank(message = ExceptionMessage.DescricaoObrigatoria)
  private String descricao;

  @Min(0)
  @NotBlank(message = ExceptionMessage.PrecoDeveSerMaiorQueZero)
  private BigDecimal preco;

  @NotBlank(message = ExceptionMessage.CategoriaObrigatoria)
  private String categoria;

  @NotBlank(message = ExceptionMessage.DisponibilidadeProdutoObrigatoria)
  private Boolean disponivel;

  @NotNull(message = ExceptionMessage.RestauranteInvalido)
  private Long restauranteId;
}
