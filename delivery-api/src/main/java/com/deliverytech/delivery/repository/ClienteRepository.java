package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

    // Buscar clientes por e-mail
    Optional<Cliente> findByEmail(String email);

    // Buscar clientes por status ativo 
    List<Cliente> findByAtivo(Boolean ativo);
}
