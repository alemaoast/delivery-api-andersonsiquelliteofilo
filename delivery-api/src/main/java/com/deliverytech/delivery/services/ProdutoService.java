package com.deliverytech.delivery.services;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery.projection.RelatorioVendasProdutos;

public interface ProdutoService {

  ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto);

  ProdutoResponseDTO buscarPorId(Long id);

  ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto);

  ProdutoResponseDTO ativarDesativar(Long id);

  ProdutoResponseDTO buscarPorNome(String nome);

  List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId);

  List<ProdutoResponseDTO> buscarPorCategoria(String categoria);

  List<ProdutoResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo);

  List<ProdutoResponseDTO> buscarTodosProdutos();

  List<ProdutoResponseDTO> buscarPorPrecoMenorOuIgual(BigDecimal valor);

  List<RelatorioVendasProdutos> listarTop5MaisVendidos();
}
