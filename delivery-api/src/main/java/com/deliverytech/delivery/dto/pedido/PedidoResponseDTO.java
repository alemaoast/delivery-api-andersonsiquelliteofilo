package com.deliverytech.delivery.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery.dto.restaurante.RestauranteResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
  private Long id;
  private String numeroPedido;
  private LocalDateTime dataPedido;
  private String status;
  private BigDecimal valorTotal;
  private String observacoes;
  private ClienteResponseDTO cliente;
  private RestauranteResponseDTO restaurante;
  private List<ItemPedidoResponseDTO> itens = new ArrayList<>();
}
