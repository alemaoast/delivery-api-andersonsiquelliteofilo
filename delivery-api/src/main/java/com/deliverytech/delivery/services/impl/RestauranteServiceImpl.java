package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.ConflictException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.projection.RelatorioVendas;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.RestauranteService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {

        // Validar nome único
        Optional<Restaurante> byNome = restauranteRepository.findByNome(dto.getNome());
        if (byNome.isPresent() && byNome.get().getNome().equals(dto.getNome()))
            throw new ConflictException("Restaurante", "nome " + dto.getNome());

        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorId(Long id) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());
        restaurante.setAvaliacao(dto.getAvaliacao());

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO ativarDesativar(Long id) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        restaurante.setAtivo(!restaurante.getAtivo());

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorNome(String nome) {

        Restaurante restaurante = restauranteRepository.findByNomeAndAtivoTrue(nome);

        if (restaurante == null || !restaurante.getNome().equalsIgnoreCase(nome))
            throw new EntityNotFoundException("Restaurante", nome);

        if (!restaurante.getAtivo())
            throw new BusinessException(ExceptionMessage.RestauranteInativo, "");

        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {

        List<Restaurante> restaurantes = restauranteRepository.findByCategoria(categoria);
        if (restaurantes.isEmpty())
            throw new BusinessException(ExceptionMessage.RestaurantesNaoEncontradosParaCategoria, categoria);

        // Converter lista de entidades para lista de DTOs
        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    public List<RestauranteResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {

        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaBetween(precoMinimo, precoMaximo);
        if (restaurantes.isEmpty()) {
            throw new BusinessException(
                    MessageFormat.format(ExceptionMessage.RestaurantesNaoEncontradosParaFaixaPreco,
                            precoMinimo,
                            precoMaximo),
                            "");
        }

        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestauranteResponseDTO> listarAtivos(int page, int size) {

        Page<Restaurante> restaurantes = restauranteRepository.findByAtivoTrue(PageRequest.of(page, size));
        if (restaurantes.isEmpty())
            throw new BusinessException(ExceptionMessage.NenhumRestauranteEncontrado, "nok");

        return restaurantes.map(r -> modelMapper.map(r, RestauranteResponseDTO.class));
    }

    @Override
    public List<RestauranteResponseDTO> listarTop5PorNome() {

        List<Restaurante> restaurantes = restauranteRepository.findTop5ByOrderByNomeAsc();
        if (restaurantes.isEmpty())
            throw new BusinessException(ExceptionMessage.NenhumRestauranteEncontrado, "nok");

        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    public List<RelatorioVendas> relatorioVendasPorRestaurante() {

        List<RelatorioVendas> relatorio = restauranteRepository.relatorioVendasPorRestaurante();
        if (relatorio.isEmpty())
            throw new EntityNotFoundException(ExceptionMessage.NenhumaVendaEncontrada, "nok");

        return relatorio;
    }

    @Override
    public List<RestauranteResponseDTO> buscarPorTaxaEntrega(BigDecimal taxaEntrega) {

        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaLessThanEqual(taxaEntrega);
        if (restaurantes.isEmpty()) {
            throw new BusinessException(
                    MessageFormat.format(ExceptionMessage.RestauranteNaoEncontradosParaPrecoMenor, taxaEntrega), "nok");
        }

        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    public RestauranteResponseDTO inativar(Long id) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado, id));

        if (!restaurante.getAtivo())
            throw new BusinessException(ExceptionMessage.RestauranteJaInativo, "nok");

        restaurante.setAtivo(false);

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    // TODO: calcularTaxaEntrega(Long restauranteId, String cep) - Lógica de entrega
}
