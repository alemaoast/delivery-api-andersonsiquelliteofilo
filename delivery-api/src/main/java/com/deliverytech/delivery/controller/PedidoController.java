package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.pedido.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.services.impl.PedidoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
public class PedidoController {

    @Autowired
    private PedidoServiceImpl pedidoService;

    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Pedido já existe")
    })
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoRequestDTO dto) {
        PedidoResponseDTO pedidoSalvo = pedidoService.criar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedidoSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(pedidoSalvo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Recupera os detalhes de um pedido específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Lista todos os pedidos de um cliente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calcular")
    @Operation(summary = "Calcular valor total do pedido", description = "Calcula o valor total de um pedido com base nos itens fornecidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor total calculado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<BigDecimal> calcularValorTotal(@RequestBody List<ItemPedidoRequestDTO> itens) {
        return ResponseEntity.ok(pedidoService.calcularValorTotalPedido(itens));
    }

    @PutMapping("/{id}/{status}")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
            @PathVariable StatusPedido status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }

    // TODO: GET /api/pedidos - Listar com filtros (status, data)
}
