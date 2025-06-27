package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Buscar pedidos por cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Buscar pedidos por status
    List<Pedido> findByStatus(String status);

    // Relatório: total gasto por cliente
    @Query("SELECT p.cliente.id, SUM(p.valorTotal) FROM Pedido p GROUP BY p.cliente.id")
    List<Object[]> totalGastoPorCliente();
}
