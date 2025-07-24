package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.response.ErrorResponseDTO;
import com.deliverytech.delivery.dto.response.PagedResponse;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import com.deliverytech.delivery.projection.RelatorioVendas;
import com.deliverytech.delivery.services.impl.RestauranteServiceImpl;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.apache.commons.lang3.NotImplementedException;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteServiceImpl restauranteService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cadastrar restaurante", description = "Cria um restaurante no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Restaurante já existe", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes ativos com filtros opcionais e paginação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de restaurantes recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @Timed(value = "restaurante.buscar", histogram = true)
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> listarPaginado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {

        Page<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivos(pageable.getPage(), pageable.getSize());

        String baseUrl = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        PagedResponse<RestauranteResponseDTO> response = new PagedResponse<>(restaurantes, baseUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar restaurante", description = "Buscar restaurante por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualizar restaurante", description = "Atualizar restaurante cadastrado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid RestauranteRequestDTO dto) {
        return ResponseEntity.ok(restauranteService.atualizar(id, dto));
    }

    @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Ativar/Desativar restaurante", description = "Ativa ou desativa o status de um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RestauranteResponseDTO> atualizarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.ativarDesativar(id));
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar restaurante", description = "Buscar restaurante por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado com o nome fornecido", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RestauranteResponseDTO> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }

    @GetMapping(value = "/preco/{precoMinimo}/{precoMaximo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar restaurante", description = "Buscar restaurante por taxa de entrega")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado para taxa de entrega", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorPreco(@PathVariable BigDecimal precoMinimo,
            @PathVariable BigDecimal precoMaximo) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorPreco(precoMinimo, precoMaximo);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping(value = "/categoria/{categoria}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar restaurante", description = "Buscar restaurante por categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado para categoria fornecida", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }

    @PatchMapping(value = "/{id}/inativar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Inativar restaurante", description = "Inativar restaurante (soft delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes inativado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RestauranteResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.inativar(id));
    }

    @GetMapping(value = "/taxa-entrega", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar restaurante", description = "Listar taxa de entrega menor ou igual")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorTaxaEntrega(@RequestParam BigDecimal taxa) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorTaxaEntrega(taxa);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping(value = "/top-cinco", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar restaurante", description = "Listar os 5 primeiros restaurantes por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<List<RestauranteResponseDTO>> listarTop5PorNome() {
        List<RestauranteResponseDTO> top5Restaurantes = restauranteService.listarTop5PorNome();
        return ResponseEntity.ok(top5Restaurantes);
    }

    @GetMapping(value = "/relatorio-vendas", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Relatório de vendas", description = "Relatório de vendas por restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório de vendas gerado"),
            @ApiResponse(responseCode = "404", description = "Nenhuma venda realizada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<List<RelatorioVendas>> relatorioVendasPorRestaurante() {
        List<RelatorioVendas> relatorio = restauranteService.relatorioVendasPorRestaurante();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping(value = "/{id}/taxa-entrega/{cep}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Calcular taxa de entrega", description = "Calcular taxa de entrega de um restaurante por cep")
    public ResponseEntity<Void> calcularTaxaEntrega(
            @PathVariable Long id,
            @PathVariable String cep) {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/proximos/{cep}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar restaurantes próximos", description = "Buscar restaurantes próximos de um cep")
    public ResponseEntity<Void> buscarRestaurantesProximos(
            @PathVariable String cep) {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/{restauranteId}/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Produtos do restaurante", description = "Buscar produtos por restaurante")
    public ResponseEntity<Void> buscarProdutosPorRestaurante(
            @PathVariable Long restauranteId) {
        throw new NotImplementedException();
    }

    @GetMapping(value = "/{restauranteId}/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Pedidos do restaurante", description = "Buscar pedidos do restaurante")
    public ResponseEntity<Void> buscarPedidosDoRestaurante(
            @PathVariable Long restauranteId) {
        throw new NotImplementedException();
    }
}
