package com.deliverytech.delivery.dto.restaurante;

import java.math.BigDecimal;

import com.deliverytech.delivery.exception.ExceptionMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
    description = "Dados necessários para criar ou atualizar um restaurante",
    title = "Restaurante Request DTO")
public class RestauranteRequestDTO {

  @Schema(description = "Nome do restaurante", example = "Pizzaria Express", required = true)
  @NotBlank(message = ExceptionMessage.NomeObrigatorio)
   @Size(min = 3, max = 100, message = "O nome do restaurante deve ter entre 3 e 100 caracteres")
  private String nome;

  @NotBlank(message = ExceptionMessage.CategoriaObrigatoria)
  private String categoria;

  @NotBlank(message = ExceptionMessage.EnderecoObrigatorio)
  private String endereco;

  @NotBlank(message = ExceptionMessage.TelefoneObrigatorio)
  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = ExceptionMessage.TelefoneInvalido)
  private String telefone;

  @NotNull(message = ExceptionMessage.TaxaEntregaObrigatoria)
  private BigDecimal taxaEntrega;

  private BigDecimal avaliacao;

  @NotNull(message = ExceptionMessage.StatusObrigatorio)
  private Boolean ativo = true;
}
