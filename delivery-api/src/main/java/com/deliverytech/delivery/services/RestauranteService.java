package com.deliverytech.delivery.services;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import com.deliverytech.delivery.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import com.deliverytech.delivery.projection.RelatorioVendas;

public interface RestauranteService {

  RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto);

  RestauranteResponseDTO buscarPorId(Long id);

  RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto);

  RestauranteResponseDTO ativarDesativar(Long id);

  RestauranteResponseDTO buscarPorNome(String nome);

  List<RestauranteResponseDTO> buscarPorCategoria(String categoria);

  List<RestauranteResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo);

  Page<RestauranteResponseDTO> listarAtivos(int page, int size);

  List<RestauranteResponseDTO> listarTop5PorNome();

  List<RelatorioVendas> relatorioVendasPorRestaurante();

  List<RestauranteResponseDTO> buscarPorTaxaEntrega(BigDecimal taxaEntrega);

  RestauranteResponseDTO inativar(Long id);
}
