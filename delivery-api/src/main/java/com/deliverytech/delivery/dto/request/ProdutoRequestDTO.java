package com.deliverytech.delivery.dto.request;

import java.math.BigDecimal;

import com.deliverytech.delivery.exception.ExceptionMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
    description = "Dados necessários para criar ou atualizar um produto",
    title = "Produto Request DTO")
public class ProdutoRequestDTO {

  @NotBlank(message = ExceptionMessage.NomeObrigatorio)
  @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
  private String nome;

  @NotBlank(message = ExceptionMessage.DescricaoObrigatoria)
  @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
  private String descricao;

  @NotBlank(message = ExceptionMessage.PrecoDeveSerMaiorQueZero)
  @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
  @DecimalMin(value = "500.00", message = "Preço não pode exceder R$ 500,00")
  private BigDecimal preco;

  @NotBlank(message = ExceptionMessage.CategoriaObrigatoria)
  private String categoria;

  @NotBlank(message = ExceptionMessage.DisponibilidadeProdutoObrigatoria)
  private Boolean disponivel;

  @NotNull(message = ExceptionMessage.RestauranteInvalido)
  private Long restauranteId;
}
