package com.deliverytech.delivery.service.interfaces;

import java.util.List;

import com.deliverytech.delivery.dto.cliente.ClienteRequestDTO;
import com.deliverytech.delivery.dto.cliente.ClienteResponseDTO;

public interface ClienteService {

  ClienteResponseDTO cadastrar(ClienteRequestDTO dto);

  ClienteResponseDTO buscarPorId(Long id);

  List<ClienteResponseDTO> buscarPorNome(String nome);

  ClienteResponseDTO buscarPorEmail(String email);

  ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto);

  ClienteResponseDTO ativarDesativar(Long id);

  List<ClienteResponseDTO> listarTodosAtivos();

  void inativar(Long id);
}