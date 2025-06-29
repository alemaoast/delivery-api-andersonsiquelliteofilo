package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /*
     * Cadastra um novo pedido
     */
    @PostMapping
    public ResponseEntity<Pedido> cadastrar(@RequestBody Pedido pedido) {
        Pedido pedidoSalvo = pedidoService.cadastrar(pedido);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedidoSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(pedidoSalvo);
    }

    /*
     * Busca um pedido pelo ID do cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorClienteId(@PathVariable Long clienteId,
            @RequestParam(defaultValue = "TODOS") String status,
            @RequestParam(defaultValue = "1900-01-01T00:00:00") LocalDateTime dataInicio,
            @RequestParam(defaultValue = "2100-01-01T00:00:00") LocalDateTime dataFim) {

        return ResponseEntity
                .ok(pedidoService.buscarPorClienteId(clienteId, status.toUpperCase(), dataInicio, dataFim));
    }

    /*
     * Atualiza o status de um pedido
     */
    @CrossOrigin(origins = "*", methods = { RequestMethod.PATCH })
    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }
}