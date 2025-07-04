package com.deliverytech.delivery.service.implementations;

import com.deliverytech.delivery.dto.restaurante.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.restaurante.RestauranteResponseDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.service.interfaces.RestauranteService;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Cadastrar novo restaurante
     */
    @Override
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {

        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);

        restaurante.setAtivo(true);

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    /*
     * Buscar restaurante por ID
     */
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorId(Long id) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    /*
     * Buscar restaurante por nome
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }

    /*
     * Buscar restaurante por categoria
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaContainingIgnoreCase(categoria)
                .stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .toList();
    }

    /*
     * Listar todos os restaurantes ativos
     */
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarDisponiveis() {
        return restauranteRepository.findByAtivoTrue()
                .stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .toList();
    }

    /*
     * Atualizar dados restaurantes
     */
    @Override
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());
        restaurante.setAvaliacao(dto.getAvaliacao());

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    /*
     * Inativar restaurante (soft delete)
     */
    @Override
    public void inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        restaurante.inativar();

        restauranteRepository.save(restaurante);
    }

    /*
     * Atualizar status do restaurante (ativo/inativo)
     */
    @Override
    public void atualizarStatus(Long id, boolean ativo) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        restaurante.setAtivo(ativo);

        restauranteRepository.save(restaurante);
    }

    @Override
    public Long calcularTaxaEntrega(Long restauranteId, String cep) {
        throw new IllegalAccessError();
    }
}
