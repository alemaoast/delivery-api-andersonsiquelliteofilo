package com.deliverytech.delivery.dto.restaurante;

import java.math.BigDecimal;

import lombok.Data;

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
