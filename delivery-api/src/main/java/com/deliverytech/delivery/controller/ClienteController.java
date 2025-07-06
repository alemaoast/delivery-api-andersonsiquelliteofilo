package com.deliverytech.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.dto.cliente.ClienteRequestDTO;
import com.deliverytech.delivery.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery.services.ClienteService;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Cadastrar novo cliente
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid ClienteRequestDTO cliente) {
        ClienteResponseDTO clienteSalvo = clienteService.cadastrar(cliente);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(clienteSalvo);
    }

    /**
     * Buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    /**
     * Buscar cliente por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(clienteService.buscarPorEmail(email));
    }

    /**
     * Listar todos os clientes ativos
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(clienteService.listarAtivos());
    }

    /**
     * Atualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClienteResponseDTO> ativarDesativarCliente(@PathVariable Long id) {
        ClienteResponseDTO clienteAtualizado = clienteService.ativarDesativar(id);
        return ResponseEntity.ok(clienteAtualizado);
    }

    /**
     * Buscar clientes por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@Param("nome") String nome) {
        return ResponseEntity.ok(clienteService.buscarPorNome(nome));
    }

}