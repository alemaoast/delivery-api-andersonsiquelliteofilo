package com.deliverytech.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.dto.cliente.ClienteRequestDTO;
import com.deliverytech.delivery.dto.cliente.ClienteResponseDTO;
import com.deliverytech.delivery.services.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cadastrar cliente", description = "Cria um novo cliente no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Cliente já cadastrado")
    })
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid ClienteRequestDTO cliente) {
        ClienteResponseDTO clienteSalvo = clienteService.cadastrar(cliente);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(clienteSalvo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Recupera os detalhes de um cliente específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por email", description = "Recupera os detalhes de um cliente específico pelo email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(clienteService.buscarPorEmail(email));
    }

    @GetMapping
    @Operation(summary = "Listar clientes ativos", description = "Lista todos os clientes que estão ativos no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado")
    })
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(clienteService.listarAtivos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/Desativar cliente", description = "Ativa ou desativa o status de um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> ativarDesativarCliente(@PathVariable Long id) {
        ClienteResponseDTO clienteAtualizado = clienteService.ativarDesativar(id);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nome", description = "Recupera uma lista de clientes que correspondem ao nome fornecido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado com o nome fornecido")
    })
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@Param("nome") String nome) {
        return ResponseEntity.ok(clienteService.buscarPorNome(nome));
    }

    // TODO: GET /api/clientes/{clienteId}/pedidos - Histórico do cliente
}