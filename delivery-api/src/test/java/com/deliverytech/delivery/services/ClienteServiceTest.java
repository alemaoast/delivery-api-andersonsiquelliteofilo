package com.deliverytech.delivery.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.deliverytech.delivery.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.exception.ConflictException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.repository.ClienteRepository;

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

        Cliente clienteMock = new Cliente(
                null,
                request.getNome(),
                request.getEmail(),
                request.getTelefone(),
                request.getTelefone(),
                LocalDateTime.now(),
                true,
                null);

        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteMock);

        ClienteResponseDTO resultado = clienteService.cadastrar(request);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("teste", resultado.getNome());
    }

    @Test
    @DisplayName("Cadastrar um cliente deve retornar erro que o e-mail ja existe")
    void testCadastrarCliente_DeveRetornarErroEmailJaExiste() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setEmail("teste@email.com");
        request.setEndereco("Av Teste");
        request.setNome("teste");
        request.setTelefone("123456789");

        Mockito.when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(true);

        var result = Assertions.assertThrows(
                ConflictException.class,
                () -> clienteService.cadastrar(request));

        Assertions.assertEquals("Cliente com email já existe", result.getMessage());
    }

    @Test
    @DisplayName("Buscar o cliente com ID deve retornar um Cliente DTO")
    void testBuscarClientePorId_DeveRetornarClienteResponseDTO() {
        Cliente clienteMock = new Cliente(1L, "teste", "teste@email.com", "123456789", "Av Teste", LocalDateTime.now(),
                true, null);
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
        ClienteResponseDTO resultado = clienteService.buscarPorId(1L);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("teste", resultado.getNome());
    }

    @Test
    @DisplayName("Buscar o cliente por e-mail deve retornar um Cliente DTO")
    void testBuscarClientePorEmail_DeveRetornarClienteResponseDTO() {
        Cliente clienteMock = new Cliente(1L, "teste", "teste@email.com", "123456789", "Av Teste", LocalDateTime.now(),
                true, null);
        Mockito.when(clienteRepository.findByEmail(clienteMock.getEmail())).thenReturn(Optional.of(clienteMock));
        ClienteResponseDTO resultado = clienteService.buscarPorEmail(clienteMock.getEmail());
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("teste@email.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Atualizar um cliente deve retornar um Cliente DTO")
    void testAtualizarCliente_DeveRetornarClienteResponseDTO() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setEmail("teste@email.com");
        request.setEndereco("Av Teste");
        request.setNome("teste");
        request.setTelefone("123456789");

        Cliente clienteMock = new Cliente(
                1L,
                request.getNome(),
                request.getEmail(),
                request.getTelefone(),
                request.getTelefone(),
                LocalDateTime.now(),
                true,
                null);

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
        Mockito.when(clienteRepository.existsByEmail(clienteMock.getEmail())).thenReturn(false);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteMock);

        ClienteResponseDTO resultado = clienteService.atualizar(1L, request);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("teste", resultado.getNome());
    }

    @Test
    @DisplayName("Atualizar um cliente deve retornar erro que o e-mail ja existe")
    void testAtualizarCliente_DeveRetornarErroEmailJaExiste() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setEmail("teste@email.com");
        request.setEndereco("Av Teste");
        request.setNome("teste");
        request.setTelefone("123456789");

        Cliente clienteMock = new Cliente(
                1L,
                request.getNome(),
                request.getEmail(),
                request.getTelefone(),
                request.getTelefone(),
                LocalDateTime.now(),
                true,
                null);

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
        clienteMock.setEmail("teste@email.com2");
        Mockito.when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(true);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteMock);

        var result = Assertions.assertThrows(
                ConflictException.class,
                () -> clienteService.atualizar(1L, request));

        Assertions.assertEquals("Cliente com email já existe", result.getMessage());
    }

    @Test
    @DisplayName("Atualizar um cliente deve retornar erro cliente não foi encontrado")
    void testAtualizarCliente_DeveRetornarErroNaoEncontrado() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setEmail("teste@email.com");
        request.setEndereco("Av Teste");
        request.setNome("teste");
        request.setTelefone("123456789");

        Cliente clienteMock = new Cliente(
                1L,
                request.getNome(),
                request.getEmail(),
                request.getTelefone(),
                request.getTelefone(),
                LocalDateTime.now(),
                true,
                null);

        Mockito.when(clienteRepository.existsByEmail(clienteMock.getEmail())).thenReturn(true);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteMock);

        var result = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> clienteService.atualizar(1L, request));

        Assertions.assertEquals("Cliente com ID 1 não encontrado", result.getMessage());
    }


    /*
     * ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto);
     * 
     * ClienteResponseDTO ativarDesativar(Long id);
     * 
     * List<ClienteResponseDTO> listarAtivos();
     * 
     * List<ClienteResponseDTO> buscarPorNome(String nome);
     * 
     * List<RelatorioVendasClientes> listarTop5RealizamMaisPedidos();
     */
}
