package com.deliverytech.delivery.service.interfaces;

import java.util.List;

import com.deliverytech.delivery.dto.restaurante.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.restaurante.RestauranteResponseDTO;

public interface RestauranteService {

  public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto);

  public RestauranteResponseDTO buscarPorId(Long id);

  public List<RestauranteResponseDTO> buscarPorCategoria(String categoria);

  public List<RestauranteResponseDTO> buscarDisponiveis();

  public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto);

  public Long calcularTaxaEntrega(Long restauranteId, String cep);

  public List<RestauranteResponseDTO> buscarPorNome(String nome);

  public void inativar(Long id);

  public void atualizarStatus(Long id, boolean ativo);
}
