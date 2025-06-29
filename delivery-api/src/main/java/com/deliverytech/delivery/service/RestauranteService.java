package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.ResourceNotFoundException;
import com.deliverytech.delivery.repository.RestauranteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    /*
     * Cadastrar novo restaurante
     */
    public Restaurante cadastrar(Restaurante restaurante) {

        // Validações de negócio
        validarDadosRestaurante(restaurante);

        restaurante.setAtivo(true);

        return restauranteRepository.save(restaurante);
    }

    /*
     * Buscar restaurante por ID
     */
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    /*
     * Buscar restaurante por nome
     */
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    /*
     * Buscar restaurante por categoria
     */
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    /*
     * Listar todos os restaurantes ativos
     */
    @Transactional(readOnly = true)
    public List<Restaurante> listarTodosAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    /*
     * Atualizar dados restaurantes
     */
    public Restaurante atualizar(Long id, Restaurante restauranteNovo) {

        // Validações de negócio
        validarDadosRestaurante(restauranteNovo);

        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado", "id", id.toString()));

        restaurante.setNome(restauranteNovo.getNome());
        restaurante.setCategoria(restauranteNovo.getCategoria());
        restaurante.setEndereco(restauranteNovo.getEndereco());
        restaurante.setTelefone(restauranteNovo.getTelefone());
        restaurante.setTaxaEntrega(restauranteNovo.getTaxaEntrega());
        restaurante.setAvaliacao(restauranteNovo.getAvaliacao());

        return restauranteRepository.save(restaurante);
    }

    /*
     * Inativar restaurante (soft delete)
     */
    public void inativar(Long id) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado", "id", id.toString()));

        restaurante.inativar();

        restauranteRepository.save(restaurante);
    }

    /*
     * Atualizar status do restaurante (ativo/inativo)
     */
    public void atualizarStatus(Long id, boolean ativo) {

        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado", "id", id.toString()));

        restaurante.setAtivo(ativo);

        restauranteRepository.save(restaurante);
    }

    /**
     * Validações de negócio
     */
    private void validarDadosRestaurante(Restaurante restaurante) {

        if (restaurante.getNome() == null || restaurante.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (restaurante.getCategoria() == null || restaurante.getCategoria().isBlank()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }

        if (restaurante.getEndereco() == null || restaurante.getEndereco().isBlank()) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }

        if (restaurante.getTelefone() == null || restaurante.getTelefone().isBlank()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }
    }
}
