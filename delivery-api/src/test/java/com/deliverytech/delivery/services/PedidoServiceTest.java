package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.request.PedidoRequestDTO;
import com.deliverytech.delivery.dto.response.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @MockitoBean
    private PedidoRepository pedidoRepository;

    @MockitoBean
    private ClienteRepository clienteRepository;

    @MockitoBean
    private RestauranteRepository restauranteRepository;

    @MockitoBean
    private ProdutoRepository produtoRepository;

    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;
    private Pedido pedido;
    private PedidoRequestDTO pedidoRequestDTO;
    private ItemPedidoRequestDTO itemPedidoRequestDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setAtivo(true);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setAtivo(true);
        restaurante.setTaxaEntrega(BigDecimal.valueOf(5));

        produto = new Produto();
        produto.setId(1L);
        produto.setDisponivel(true);
        produto.setPreco(BigDecimal.valueOf(10));
        produto.setRestaurante(restaurante);

        itemPedidoRequestDTO = new ItemPedidoRequestDTO();
        itemPedidoRequestDTO.setProdutoId(produto.getId());
        itemPedidoRequestDTO.setQuantidade(2);

        pedidoRequestDTO = new PedidoRequestDTO();
        pedidoRequestDTO.setClienteId(cliente.getId());
        pedidoRequestDTO.setRestauranteId(restaurante.getId());
        pedidoRequestDTO.setNumeroPedido("12345");
        pedidoRequestDTO.setObservacoes("Teste");
        pedidoRequestDTO.setEnderecoEntrega("Rua Teste");
        pedidoRequestDTO.setItens(List.of(itemPedidoRequestDTO));

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNumeroPedido(pedidoRequestDTO.getNumeroPedido());
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE.name());
        pedido.setTaxaEntrega(restaurante.getTaxaEntrega());
        pedido.setValorTotal(BigDecimal.valueOf(25)); // 2*10 + 5 taxa
        pedido.setEnderecoEntrega(pedidoRequestDTO.getEnderecoEntrega());
        pedido.setObservacoes(pedidoRequestDTO.getObservacoes());
        pedido.setDataPedido(LocalDateTime.now());
    }

    @Test
    void deveCriarPedidoComSucesso() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoResponseDTO response = pedidoService.criar(pedidoRequestDTO);

        assertNotNull(response);
        assertEquals(pedidoRequestDTO.getNumeroPedido(), response.getNumeroPedido());
        assertEquals(StatusPedido.PENDENTE.name(), response.getStatus());
        assertEquals(BigDecimal.valueOf(25), response.getValorTotal());
    }

    @Test
    void deveLancarEntityNotFoundExceptionSeClienteNaoExistir() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> pedidoService.criar(pedidoRequestDTO));
        assertEquals(ExceptionMessage.ClienteNaoEncontrado, ex.getMessage());
    }

    @Test
    void deveLancarBusinessExceptionSeClienteInativo() {
        cliente.setAtivo(false);
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));

        BusinessException ex = assertThrows(BusinessException.class,
            () -> pedidoService.criar(pedidoRequestDTO));
        assertTrue(ex.getMessage().contains("O cliente está inativo."));
    }

    @Test
    void deveLancarEntityNotFoundExceptionSeRestauranteNaoExistir() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> pedidoService.criar(pedidoRequestDTO));
        assertEquals(ExceptionMessage.RestauranteNaoEncontrado, ex.getMessage());
    }

    @Test
    void deveLancarBusinessExceptionSeRestauranteInativo() {
        restaurante.setAtivo(false);
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));

        BusinessException ex = assertThrows(BusinessException.class,
            () -> pedidoService.criar(pedidoRequestDTO));
        assertTrue(ex.getMessage().contains("O restaurante não está disponível."));
    }

    @Test
    void deveLancarEntityNotFoundExceptionSeProdutoNaoExistir() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> pedidoService.criar(pedidoRequestDTO));
        assertEquals(ExceptionMessage.ProdutoNaoEncontrado, ex.getMessage());
    }

    @Test
    void deveLancarBusinessExceptionSeProdutoNaoDisponivel() {
        produto.setDisponivel(false);
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        BusinessException ex = assertThrows(BusinessException.class,
            () -> pedidoService.criar(pedidoRequestDTO));
        assertTrue(ex.getMessage().contains("O produto não está disponível."));
    }

    @Test
    void deveLancarBusinessExceptionSeProdutoNaoPertencerAoRestaurante() {
        Produto outroProduto = new Produto();
        outroProduto.setId(2L);
        outroProduto.setDisponivel(true);
        outroProduto.setPreco(BigDecimal.valueOf(10));
        Restaurante outroRestaurante = new Restaurante();
        outroRestaurante.setId(2L);
        outroProduto.setRestaurante(outroRestaurante); // diferente

        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(outroProduto));

        BusinessException ex = assertThrows(BusinessException.class,
            () -> pedidoService.criar(pedidoRequestDTO));
        assertTrue(ex.getMessage().contains("O produto não pertence ao restaurante selecionado."));
    }

    @Test
    void deveBuscarPedidoPorIdComSucesso() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        PedidoResponseDTO response = pedidoService.buscarPorId(pedido.getId());

        assertNotNull(response);
        assertEquals(pedido.getNumeroPedido(), response.getNumeroPedido());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoBuscarPedidoPorIdInexistente() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pedidoService.buscarPorId(pedido.getId()));
    }

    @Test
    void deveListarPedidosPorClienteComSucesso() {
        when(pedidoRepository.findByClienteId(cliente.getId())).thenReturn(List.of(pedido));

        List<PedidoResponseDTO> response = pedidoService.listarPorCliente(cliente.getId());

        assertFalse(response.isEmpty());
        assertEquals(pedido.getNumeroPedido(), response.get(0).getNumeroPedido());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoListarPedidosPorClienteSemPedidos() {
        when(pedidoRepository.findByClienteId(cliente.getId())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> pedidoService.listarPorCliente(cliente.getId()));
    }

    @Test
    void deveAtualizarStatusPedidoComSucesso() {
        pedido.setStatus(StatusPedido.PENDENTE.name());

        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoResponseDTO response = pedidoService.atualizarStatus(pedido.getId(), StatusPedido.CONFIRMADO);

        assertNotNull(response);
        assertEquals(StatusPedido.CONFIRMADO.name(), pedido.getStatus()); // status set before save
        assertEquals(StatusPedido.CONFIRMADO.name(), response.getStatus());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoAtualizarStatusPedidoInexistente() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pedidoService.atualizarStatus(pedido.getId(), StatusPedido.CONFIRMADO));
    }

    @Test
    void deveLancarBusinessExceptionSeTransicaoStatusInvalida() {
        pedido.setStatus(StatusPedido.ENTREGUE.name());

        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        BusinessException ex = assertThrows(BusinessException.class,
            () -> pedidoService.atualizarStatus(pedido.getId(), StatusPedido.PENDENTE));
        assertTrue(ex.getMessage().contains("Transição de status inválida para o pedido."));
    }

    @Test
    void deveCalcularValorTotalPedidoComSucesso() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        BigDecimal total = pedidoService.calcularValorTotalPedido(List.of(itemPedidoRequestDTO));

        assertEquals(BigDecimal.valueOf(20), total);
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoCalcularValorTotalPedidoComProdutoInexistente() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pedidoService.calcularValorTotalPedido(List.of(itemPedidoRequestDTO)));
    }

    @Test
    void deveCancelarPedidoComSucesso() {
        pedido.setStatus(StatusPedido.PENDENTE.name());

        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoResponseDTO response = pedidoService.cancelar(pedido.getId());

        assertNotNull(response);
        assertEquals(StatusPedido.CANCELADO.name(), response.getStatus());
    }

    @Test
    void deveLancarEntityNotFoundExceptionAoCancelarPedidoInexistente() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pedidoService.cancelar(pedido.getId()));
    }

    @Test
    void deveLancarBusinessExceptionSePedidoJaCancelado() {
        pedido.setStatus(StatusPedido.CANCELADO.name());

        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        BusinessException ex = assertThrows(BusinessException.class, 
            () -> pedidoService.cancelar(pedido.getId()));
        assertTrue(ex.getMessage().contains("O pedido já está cancelado."));
    }

    @Test
    void deveLancarBusinessExceptionSePedidoNaoPodeSerCancelado() {
        pedido.setStatus(StatusPedido.PREPARANDO.name());

        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        BusinessException ex = assertThrows(BusinessException.class, 
            () -> pedidoService.cancelar(pedido.getId()));
        assertTrue(ex.getMessage().contains("O pedido não pode ser cancelado,  status atual: PREPARANDO"));
    }

    @Test
    void deveListarPedidosPorPeriodoComSucesso() {
        LocalDate inicio = LocalDate.now().minusDays(2);
        LocalDate fim = LocalDate.now();

        when(pedidoRepository.findByDataPedidoBetween(any(), any())).thenReturn(List.of(pedido));

        List<PedidoResponseDTO> response = pedidoService.listarPorPeriodo(inicio, fim);

        assertFalse(response.isEmpty());
        assertEquals(pedido.getNumeroPedido(), response.get(0).getNumeroPedido());
    }

    @Test
    void deveLancarBusinessExceptionSeDataInicioInvalida() {
        LocalDate inicio = LocalDate.of(2024, 12, 31);
        LocalDate fim = LocalDate.now();
        
        BusinessException ex = assertThrows(BusinessException.class, () -> pedidoService.listarPorPeriodo(inicio, fim));
        assertTrue(ex.getMessage().contains("inválida"));
    }

    @Test
    void deveLancarBusinessExceptionSeDataFimInvalida() {
        LocalDate inicio = LocalDate.now().minusDays(1);
        LocalDate fim = LocalDateTime.now().plusDays(1).toLocalDate();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, 
            () -> pedidoService.listarPorPeriodo(inicio, fim));
        assertTrue(ex.getMessage().contains("Nenhum pedido encontrado."));
    }

    @Test
    void deveLancarEntityNotFoundExceptionSeNaoHouverPedidosNoPeriodo() {
        LocalDate inicio = LocalDate.now().minusDays(2);
        LocalDate fim = LocalDate.now();

        when(pedidoRepository.findByDataPedidoBetween(any(), any())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> pedidoService.listarPorPeriodo(inicio, fim));
    }
}