package com.deliverytech.delivery.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.pedido.PedidoResponseDTO;
import com.deliverytech.delivery.projection.RelatorioVendas;
import com.deliverytech.delivery.projection.RelatorioVendasClientes;
import com.deliverytech.delivery.projection.RelatorioVendasProdutos;
import com.deliverytech.delivery.services.ClienteService;
import com.deliverytech.delivery.services.PedidoService;
import com.deliverytech.delivery.services.ProdutoService;
import com.deliverytech.delivery.services.RestauranteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
@Tag(name = "Relatórios", description = "Relatórios gerais da plataforma")
public class RelatorioController {

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ClienteService clienteService;

     @Autowired
    private PedidoService pedidoService;

    @GetMapping("/vendas-por-restaurante")
    @Operation(summary = "Vendas por restaurante", description = "Relatório de vendas por restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório de vendas encontrado"),
            @ApiResponse(responseCode = "404", description = "Nenhum dado de venda encontrado")
    })
    public ResponseEntity<List<RelatorioVendas>> relatorioVendasPorRestaurante() {

        List<RelatorioVendas> relatorio = restauranteService.relatorioVendasPorRestaurante();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/produtos-mais-vendidos")
    @Operation(summary = "Top 5 produtos mais vendidos", description = "Listar os 5 primeiros produtos mais vendidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos mais vendidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto foi vendido")
    })
    public ResponseEntity<List<RelatorioVendasProdutos>> listarTop5ProdutosMaisVendidos() {

        List<RelatorioVendasProdutos> relatorio = produtoService.listarTop5MaisVendidos();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/clientes-ativos")
    @Operation(summary = "Top 5 clientes que compram mais", description = "Listar os 5 primeiros clientes que mais fazem pedidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clientes mais ativos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente ativo foi encontrado")
    })
    public ResponseEntity<List<RelatorioVendasClientes>> listarClientesMaisAtivos() {

        List<RelatorioVendasClientes> clientes = clienteService.listarTop5RealizamMaisPedidos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/pedidos-por-periodo")
    @Operation(summary = "Pedidos por período", description = "Listar pedidos por período")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "400", description = "Datas inicio ou fim inválidas"),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<PedidoResponseDTO> pedidos = pedidoService.listarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(pedidos);
    }
}
