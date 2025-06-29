package com.deliverytech.delivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.exception.ResourceNotFoundException;
import com.deliverytech.delivery.repository.ClienteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Cadastrar novo cliente
     */
    public Cliente cadastrar(Cliente cliente) {
        // Validações de negócio
        validarDadosCliente(cliente);

        // Validar email único
        if (clienteRepository.existsByEmailAndAtivoTrue(cliente.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // Definir como ativo por padrão
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());

        return clienteRepository.save(cliente);
    }

    /**
     * Buscar cliente por ID
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Buscar cliente por nome
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Buscar cliente por email
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    /**
     * Listar todos os clientes ativos
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodosAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    /**
     * Atualizar dados do cliente
     */
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {

        // Validações de negócio
        validarDadosCliente(clienteAtualizado);

        Cliente cliente = buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado", "id", id.toString()));

        // Verificar se email não está sendo usado por outro cliente
        if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) &&
                clienteRepository.existsByEmailAndAtivoTrue(clienteAtualizado.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + clienteAtualizado.getEmail());
        }

        // Atualizar campos
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setEndereco(clienteAtualizado.getEndereco());

        return clienteRepository.save(cliente);
    }

    /**
     * Inativar cliente (soft delete)
     */
    public void inativar(Long id) {

        Cliente cliente = buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado", "id", id.toString()));

        cliente.inativar();
        clienteRepository.save(cliente);
    }

    /**
     * Validações de negócio
     */
    private void validarDadosCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (cliente.getNome() == null || cliente.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }

        if (cliente.getEndereco() == null || cliente.getEndereco().isBlank()) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }
    }
}