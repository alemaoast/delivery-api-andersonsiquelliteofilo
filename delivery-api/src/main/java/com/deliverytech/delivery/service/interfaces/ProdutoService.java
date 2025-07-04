package com.deliverytech.delivery.service.interfaces;

import java.util.List;

import com.deliverytech.delivery.dto.produto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.produto.ProdutoResponseDTO;

public interface ProdutoService {

  public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto);

  public List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId);

  public ProdutoResponseDTO buscarPorId(Long id);

  public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto);

  public ProdutoResponseDTO alterarDisponibilidade(Long id, boolean disponivel);

  public List<ProdutoResponseDTO> buscarPorCategoria(String categoria);
}
