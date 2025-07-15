package com.deliverytech.delivery.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.deliverytech.delivery.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery.enums.StatusPedido;

public interface PedidoService {

  PedidoResponseDTO criar(PedidoRequestDTO dto);

  PedidoResponseDTO buscarPorId(Long id);

  List<PedidoResponseDTO> listarPorCliente(Long clienteId);

  PedidoResponseDTO atualizarStatus(Long id, StatusPedido status);

  BigDecimal calcularValorTotalPedido(List<ItemPedidoRequestDTO> itens);

  PedidoResponseDTO cancelar(Long id);

  List<PedidoResponseDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim);
}
