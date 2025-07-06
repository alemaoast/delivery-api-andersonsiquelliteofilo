package com.deliverytech.delivery.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.projection.RelatorioVendas;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar restaurante por nome
    Optional<Restaurante> findByNome(String nome);

    // Buscar restaurante por nome e ativo
    Restaurante findByNomeAndAtivoTrue(String nome);

    // Buscar restaurante ativos
    List<Restaurante> findByAtivoTrue();

    // Buscar restaurante por categoria
    List<Restaurante> findByCategoria(String categoria);

    // Buscar restaurante por taxa de entrega menor ou igual a um valor específico
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    // Buscar restaurante por taxa de entrega entre
    List<Restaurante> findByTaxaEntregaBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    // Buscar os 5 primeiros restaurantes ordenados por nome (ordem alfabética)
    List<Restaurante> findTop5ByOrderByNomeAsc();

    // Relatório de vendas por restaurante
    @Query("SELECT r.nome as nomeRestaurante, " +
            "SUM(p.valorTotal) as totalVendas, " +
            "COUNT(p.id) as quantidadePedidos " +
            "FROM Restaurante r " +
            "LEFT JOIN Pedido p ON r.id = p.restaurante.id " +
            "GROUP BY r.id, r.nome")
    List<RelatorioVendas> relatorioVendasPorRestaurante();
}