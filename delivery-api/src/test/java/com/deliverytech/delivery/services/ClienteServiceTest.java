package com.deliverytech.delivery.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.deliverytech.delivery.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.ConflictException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.projection.RelatorioVendasClientes;
import com.deliverytech.delivery.repository.ClienteRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

        @MockitoBean
        private ClienteRepository clienteRepository;

        @Autowired
        private ClienteService clienteService;

        @Test
        @DisplayName("Cadastrar um cliente deve retornar um Cliente DTO")
        void testCadastrarCliente_DeveRetornarClienteResponseDTO() {
                ClienteRequestDTO request = new ClienteRequestDTO();
                request.setEmail("teste@email.com");
                request.setEndereco("Av Teste");
                request.setNome("teste");
                request.setTelefone("123456789");

                Cliente clienteMock = new Cliente(null, request.getNome(), request.getEmail(),
                                request.getTelefone(), request.getTelefone(), LocalDateTime.now(),
                                true, null);

                when(clienteRepository.save(any(Cliente.class)))
                                .thenReturn(clienteMock);

                ClienteResponseDTO resultado = clienteService.cadastrar(request);

                assertNotNull(resultado);
                assertEquals("teste", resultado.getNome());
        }

        @Test
        @DisplayName("Cadastrar um cliente deve retornar erro que o e-mail ja existe")
        void testCadastrarCliente_DeveRetornarErroEmailJaExiste() {
                ClienteRequestDTO request = new ClienteRequestDTO();
                request.setEmail("teste@email.com");
                request.setEndereco("Av Teste");
                request.setNome("teste");
                request.setTelefone("123456789");

                when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(true);

                var result = assertThrows(ConflictException.class,
                                () -> clienteService.cadastrar(request));

                assertEquals("Cliente com email já existe", result.getMessage());
        }

        @Test
        @DisplayName("Buscar o cliente com ID deve retornar um Cliente DTO")
        void testBuscarClientePorId_DeveRetornarClienteResponseDTO() {
                Cliente clienteMock = new Cliente(1L, "teste", "teste@email.com", "123456789",
                                "Av Teste", LocalDateTime.now(), true, null);
                when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
                ClienteResponseDTO resultado = clienteService.buscarPorId(1L);
                assertNotNull(resultado);
                assertEquals("teste", resultado.getNome());
        }

        @Test
        @DisplayName("Buscar o cliente por e-mail deve retornar um Cliente DTO")
        void testBuscarClientePorEmail_DeveRetornarClienteResponseDTO() {
                Cliente clienteMock = new Cliente(1L, "teste", "teste@email.com", "123456789",
                                "Av Teste", LocalDateTime.now(), true, null);
                when(clienteRepository.findByEmail(clienteMock.getEmail()))
                                .thenReturn(Optional.of(clienteMock));
                ClienteResponseDTO resultado =
                                clienteService.buscarPorEmail(clienteMock.getEmail());
                assertNotNull(resultado);
                assertEquals("teste@email.com", resultado.getEmail());
        }

        @Test
        @DisplayName("Atualizar um cliente deve retornar um Cliente DTO")
        void testAtualizarCliente_DeveRetornarClienteResponseDTO() {
                ClienteRequestDTO request = new ClienteRequestDTO();
                request.setEmail("teste@email.com");
                request.setEndereco("Av Teste");
                request.setNome("teste");
                request.setTelefone("123456789");

                Cliente clienteMock = new Cliente(1L, request.getNome(), request.getEmail(),
                                request.getTelefone(), request.getTelefone(), LocalDateTime.now(),
                                true, null);

                when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
                when(clienteRepository.existsByEmail(clienteMock.getEmail()))
                                .thenReturn(false);
                when(clienteRepository.save(any(Cliente.class)))
                                .thenReturn(clienteMock);

                ClienteResponseDTO resultado = clienteService.atualizar(1L, request);

                assertNotNull(resultado);
                assertEquals("teste", resultado.getNome());
        }

        @Test
        @DisplayName("Atualizar um cliente deve retornar erro que o e-mail ja existe")
        void testAtualizarCliente_DeveRetornarErroEmailJaExiste() {
                ClienteRequestDTO request = new ClienteRequestDTO();
                request.setEmail("teste@email.com");
                request.setEndereco("Av Teste");
                request.setNome("teste");
                request.setTelefone("123456789");

                Cliente clienteMock = new Cliente(1L, request.getNome(), request.getEmail(),
                                request.getTelefone(), request.getTelefone(), LocalDateTime.now(),
                                true, null);

                when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
                clienteMock.setEmail("teste@email.com2");
                when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(true);
                when(clienteRepository.save(any(Cliente.class)))
                                .thenReturn(clienteMock);

                var result = assertThrows(ConflictException.class,
                                () -> clienteService.atualizar(1L, request));

                assertEquals("Cliente com email já existe", result.getMessage());
        }

        @Test
        @DisplayName("Atualizar um cliente deve retornar erro cliente não foi encontrado")
        void testAtualizarCliente_DeveRetornarErroNaoEncontrado() {
                ClienteRequestDTO request = new ClienteRequestDTO();
                request.setEmail("teste@email.com");
                request.setEndereco("Av Teste");
                request.setNome("teste");
                request.setTelefone("123456789");

                Cliente clienteMock = new Cliente(1L, request.getNome(), request.getEmail(),
                                request.getTelefone(), request.getTelefone(), LocalDateTime.now(),
                                true, null);

                when(clienteRepository.existsByEmail(clienteMock.getEmail()))
                                .thenReturn(true);
                when(clienteRepository.save(any(Cliente.class)))
                                .thenReturn(clienteMock);

                var result = assertThrows(EntityNotFoundException.class,
                                () -> clienteService.atualizar(1L, request));

                assertEquals("Cliente com ID 1 não encontrado", result.getMessage());
        }

        @Test
        @DisplayName("Ativar / Desativar um cliente deve retornar um Cliente DTO")
        void testToggleStatusCliente_DeveRetornarClienteDTO() {

                Cliente clienteMock = new Cliente(1L, "teste", "teste@email.com", "123456789",
                                "Av Teste", LocalDateTime.now(), true, null);

                when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
                when(clienteRepository.save(any(Cliente.class)))
                                .thenReturn(clienteMock);
                var result = clienteService.ativarDesativar(1L);

                assertEquals(false, result.getAtivo());
        }

        @Test
        @DisplayName("Ativar / Desativar um cliente deve retornar erro cliente não foi encontrado")
        void testToggleStatusCliente_DeveRetornarErroNaoEncontrado() {

                var result = assertThrows(EntityNotFoundException.class,
                                () -> clienteService.ativarDesativar(1L));

                assertEquals("Cliente com ID 1 não encontrado", result.getMessage());
        }

        @Test
        @DisplayName("Listar clientes ativos deve retornar uma Lista de Cliente DTO")
        void testListarClientesAtivos_DeveRetornarListaClienteDTO() {

                List<Cliente> clientesAtivosMock =
                                List.of(new Cliente(1L, "teste", "teste@email.com", "123456789",
                                                "Av Teste", LocalDateTime.now(), true, null));

                when(clienteRepository.findByAtivoTrue()).thenReturn(clientesAtivosMock);
                var result = clienteService.listarAtivos();

                assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Listar clientes por nome deve retornar uma Lista de Cliente DTO")
        void testListarPorNome_DeveRetornarListaClienteDTO() {

                List<Cliente> clientesMock = List.of(
                                new Cliente(1L, "teste", "teste@email.com", "123456789", "Av Teste",
                                                LocalDateTime.now(), true, null),
                                new Cliente(2L, "teste novo", "teste@email2.com", "123456789",
                                                "Av Teste", LocalDateTime.now(), false, null));

                when(clienteRepository.findByNomeContainingIgnoreCase("tEste"))
                                .thenReturn(clientesMock);
                var result = clienteService.buscarPorNome("tEste");

                assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Listar clientes por nome deve retornar vazio")
        void testListarPorNome_DeveRetornarVazio() {

                var result = clienteService.buscarPorNome("tEste");
                assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Listar clientes ativos deve retornar vazio")
        void testListarClientesAtivos_DeveRetornarVazio() {

                var result = clienteService.listarAtivos();
                assertEquals(0, result.size());
        }

        @Test
        @DisplayName("Listar clientes que realizam mais pedidos deve retornar uma lista de relatório de vendas")
        void testListarTop5ClientesRealizamMaisPedidos_DeveRetornarListaRelatorioVendasClientes() {

                RelatorioVendasClientes relatorio1 = mock(RelatorioVendasClientes.class);
                RelatorioVendasClientes relatorio2 = mock(RelatorioVendasClientes.class);

                List<RelatorioVendasClientes> relatrioVendasMock = List.of(relatorio1, relatorio2);

                when(clienteRepository.listarTop5ClientesQueMaisCompram())
                                .thenReturn(relatrioVendasMock);
                var result = clienteService.listarTop5RealizamMaisPedidos();

                assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Listar clientes que realizam mais pedidos deve retornar erro nenhuma venda encontrada")
        void testListarTop5ClientesRealizamMaisPedidos_DeveRetornarErroNenhumaVendaEncontrada() {

                var result = assertThrows(BusinessException.class,
                                () -> clienteService.listarTop5RealizamMaisPedidos());

                assertEquals("Nenhum dado de vendas encontrado.", result.getMessage());
        }
}
