package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.RestauranteRequestDTO;
import com.deliverytech.delivery.dto.response.RestauranteResponseDTO;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.ConflictException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.projection.RelatorioVendas;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.impl.RestauranteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RestauranteServiceImplTest {

    @Autowired
    private RestauranteServiceImpl restauranteService;

    @MockitoBean
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Pizza Top");
        restaurante.setCategoria("Italiana");
        restaurante.setEndereco("Rua A");
        restaurante.setTelefone("123456");
        restaurante.setTaxaEntrega(BigDecimal.valueOf(5.0));
        restaurante.setAvaliacao(BigDecimal.valueOf(4.5));
        restaurante.setAtivo(true);
    }

    @Test
    void deveCadastrarRestauranteComSucesso() {
        RestauranteRequestDTO dto = modelMapper.map(restaurante, RestauranteRequestDTO.class);

        when(restauranteRepository.findByNome(dto.getNome())).thenReturn(Optional.empty());
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteResponseDTO response = restauranteService.cadastrar(dto);

        assertNotNull(response);
        assertEquals("Pizza Top", response.getNome());
    }

    @Test
    void deveLancarConflictExceptionAoCadastrarComNomeExistente() {
        RestauranteRequestDTO dto = modelMapper.map(restaurante, RestauranteRequestDTO.class);

        when(restauranteRepository.findByNome(dto.getNome())).thenReturn(Optional.of(restaurante));

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            restauranteService.cadastrar(dto);
        });

        assertTrue(exception.getMessage().contains("Restaurante"));
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        RestauranteResponseDTO response = restauranteService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals("Pizza Top", response.getNome());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPorIdInexistente() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.buscarPorId(1L));
    }

    @Test
    void deveAtualizarRestauranteComSucesso() {
        RestauranteRequestDTO dto = new RestauranteRequestDTO();
        dto.setNome("Nova Pizza");
        dto.setCategoria("Fast Food");
        dto.setEndereco("Rua B");
        dto.setTelefone("654321");
        dto.setTaxaEntrega(BigDecimal.valueOf(6.0));
        dto.setAvaliacao(BigDecimal.valueOf(4.8));

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteResponseDTO response = restauranteService.atualizar(1L, dto);

        assertNotNull(response);
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoAtualizarRestauranteInexistente() {
        RestauranteRequestDTO dto = new RestauranteRequestDTO();
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.atualizar(1L, dto));
    }

    @Test
    void deveAtivarDesativarRestauranteComSucesso() {
        restaurante.setAtivo(true);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteResponseDTO response = restauranteService.ativarDesativar(1L);

        assertNotNull(response);
        assertFalse(response.getAtivo());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoAtivarDesativarRestauranteInexistente() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.ativarDesativar(1L));
    }

    @Test
    void deveBuscarPorNomeComSucesso() {
        restaurante.setAtivo(true);
        when(restauranteRepository.findByNomeAndAtivoTrue("Pizza Top")).thenReturn(restaurante);

        RestauranteResponseDTO response = restauranteService.buscarPorNome("Pizza Top");

        assertNotNull(response);
        assertEquals("Pizza Top", response.getNome());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPorNomeInexistente() {
        when(restauranteRepository.findByNomeAndAtivoTrue("Inexistente")).thenReturn(null);

        assertThrows(EntityNotFoundException.class,
                () -> restauranteService.buscarPorNome("Inexistente"));
    }

    @Test
    void deveLancarBusinessExceptionSeRestauranteInativoAoBuscarPorNome() {
        restaurante.setAtivo(false);
        when(restauranteRepository.findByNomeAndAtivoTrue("Pizza Top")).thenReturn(restaurante);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> restauranteService.buscarPorNome("Pizza Top"));

        assertTrue(exception.getMessage().contains("inativo"));
    }

    @Test
    void deveBuscarPorCategoriaComSucesso() {
        when(restauranteRepository.findByCategoria("Italiana")).thenReturn(List.of(restaurante));

        List<RestauranteResponseDTO> response = restauranteService.buscarPorCategoria("Italiana");

        assertFalse(response.isEmpty());
        assertEquals("Pizza Top", response.get(0).getNome());
    }

    @Test
    void deveLancarBusinessExceptionAoBuscarPorCategoriaSemResultados() {
        when(restauranteRepository.findByCategoria("Japonesa")).thenReturn(List.of());

        assertThrows(BusinessException.class,
                () -> restauranteService.buscarPorCategoria("Japonesa"));
    }

    @Test
    void deveBuscarPorPrecoComSucesso() {
        when(restauranteRepository.findByTaxaEntregaBetween(BigDecimal.valueOf(2),
                BigDecimal.valueOf(10))).thenReturn(List.of(restaurante));

        List<RestauranteResponseDTO> response =
                restauranteService.buscarPorPreco(BigDecimal.valueOf(2), BigDecimal.valueOf(10));

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarBusinessExceptionAoBuscarPorPrecoSemResultados() {
        when(restauranteRepository.findByTaxaEntregaBetween(BigDecimal.valueOf(1),
                BigDecimal.valueOf(2))).thenReturn(List.of());

        assertThrows(BusinessException.class, () -> restauranteService
                .buscarPorPreco(BigDecimal.valueOf(1), BigDecimal.valueOf(2)));
    }

    @Test
    void deveListarAtivosComSucesso() {
        when(restauranteRepository.findByAtivoTrue()).thenReturn(List.of(restaurante));

        List<RestauranteResponseDTO> response = restauranteService.listarAtivos();

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarBusinessExceptionAoListarAtivosSemResultados() {
        when(restauranteRepository.findByAtivoTrue()).thenReturn(List.of());

        assertThrows(BusinessException.class, () -> restauranteService.listarAtivos());
    }

    @Test
    void deveListarTop5PorNomeComSucesso() {
        when(restauranteRepository.findTop5ByOrderByNomeAsc()).thenReturn(List.of(restaurante));

        List<RestauranteResponseDTO> response = restauranteService.listarTop5PorNome();

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarBusinessExceptionAoListarTop5SemResultados() {
        when(restauranteRepository.findTop5ByOrderByNomeAsc()).thenReturn(List.of());

        assertThrows(BusinessException.class, () -> restauranteService.listarTop5PorNome());
    }

    @Test
    void deveRetornarRelatorioVendasPorRestauranteComSucesso() {
        RelatorioVendas relatorio = mock(RelatorioVendas.class);

        when(restauranteRepository.relatorioVendasPorRestaurante()).thenReturn(List.of(relatorio));

        List<RelatorioVendas> response = restauranteService.relatorioVendasPorRestaurante();

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoRetornarRelatorioVendasVazio() {
        when(restauranteRepository.relatorioVendasPorRestaurante()).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class,
                () -> restauranteService.relatorioVendasPorRestaurante());
    }

    @Test
    void deveBuscarPorTaxaEntregaComSucesso() {
        when(restauranteRepository.findByTaxaEntregaLessThanEqual(BigDecimal.valueOf(5)))
                .thenReturn(List.of(restaurante));

        List<RestauranteResponseDTO> response =
                restauranteService.buscarPorTaxaEntrega(BigDecimal.valueOf(5));

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarBusinessExceptionAoBuscarPorTaxaEntregaSemResultados() {
        when(restauranteRepository.findByTaxaEntregaLessThanEqual(BigDecimal.valueOf(1)))
                .thenReturn(List.of());

        assertThrows(BusinessException.class,
                () -> restauranteService.buscarPorTaxaEntrega(BigDecimal.valueOf(1)));
    }

    @Test
    void deveInativarRestauranteComSucesso() {
        restaurante.setAtivo(true);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteResponseDTO response = restauranteService.inativar(1L);

        assertNotNull(response);
        assertFalse(response.getAtivo());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoInativarRestauranteInexistente() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.inativar(1L));
    }

    @Test
    void deveLancarBusinessExceptionAoInativarRestauranteJaInativo() {
        restaurante.setAtivo(false);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        assertThrows(BusinessException.class, () -> restauranteService.inativar(1L));
    }
}
