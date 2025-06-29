package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.service.ProdutoService;

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
    private ProdutoService produtoService;

    /*
     * Cadastrar novo produto
     */
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        Produto produtoSalvo = produtoService.cadastrar(produto);

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
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Buscar produtos por restaurante ID
     */
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.buscarPorRestauranteId(restauranteId));
    }

    /*
     * Buscar produtos disponiveis por restaurante ID
     */
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<List<Produto>> listarDisponiveisPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.listarDisponiveisPorRestaurante(restauranteId));
    }

    /*
     * Buscar produtos por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoria));
    }

    /*
     * Atualizar produto
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto novoProduto) {
        return ResponseEntity.ok(produtoService.atualizar(id, novoProduto));
    }

    /*
     * Deletar produto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
