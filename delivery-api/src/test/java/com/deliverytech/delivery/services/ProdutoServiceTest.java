package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.projection.RelatorioVendasProdutos;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.impl.ProdutoServiceImpl;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class ProdutoServiceTest {

    @Autowired
    private ProdutoServiceImpl produtoService;

    @MockitoBean
    private ProdutoRepository produtoRepository;

    @MockitoBean
    private RestauranteRepository restauranteRepository;

    private Produto produto;
    private Restaurante restaurante;
    private ProdutoRequestDTO produtoRequestDTO;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(BigDecimal.valueOf(10.0));
        produto.setCategoria("Categoria Teste");
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);

        produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setNome(produto.getNome());
        produtoRequestDTO.setDescricao(produto.getDescricao());
        produtoRequestDTO.setPreco(produto.getPreco());
        produtoRequestDTO.setCategoria(produto.getCategoria());
        produtoRequestDTO.setDisponivel(produto.getDisponivel());
        produtoRequestDTO.setRestauranteId(restaurante.getId());
    }

    @Test
    void deveCadastrarProdutoComSucesso() {
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO response = produtoService.cadastrar(produtoRequestDTO);

        assertNotNull(response);
        assertEquals(produto.getNome(), response.getNome());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoCadastrarComRestauranteInexistente() {
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> produtoService.cadastrar(produtoRequestDTO));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        ProdutoResponseDTO response = produtoService.buscarPorId(produto.getId());

        assertNotNull(response);
        assertEquals(produto.getNome(), response.getNome());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPorIdInexistente() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> produtoService.buscarPorId(produto.getId()));
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        ProdutoRequestDTO dtoAtualizado = new ProdutoRequestDTO();
        dtoAtualizado.setNome("Nome Atualizado");
        dtoAtualizado.setDescricao("Descricao Atualizada");
        dtoAtualizado.setPreco(BigDecimal.valueOf(20.0));
        dtoAtualizado.setCategoria("Categoria Atualizada");
        dtoAtualizado.setDisponivel(false);
        dtoAtualizado.setRestauranteId(restaurante.getId());

        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO response = produtoService.atualizar(produto.getId(), dtoAtualizado);

        assertNotNull(response);
        assertEquals(dtoAtualizado.getNome(), response.getNome());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoAtualizarProdutoInexistente() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> produtoService.atualizar(produto.getId(), produtoRequestDTO));
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoAtualizarComRestauranteInexistente() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> produtoService.atualizar(produto.getId(), produtoRequestDTO));
    }

    @Test
    void deveAtivarDesativarProdutoComSucesso() {
        produto.setDisponivel(true);
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO response = produtoService.ativarDesativar(produto.getId());

        assertNotNull(response);
        assertFalse(response.getDisponivel());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoAtivarDesativarProdutoInexistente() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> produtoService.ativarDesativar(produto.getId()));
    }

    @Test
    void deveBuscarPorNomeComSucesso() {
        produto.setDisponivel(true);
        when(produtoRepository.findByNome(produto.getNome())).thenReturn(produto);

        ProdutoResponseDTO response = produtoService.buscarPorNome(produto.getNome());

        assertNotNull(response);
        assertEquals(produto.getNome(), response.getNome());
    }

    @Test
    void deveLancarBusinessExceptionSeProdutoNaoDisponivelAoBuscarPorNome() {
        produto.setDisponivel(false);
        when(produtoRepository.findByNome(produto.getNome())).thenReturn(produto);

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> produtoService.buscarPorNome(produto.getNome()));

        assertTrue(exception.getMessage().contains("O produto não está disponível."));
    }

    @Test
    void deveBuscarPorRestauranteComSucesso() {
        when(produtoRepository.findByRestauranteId(restaurante.getId())).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> response = produtoService.buscarPorRestaurante(restaurante.getId());

        assertFalse(response.isEmpty());
        assertEquals(produto.getNome(), response.get(0).getNome());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPorRestauranteSemProdutosDisponiveis() {
        Produto produtoIndisponivel = new Produto();
        produtoIndisponivel.setDisponivel(false);

        when(produtoRepository.findByRestauranteId(restaurante.getId())).thenReturn(List.of(produtoIndisponivel));

        assertThrows(EntityNotFoundException.class, () -> produtoService.buscarPorRestaurante(restaurante.getId()));
    }

    @Test
    void deveBuscarPorCategoriaComSucesso() {
        when(produtoRepository.findByCategoria(produto.getCategoria())).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> response = produtoService.buscarPorCategoria(produto.getCategoria());

        assertFalse(response.isEmpty());
        assertEquals(produto.getNome(), response.get(0).getNome());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPorCategoriaSemProdutos() {
        when(produtoRepository.findByCategoria("categoriaInexistente")).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> produtoService.buscarPorCategoria("categoriaInexistente"));
    }

    @Test
    void deveBuscarPorPrecoComSucesso() {
        when(produtoRepository.findByPrecoLessThanEqual(BigDecimal.valueOf(20))).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> response = produtoService.buscarPorPreco(BigDecimal.valueOf(5), BigDecimal.valueOf(20));

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPorPrecoSemProdutos() {
        when(produtoRepository.findByPrecoLessThanEqual(BigDecimal.valueOf(20))).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> produtoService.buscarPorPreco(BigDecimal.valueOf(5), BigDecimal.valueOf(20)));
    }

    @Test
    void deveBuscarTodosProdutosComSucesso() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> response = produtoService.buscarTodosProdutos();

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarTodosProdutosSemProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> produtoService.buscarTodosProdutos());
    }

    @Test
    void deveBuscarPorPrecoMenorOuIgualComSucesso() {
        when(produtoRepository.findByPrecoLessThanEqual(BigDecimal.valueOf(15))).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> response = produtoService.buscarPorPrecoMenorOuIgual(BigDecimal.valueOf(15));

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPorPrecoMenorOuIgualSemProdutos() {
        when(produtoRepository.findByPrecoLessThanEqual(BigDecimal.valueOf(15))).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> produtoService.buscarPorPrecoMenorOuIgual(BigDecimal.valueOf(15)));
    }

    @Test
    void deveListarTop5MaisVendidosComSucesso() {
        RelatorioVendasProdutos relatorio = mock(RelatorioVendasProdutos.class);
        when(produtoRepository.listarTop5ProdutosMaisVendidos()).thenReturn(List.of(relatorio));

        List<RelatorioVendasProdutos> response = produtoService.listarTop5MaisVendidos();

        assertFalse(response.isEmpty());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoListarTop5MaisVendidosSemResultados() {
        when(produtoRepository.listarTop5ProdutosMaisVendidos()).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> produtoService.listarTop5MaisVendidos());
    }
}