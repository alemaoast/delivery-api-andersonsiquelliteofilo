package com.deliverytech.delivery.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.cliente.ClienteRequestDTO;
import com.deliverytech.delivery.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.services.ClienteService;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {

        // Validar email único
        if (clienteRepository.existsByEmail(dto.getEmail()))
            throw new BusinessException(ExceptionMessage.EmailJaCadastrado);

        Cliente cliente = modelMapper.map(dto, Cliente.class);
        cliente.setAtivo(true); // Definir como ativo por padrão
        cliente.setDataCadastro(LocalDateTime.now());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorEmail(String email) {

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        // Verificar se email não está sendo usado por outro cliente
        if (!cliente.getEmail().equals(dto.getEmail()) &&
                clienteRepository.existsByEmail(dto.getEmail()))
            throw new BusinessException(ExceptionMessage.EmailJaCadastrado);

        // Atualizar campos
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO ativarDesativar(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ClienteNaoEncontrado));

        cliente.setAtivo(!cliente.getAtivo());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarAtivos() {

        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorNome(String nome) {

        return clienteRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .toList();
    }
}