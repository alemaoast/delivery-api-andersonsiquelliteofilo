package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(
    description = "Resposta quando criar ou atualizar um restaurante",
    title = "Restaurante Response DTO")
@Data
public class RestauranteResponseDTO {
  private Long id;
  private String nome;
  private String categoria;
  private String endereco;
  private String telefone;
  private BigDecimal taxaEntrega;
  private BigDecimal avaliacao;
  private Boolean ativo;
}
