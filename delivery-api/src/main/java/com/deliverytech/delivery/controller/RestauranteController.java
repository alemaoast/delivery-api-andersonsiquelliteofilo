package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.service.RestauranteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    /*
     * Cadastrar novo restaurante
     */
    @PostMapping
    public ResponseEntity<Restaurante> cadastrar(@RequestBody Restaurante restaurante) {
        Restaurante restauranteSalvo = restauranteService.cadastrar(restaurante);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(restauranteSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(restauranteSalvo);
    }

    /*
     * Listar todos os restaurantes ativos
     */
    @GetMapping
    public ResponseEntity<List<Restaurante>> listar() {
        return ResponseEntity.ok(restauranteService.listarTodosAtivos());
    }

    /*
     * Buscar restaurante por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Buscar restaurante por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Restaurante>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }

    /*
     * Buscar restaurante por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }

    /*
     * Atualizar restaurante
     */
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(@PathVariable Long id, @RequestBody Restaurante novo) {
        return ResponseEntity.ok(restauranteService.atualizar(id, novo));
    }

    /*
     * Atualizar status restaurante
     */
    @CrossOrigin(origins = "*", methods = { RequestMethod.PATCH })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestParam Boolean ativo) {
        restauranteService.atualizarStatus(id, ativo);
        return ResponseEntity.noContent().build();
    }

    /*
     * Inativar restaurante (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
