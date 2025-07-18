package com.deliverytech.delivery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    description = "Dados necessários para criar um usuário",
    title = "Register Request DTO")
public class RegisterRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private String role; // Deve ser: ADMIN, CLIENTE, RESTAURANTE ou ENTREGADOR
    private String restauranteId;
}