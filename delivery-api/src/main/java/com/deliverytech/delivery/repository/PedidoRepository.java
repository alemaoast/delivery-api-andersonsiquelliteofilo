package com.deliverytech.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.enums.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

      // Buscar pedidos por cliente id
      List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

      // Buscar pedidos por cliente id
      List<Pedido> findByClienteId(Long clienteId);

      // Buscar pedidos por status
      List<Pedido> findByStatus(StatusPedido status);

      // Buscar top 10 pedidos mais recentes
      List<Pedido> findTop10ByOrderByDataPedidoDesc();

      // Buscar pedidos por data de pedido
      List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
}
