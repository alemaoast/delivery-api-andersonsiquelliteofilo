package com.deliverytech.delivery.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.cliente.ClienteRequestDTO;
import com.deliverytech.delivery.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.service.interfaces.ClienteService;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Cadastrar novo cliente
     */
    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO clienteCadastrar) {

        // Validar email único
        if (clienteRepository.existsByEmail(clienteCadastrar.getEmail()))
            throw new IllegalArgumentException(ExceptionMessage.EmailJaCadastrado);

        Cliente cliente = modelMapper.map(clienteCadastrar, Cliente.class);
        cliente.setAtivo(true); // Definir como ativo por padrão
        cliente.setDataCadastro(LocalDateTime.now());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    /**
     * Buscar cliente por ID
     */
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    /**
     * Buscar cliente por nome
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        List<Cliente> cliente = clienteRepository.findByNomeContainingIgnoreCase(nome);
        return cliente
                .stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Buscar cliente por email
     */
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorEmail(String email) {

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    /**
     * Listar todos os clientes ativos
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodosAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Atualizar dados do cliente
     */
    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO clienteAtualizado) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        // Verificar se email não está sendo usado por outro cliente
        if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) &&
                clienteRepository.existsByEmail(clienteAtualizado.getEmail()))
            throw new IllegalArgumentException(ExceptionMessage.EmailJaCadastrado + " " + clienteAtualizado.getEmail());

        // Atualizar campos
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setEndereco(clienteAtualizado.getEndereco());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    /**
     * Inativar cliente (soft delete)
     */
    @Override
    public void inativar(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        cliente.inativar();
        clienteRepository.save(cliente);
    }

    /**
     * Ativar desativar cliente
     */
    @Override
    public ClienteResponseDTO ativarDesativar(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        cliente.setAtivo(!cliente.getAtivo());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }
}