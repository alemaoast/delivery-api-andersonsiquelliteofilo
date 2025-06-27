package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteRepository repository;

    public RestauranteService(RestauranteRepository repository) {
        this.repository = repository;
    }

    public List<Restaurante> buscarPorNome(String nome) {
        return repository.findByNome(nome);
    }

    public List<Restaurante> buscarPorAtivo() {
        return repository.findByAtivo();
    }

    public List<Restaurante> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public List<Restaurante> listarTodos() {
        return repository.findAll();
    }

    public Optional<Restaurante> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Restaurante salvar(Restaurante restaurante) {
        return repository.save(restaurante);
    }

    public Restaurante atualizar(Integer id, Restaurante novo) {
        return repository.findById(id).map(r -> {
            r.setNome(novo.getNome());
            r.setCategoria(novo.getCategoria());
            r.setEndereco(novo.getEndereco());
            r.setTelefone(novo.getTelefone());
            r.setTaxaEntrega(novo.getTaxaEntrega());
            r.setAvaliacao(novo.getAvaliacao());
            r.setAtivo(novo.getAtivo());
            return repository.save(r);
        }).orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
    }

    public void excluir(Integer id) {
        repository.deleteById(id);
    }
}
