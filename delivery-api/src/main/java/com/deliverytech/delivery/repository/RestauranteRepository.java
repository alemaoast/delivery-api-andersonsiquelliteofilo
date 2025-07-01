package com.deliverytech.delivery.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar restaurante por nome, ordenação por avaliação
    @Query("SELECT r FROM Restaurante r WHERE upper(r.nome) like concat('%', upper(:nome), '%') ORDER BY r.avaliacao DESC")
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Buscar restaurante por nome, ordenação por avaliação
    @Query("SELECT r FROM Restaurante r WHERE upper(r.categoria) like concat('%', upper(:categoria), '%') ORDER BY r.avaliacao DESC")
    List<Restaurante> findByCategoriaContainingIgnoreCase(String categoria);
    
    // Buscar restaurante por categoria, ordenação por avaliação
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria ORDER BY r.avaliacao DESC")
    List<Restaurante> findByCategoria(String categoria);

    // Buscar restaurante por ativos, ordenação por avaliação
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true ORDER BY r.avaliacao DESC")
    List<Restaurante> findByAtivoTrue();

    // Buscar restaurante por taxa de entrega menor ou igual a um valor específico
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    // Buscar os 5 primeiros restaurantes ordenados por nome
    List<Restaurante> findTop5ByOrderByNomeAsc();
}