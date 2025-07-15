package com.deliverytech.delivery.dto.request;

import java.math.BigDecimal;

import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.validation.ValidCEP;
import com.deliverytech.delivery.validation.ValidCategoria;
import com.deliverytech.delivery.validation.ValidTelefone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
    description = "Dados necessários para criar ou atualizar um restaurante",
    title = "Restaurante Request DTO")
public class RestauranteRequestDTO {

  @Schema(description = "Nome do restaurante", example = "Pizzaria Express", required = true)
  @NotBlank(message = ExceptionMessage.NomeObrigatorio)
  @Size(min = 2, max = 100, message = "O nome do restaurante deve ter entre 2 e 100 caracteres")
  private String nome;

  @ValidCategoria
  private String categoria;

  @NotBlank(message = ExceptionMessage.EnderecoObrigatorio)
  private String endereco;

  @ValidCEP
  private String cep;

  @NotBlank(message = ExceptionMessage.TelefoneObrigatorio)
  @ValidTelefone
  private String telefone;

  @NotNull(message = ExceptionMessage.TaxaEntregaObrigatoria)
  private BigDecimal taxaEntrega;

  private BigDecimal avaliacao;

  @NotNull(message = ExceptionMessage.StatusObrigatorio)
  private Boolean ativo = true;
}
