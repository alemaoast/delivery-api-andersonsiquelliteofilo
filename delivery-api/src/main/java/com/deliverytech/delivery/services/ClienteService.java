package com.deliverytech.delivery.services;

import java.util.List;

import com.deliverytech.delivery.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.projection.RelatorioVendasClientes;

public interface ClienteService {

  ClienteResponseDTO cadastrar(ClienteRequestDTO dto);

  ClienteResponseDTO buscarPorId(Long id);

  ClienteResponseDTO buscarPorEmail(String email);

  ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto);

  ClienteResponseDTO ativarDesativar(Long id);

  List<ClienteResponseDTO> listarAtivos();

  List<ClienteResponseDTO> buscarPorNome(String nome);

  List<RelatorioVendasClientes> listarTop5RealizamMaisPedidos();
}