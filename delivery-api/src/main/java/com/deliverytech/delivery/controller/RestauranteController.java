package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ApiResponseWrapper;
import com.deliverytech.delivery.dto.restaurante.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.restaurante.RestauranteResponseDTO;
import com.deliverytech.delivery.projection.RelatorioVendas;
import com.deliverytech.delivery.services.impl.RestauranteServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteServiceImpl restauranteService;

    @PostMapping
    @Operation(summary = "Cadastrar restaurante", description = "Cria um restaurante no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Restaurante já existe"),
    })
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@RequestBody @Valid RestauranteRequestDTO dto) {
        RestauranteResponseDTO restauranteSalvo = restauranteService.cadastrar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(restauranteSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(restauranteSalvo);
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes ativos com filtros opcionais e paginação")
    public ResponseEntity<ApiResponseWrapper<List<RestauranteResponseDTO>>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivos();
        ApiResponseWrapper<List<RestauranteResponseDTO>> response = ApiResponseWrapper.success(restaurantes,
                "Busca realizada com sucesso");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante", description = "Buscar restaurante por id")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante", description = "Atualizar restaurante cadastrado")
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid RestauranteRequestDTO dto) {
        return ResponseEntity.ok(restauranteService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/ativar-desativar")
    @Operation(summary = "Ativar ou desativar restaurante", description = "Ativar ou desativar restaurante")
    public ResponseEntity<RestauranteResponseDTO> atualizarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.ativarDesativar(id));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar restaurante", description = "Buscar restaurante por nome")
    public ResponseEntity<RestauranteResponseDTO> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }

    @GetMapping("/preco/{precoMinimo}/{precoMaximo}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorPreco(@PathVariable BigDecimal precoMinimo,
            @PathVariable BigDecimal precoMaximo) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorPreco(precoMinimo, precoMaximo);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar restaurante", description = "Buscar restaurante por categoria")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar restaurante", description = "Inativar restaurante (soft delete)")
    public ResponseEntity<RestauranteResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.inativar(id));
    }

    @GetMapping("/taxa-entrega")
    @Operation(summary = "Buscar restaurante", description = "Listar taxa de entrega menor ou igual")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorTaxaEntrega(@RequestParam BigDecimal taxa) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorTaxaEntrega(taxa);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/top-cinco")
    @Operation(summary = "Buscar restaurante", description = "Listar os 5 primeiros restaurantes por nome")
    public ResponseEntity<List<RestauranteResponseDTO>> listarTop5PorNome() {
        List<RestauranteResponseDTO> top5Restaurantes = restauranteService.listarTop5PorNome();
        return ResponseEntity.ok(top5Restaurantes);
    }

    @GetMapping("/relatorio-vendas")
    @Operation(summary = "Relatório de vendas", description = "Relatório de vendas por restaurante")
    public ResponseEntity<List<RelatorioVendas>> relatorioVendasPorRestaurante() {
        List<RelatorioVendas> relatorio = restauranteService.relatorioVendasPorRestaurante();
        return ResponseEntity.ok(relatorio);
    }

    // TODO: GET /api/restaurantes/{id}/taxa-entrega/{cep} - Calcular taxa

    // TODO: GET /api/restaurantes/{restauranteId}/produtos - Produtos do restaurante

    // TODO: GET /api/restaurantes/proximos/{cep} - Restaurantes próximos

    // TODO: GET /api/restaurantes/{restauranteId}/pedidos - Pedidos do restaurante
}
