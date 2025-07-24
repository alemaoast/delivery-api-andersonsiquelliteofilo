package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery.services.impl.ProdutoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
public class ProdutoController {

    @Autowired
    private ProdutoServiceImpl produtoService;

    @PostMapping
    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Produto já existe")
    })
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@RequestBody @Valid ProdutoRequestDTO dto) {
        ProdutoResponseDTO produtoSalvo = produtoService.cadastrar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(produtoSalvo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Recupera os detalhes de um produto específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os detalhes de um produto existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Toggle disponibilidade", description = "Ativa ou desativa um produto pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto ativado/desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> ativarDesativar(@PathVariable Long id) {
        ProdutoResponseDTO produtoAtualizado = produtoService.ativarDesativar(id);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar produto por nome", description = "Recupera os detalhes de um produto específico pelo nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorNome(@PathVariable String nome) {
        ProdutoResponseDTO produto = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Buscar produtos por restaurante", description = "Lista todos os produtos de um restaurante específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.buscarPorRestaurante(restauranteId));
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar produtos por categoria", description = "Lista todos os produtos de uma categoria específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoria));
    }

    @GetMapping("/preco")
    @Operation(summary = "Buscar produtos por faixa de preço", description = "Lista todos os produtos dentro de uma faixa de preço específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado na faixa de preço")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorPreco(@RequestParam BigDecimal precoMinimo,
            @RequestParam BigDecimal precoMaximo) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorPreco(precoMinimo, precoMaximo);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Lista todos os produtos disponíveis no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado")
    })
    @Cacheable("produtos")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodosProdutos() {
        List<ProdutoResponseDTO> produtos = produtoService.buscarTodosProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/preco/{valor}")
    @Operation(summary = "Buscar produtos por preço menor ou igual", description = "Lista todos os produtos com preço menor ou igual ao valor especificado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado com preço menor ou igual ao valor especificado")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorPrecoMenorOuIgual(@PathVariable BigDecimal valor) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorPrecoMenorOuIgual(valor);
        return ResponseEntity.ok(produtos);
    }

    // TODO: DELETE /api/produtos/{id} - Remover produto
}
