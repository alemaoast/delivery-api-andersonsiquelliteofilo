package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(
    description = "Resposta quando criar ou atualizar um produto",
    title = "Produto Response DTO")
@Data
public class ProdutoResponseDTO {
  private Long id;
  private String nome;
  private String descricao;
  private BigDecimal preco;
  private String categoria;
  private Boolean disponivel;
  private RestauranteResponseDTO restaurante;
  private List<ItemPedidoResponseDTO> itens = new ArrayList<>();
}
