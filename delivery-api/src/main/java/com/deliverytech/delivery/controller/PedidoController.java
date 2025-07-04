package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.pedido.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoRequestDTO;
import com.deliverytech.delivery.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.service.implementations.PedidoServiceImpl;

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
public class PedidoController {

    @Autowired
    private PedidoServiceImpl pedidoService;

    /*
     * Cadastra um novo pedido
     */
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoRequestDTO dto) {
        PedidoResponseDTO pedidoSalvo = pedidoService.criar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedidoSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(pedidoSalvo);
    }

    /*
     * Busca um pedido pelo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    /*
     * Busca um pedido pelo ID do cliente
     */
    @GetMapping("/clientes/{clienteId}/pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.buscarPorCliente(clienteId));
    }

    /*
     * Atualiza o status de um pedido
     */
    @CrossOrigin(origins = "*", methods = { RequestMethod.PATCH })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }

    /*
     * Cancelar pedido (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * Calcular total sem salvar
     */
    @PostMapping("/calcular")
    public ResponseEntity<BigDecimal> calcularValorTotal(@RequestBody List<ItemPedidoRequestDTO> itens) {
        return ResponseEntity.ok(pedidoService.calcularTotalPedido(itens));
    }
}
