package com.deliverytech.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.enums.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

   // Buscar pedidos por cliente com filtro por status/data
   @Query("SELECT p FROM Pedido p WHERE " +
         "(p.cliente.id = :clienteId) AND " +
         "(upper(p.status) = upper(:status)) AND " +
         "(p.dataPedido >= :dataInicio) AND " +
         "(p.dataPedido <= :dataFim)")
   List<Pedido> findByClienteIdAndStatusAndDataPedidoBetween(Long clienteId, String status, LocalDateTime dataInicio,
         LocalDateTime dataFim);

   // Buscar pedidos por cliente id
   List<Pedido> findByClienteId(Long clienteId);

   // Buscar pedidos por cliente com filtro por data
   @Query("SELECT p FROM Pedido p WHERE " +
         "(p.cliente.id = :clienteId) AND " +
         "(p.dataPedido >= :dataInicio) AND " +
         "(p.dataPedido <= :dataFim)")
   List<Pedido> findByClienteIdAndDataPedidoBetween(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim);

    // Relatório: total gasto por cliente
    @Query("SELECT p.cliente.id, SUM(p.valorTotal) FROM Pedido p GROUP BY p.cliente.id")
    List<Object[]> totalGastoPorCliente();
   
   // Buscar pedidos por status
   List<Pedido> findByStatus(StatusPedido status);

   // Buscar top 10 pedidos mais recentes
   List<Pedido> findTop10ByOrderByDataPedidoDesc();

   // Buscar pedidos por data de pedido
   List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

   // Relatório: Total de vendas por restaurante
   @Query("SELECT p.restaurante.id, SUM(p.valorTotal) FROM Pedido p WHERE p.restaurante.id = :restauranteId GROUP by p.restaurante.id")
   List<Object[]> totalVendasPorRestaurante(Long restauranteId);

   // Relatório: Pedidos com valor total acima de um valor específico
   @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valor")
   List<Pedido> findPedidosComValorTotalAcima(Double valor);

   // Relatório: Pedidos por status e data
   @Query("SELECT p FROM Pedido p WHERE " +
         "(upper(p.status) = upper(:status)) AND " +
         "(p.dataPedido >= :dataInicio) AND " +
         "(p.dataPedido <= :dataFim)")
   List<Pedido> findByStatusAndDataPedidoBetween(String status, LocalDateTime dataInicio, LocalDateTime dataFim);

   // Ranking de clientes por nº de pedidos 
   @Query("SELECT p.cliente.id, COUNT(p.id) FROM Pedido p GROUP BY p.cliente.id ORDER BY COUNT(p.id) DESC")
   List<Object[]> rankingClientesPorNumeroDePedidos();

   // Faturamento por categoria
   @Query("SELECT p.restaurante.categoria, SUM(p.valorTotal) FROM Pedido p GROUP BY p.restaurante.categoria")
   List<Object[]> faturamentoPorCategoria();
}
