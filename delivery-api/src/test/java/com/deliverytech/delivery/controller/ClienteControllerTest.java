package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery.services.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteRequestDTO clienteRequestDTO;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setUp() {
        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setNome("João Silva");
        clienteRequestDTO.setTelefone("11999999999");
        clienteRequestDTO.setEmail("joao@email.com");
        clienteRequestDTO.setEndereco("Av Joao da Silva");

        clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setId(1L);
        clienteResponseDTO.setNome(clienteRequestDTO.getNome());
        clienteResponseDTO.setEmail(clienteRequestDTO.getEmail());
    }

    @Test
    @WithMockUser
    void deveCadastrarClienteComSucesso() throws Exception {
        // Arrange
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setId(1L);
        clienteResponseDTO.setNome(clienteRequestDTO.getNome());
        clienteResponseDTO.setEmail(clienteRequestDTO.getEmail());

        when(clienteService.cadastrar(clienteRequestDTO)).thenReturn(clienteResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    @WithMockUser
    void deveBuscarClientePorId() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(clienteResponseDTO);

        mockMvc.perform(get("/api/clientes/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @WithMockUser
    void deveAtualizarClienteComSucesso() throws Exception {
        when(clienteService.atualizar(eq(1L), any(ClienteRequestDTO.class)))
                .thenReturn(clienteResponseDTO);

        mockMvc.perform(put("/api/clientes/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequestDTO)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("João Silva"));
    }

    // @Test
    // void deveInativarClienteComSucesso() throws Exception {
    // doNothing().when(clienteService).inativar(1L);

    // mockMvc.perform(delete("/api/clientes/1")).andExpect(status().isNoContent());
    // }
}
