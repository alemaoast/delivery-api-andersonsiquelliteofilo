package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

    // Buscar clientes por e-mail
    Optional<Cliente> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmailAndAtivoTrue(String email);

    // Buscar clientes por status ativo 
    List<Cliente> findByAtivoTrue();

    // Buscar clientes por nome (contendo)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
