package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.exception.ResourceNotFoundException;
import com.deliverytech.delivery.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /*
     * Cadastro de um novo produto
     */
    public Produto cadastrar(Produto produto) {

        // Validações de negócio
        validarDadosProdutoCadastro(produto);
        validarDadosProduto(produto);

        return produtoRepository.save(produto);
    }

    /*
     * Busca de produto por ID
     */
    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    /*
     * Buscar produtos por restaurante ID
     */
    @Transactional(readOnly = true)
    public List<Produto> buscarPorRestauranteId(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    /*
     * Buscar produtos disponiveis por restaurante ID
     */
    @Transactional(readOnly = true)
    public List<Produto> listarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findByDisponivelTrueAndRestauranteId(restauranteId);
    }

    /*
     * Buscar produtos por categoria
     */
    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    /*
     * Atualização de produto
     */
    public Produto atualizar(Long id, Produto produtoNovo) {

        // Validações de negócio
        validarDadosProduto(produtoNovo);

        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado", "id", id.toString()));

        produto.setNome(produtoNovo.getNome());
        produto.setDescricao(produtoNovo.getDescricao());
        produto.setPreco(produtoNovo.getPreco());
        produto.setCategoria(produtoNovo.getCategoria());
        produto.setDisponivel(produtoNovo.getDisponivel());

        return produtoRepository.save(produto);
    }

    /*
     * Exclusão de produto por ID
     */
    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }

    /*
     * Validações de negócio
     */
    private void validarDadosProdutoCadastro(Produto produto) {
        if (produto.getRestaurante() == null || produto.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("Restaurante é obrigatório");
        }
    }
    private void validarDadosProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
    }
}
