package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Produto> listarPorRestaurante(Integer restauranteId) {
        return repository.findByRestauranteId(restauranteId);
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public Produto atualizar(Integer id, Produto novo) {
        return repository.findById(id).map(p -> {
            p.setNome(novo.getNome());
            p.setDescricao(novo.getDescricao());
            p.setPreco(novo.getPreco());
            p.setCategoria(novo.getCategoria());
            p.setDisponivel(novo.getDisponivel());
            p.setRestaurante(novo.getRestaurante());
            return repository.save(p);
        }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void excluir(Integer id) {
        repository.deleteById(id);
    }

    public List<Produto> buscarPorRestauranteId(Integer restauranteId) {
        return repository.findByRestauranteId(restauranteId);
    }

    public List<Produto> listarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public List<Produto> listarPorDisponivel(Boolean disponivel) {
        return repository.findByDisponivel(disponivel);
    }
}
