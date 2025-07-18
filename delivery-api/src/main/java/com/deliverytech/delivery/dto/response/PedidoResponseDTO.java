package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(
    description = "Resposta quando criar ou atualizar um pedido",
    title = "Pedido Response DTO")
@Data
public class PedidoResponseDTO {
  private Long id;
  private String numeroPedido;
  private LocalDateTime dataPedido;
  private String status;
  private BigDecimal valorTotal;
  private String observacoes;
  private String enderecoEntrega;
  private BigDecimal taxaEntrega;
  private ClienteResponseDTO cliente;
  private RestauranteResponseDTO restaurante;
  private List<ItemPedidoResponseDTO> itens;
}
