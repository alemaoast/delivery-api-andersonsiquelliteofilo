package com.deliverytech.delivery.dto.produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery.dto.pedido.ItemPedidoResponseDTO;
import com.deliverytech.delivery.dto.restaurante.RestauranteResponseDTO;

import lombok.Data;

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
