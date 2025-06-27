package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {

    // Buscar restaurante por nome, ordenação por avaliação
    @Query("SELECT r FROM Restaurante r WHERE r.nome = :nome ORDER BY r.avaliacao DESC")
    List<Restaurante> findByNome(String nome);

    // Buscar restaurante por ativos, ordenação por avaliação
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true ORDER BY r.avaliacao DESC")
    List<Restaurante> findByAtivo();

    // Buscar restaurante por categoria, ordenação por avaliação
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria ORDER BY r.avaliacao DESC")
    List<Restaurante> findByCategoria(String categoria);
}