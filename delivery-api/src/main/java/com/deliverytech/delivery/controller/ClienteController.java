package com.deliverytech.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.dto.response.ErrorResponseDTO;
import com.deliverytech.delivery.services.ClienteService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
@Timed(value = "cliente.controller.requests",
                description = "Tempo de resposta dos endpoints de cliente")
public class ClienteController {

        @Autowired
        private ClienteService clienteService;

        private Counter clienteCriadoContador;

        public ClienteController(MeterRegistry meterRegistry) {
                super();
                clienteCriadoContador = Counter.builder("client.created.count")
                        .description("Número de clientes criados").register(meterRegistry);
        }

        @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Cadastrar cliente", description = "Cria um novo cliente no sistema")
        @ApiResponses({@ApiResponse(responseCode = "201",
                        description = "Cliente cadastrado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Cliente já cadastrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class)))})
        @Timed(value = "client.created.time", description = "Tempo para criar um cliente")
        public ResponseEntity<ClienteResponseDTO> cadastrar(
                        @RequestBody @Valid ClienteRequestDTO cliente) {
                ClienteResponseDTO clienteSalvo = clienteService.cadastrar(cliente);
                clienteCriadoContador.increment();
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                                .buildAndExpand(clienteSalvo.getId()).toUri();

                return ResponseEntity.created(location).body(clienteSalvo);
        }

        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar cliente por ID",
                        description = "Recupera os detalhes de um cliente específico pelo ID")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class)))})
        public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
                return ResponseEntity.ok(clienteService.buscarPorId(id));
        }

        @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar cliente por email",
                        description = "Recupera os detalhes de um cliente específico pelo email")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class)))})
        public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
                return ResponseEntity.ok(clienteService.buscarPorEmail(email));
        }

        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Listar clientes ativos",
                        description = "Lista todos os clientes que estão ativos no sistema")
        @ApiResponses({@ApiResponse(responseCode = "200",
                        description = "Lista de clientes recuperada com sucesso"),
                        @ApiResponse(responseCode = "404",
                                        description = "Nenhum cliente encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class)))})
        @Timed(value = "client.list.time", description = "Tempo para buscar todos os clientes ativos")
        public ResponseEntity<List<ClienteResponseDTO>> listar() {
                return ResponseEntity.ok(clienteService.listarAtivos());
        }

        @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Atualizar cliente",
                        description = "Atualiza os dados de um cliente existente")
        @ApiResponses({@ApiResponse(responseCode = "200",
                        description = "Cliente atualizado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class)))})
        public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id,
                        @RequestBody @Valid ClienteRequestDTO cliente) {
                return ResponseEntity.ok(clienteService.atualizar(id, cliente));
        }

        @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Ativar/Desativar cliente",
                        description = "Ativa ou desativa o status de um cliente")
        @ApiResponses({@ApiResponse(responseCode = "200",
                        description = "Cliente atualizado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class)))})
        public ResponseEntity<ClienteResponseDTO> ativarDesativarCliente(@PathVariable Long id) {
                ClienteResponseDTO clienteAtualizado = clienteService.ativarDesativar(id);
                return ResponseEntity.ok(clienteAtualizado);
        }

        @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Buscar clientes por nome",
                        description = "Recupera uma lista de clientes que correspondem ao nome fornecido")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Clientes encontrados"),
                        @ApiResponse(responseCode = "404",
                                        description = "Nenhum cliente encontrado com o nome fornecido",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponseDTO.class)))})
        public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@Param("nome") String nome) {
                return ResponseEntity.ok(clienteService.buscarPorNome(nome));
        }

        // TODO: GET /api/clientes/{clienteId}/pedidos - Histórico do cliente
}
