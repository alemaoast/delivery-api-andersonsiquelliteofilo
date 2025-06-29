package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Long restauranteId);

    // Buscar produtos por categoria
    List<Produto> findByCategoriaContainingIgnoreCase(String categoria);

    // Buscar produtos disponiveis por restaurante
    List<Produto> findByDisponivelTrueAndRestauranteId(Long restauranteId);
}
