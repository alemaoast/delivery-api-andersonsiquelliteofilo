package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.produto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.produto.ProdutoResponseDTO;
import com.deliverytech.delivery.service.implementations.ProdutoServiceImpl;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoServiceImpl produtoService;

    /*
     * Cadastrar novo produto
     */
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@RequestBody @Valid ProdutoRequestDTO dto) {
        ProdutoResponseDTO produtoSalvo = produtoService.cadastrar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(produtoSalvo);
    }

    /*
     * Buscar produto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    /*
     * Buscar produtos por restaurante ID
     */
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.buscarPorRestaurante(restauranteId));
    }

    /*
     * Buscar produtos por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoria));
    }

    /*
     * Atualizar produto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id,
            @RequestBody @Valid ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    /*
     * Deletar produto
     */
    @PatchMapping("/{id}/disponibilidade")
    @CrossOrigin(origins = "*", methods = { RequestMethod.PATCH })
    public ResponseEntity<ProdutoResponseDTO> alterarDisponibilidade(@PathVariable Long id,
            @RequestParam boolean disponibilidade) {
        return ResponseEntity.ok(produtoService.alterarDisponibilidade(id, disponibilidade));
    }
}
