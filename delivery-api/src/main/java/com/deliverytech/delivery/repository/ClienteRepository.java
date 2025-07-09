package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.projection.RelatorioVendasClientes;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar clientes por e-mail
    Optional<Cliente> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar clientes por status ativo
    List<Cliente> findByAtivoTrue();

    // Buscar clientes por nome (contendo)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // Relatório dos 5 clientes que mais compram
    @Query("SELECT " +
            "c.id as idCliente, " +
            "c.nome as nomeCliente, " +
            "SUM(p.valorTotal) as totalCompras, " +
            "COUNT(p.id) as quantidadePedidos " +
            "FROM Cliente c " +
            "INNER JOIN Pedido p ON c.id = p.cliente.id " +
            "WHERE c.ativo = true " +
            "GROUP BY c.id, c.nome " +
            "ORDER BY quantidadePedidos DESC " +
            "LIMIT 5")
    List<RelatorioVendasClientes> listarTop5ClientesQueMaisCompram();
}
