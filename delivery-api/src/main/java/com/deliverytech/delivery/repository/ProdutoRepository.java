package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

     // Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Integer restauranteId);

    // Buscar produtos por categoria
    List<Produto> findByCategoria(String categoria);

    // Buscar produtos por disponibilidade
    List<Produto> findByDisponivel(Boolean disponivel);
}
