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

import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @MockitoBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Test
    @DisplayName("Deve buscar o cliente com ID")
    void testBuscarClientePorId_DeveRetornarClienteResponseDTO() {
        Cliente clienteMock = new Cliente(1L, "teste", "teste@email.com", "123456789", "Av Teste", LocalDateTime.now(), true, null);
        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));
        ClienteResponseDTO resultado = clienteService.buscarPorId(1L);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("teste", resultado.getNome());
    }
}
