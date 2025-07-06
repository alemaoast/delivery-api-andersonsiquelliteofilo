package com.deliverytech.delivery.services;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.dto.produto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.produto.ProdutoResponseDTO;

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
}
