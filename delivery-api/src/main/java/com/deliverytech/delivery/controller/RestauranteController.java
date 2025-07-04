package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.restaurante.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.restaurante.RestauranteResponseDTO;
import com.deliverytech.delivery.service.implementations.RestauranteServiceImpl;

import jakarta.validation.Valid;

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
    private RestauranteServiceImpl restauranteService;

    /*
     * Cadastrar novo restaurante
     */
    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@RequestBody @Valid RestauranteRequestDTO dto) {
        RestauranteResponseDTO restauranteSalvo = restauranteService.cadastrar(dto);

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
    public ResponseEntity<List<RestauranteResponseDTO>> listar() {
        return ResponseEntity.ok(restauranteService.buscarDisponiveis());
    }

    /*
     * Buscar restaurante por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    /*
     * Buscar restaurante por nome
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }

    /*
     * Buscar restaurante por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }

    /*
     * Atualizar restaurante
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid RestauranteRequestDTO dto) {
        return ResponseEntity.ok(restauranteService.atualizar(id, dto));
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
