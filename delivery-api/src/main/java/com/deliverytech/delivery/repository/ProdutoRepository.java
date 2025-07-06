package com.deliverytech.delivery.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar produtos por restaurante id
    List<Produto> findByRestauranteId(Long restauranteId);

    // Buscar produtos disponiveis
    List<Produto> findByDisponivelTrue();

    // Buscar produtos por categoria
    List<Produto> findByCategoria(String categoria);

    // Buscar produtos por preço menor ou igual a um valor especificado
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

    // Buscar produtos por nome
    Produto findByNome(String nome);
}
