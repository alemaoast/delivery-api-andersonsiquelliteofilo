package com.deliverytech.delivery.service;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Cliente> listarPorStatusAtivo(Boolean ativo) {
        return repository.findByAtivo(ativo);
    }

    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public void excluir(Integer id) {
        repository.deleteById(id);
    }

    public Cliente atualizar(Integer id, Cliente clienteAtualizado) {
        return repository.findById(id)
                .map(cliente -> {
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setEmail(clienteAtualizado.getEmail());
                    return repository.save(cliente);
                }).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }
}