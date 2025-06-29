package com.deliverytech.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por cliente com filtro por status/data
    @Query("SELECT p FROM Pedido p WHERE " +
       "(p.cliente.id = :clienteId) AND " +
       "(upper(p.status) = upper(:status)) AND " +
       "(p.dataPedido >= :dataInicio) AND " +
       "(p.dataPedido <= :dataFim)")
    List<Pedido> findByClienteIdAndStatusAndDataPedidoBetween(Long clienteId, String status, LocalDateTime dataInicio, LocalDateTime dataFim);

    // Buscar pedidos por cliente com filtro por data
    @Query("SELECT p FROM Pedido p WHERE " +
       "(p.cliente.id = :clienteId) AND " +
       "(p.dataPedido >= :dataInicio) AND " +
       "(p.dataPedido <= :dataFim)")
    List<Pedido> findByClienteIdAndDataPedidoBetween(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim);

    // Relatório: total gasto por cliente
    @Query("SELECT p.cliente.id, SUM(p.valorTotal) FROM Pedido p GROUP BY p.cliente.id")
    List<Object[]> totalGastoPorCliente();
}
