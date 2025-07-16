package com.deliverytech.delivery.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private String role; // Deve ser: ADMIN, CLIENTE, RESTAURANTE ou ENTREGADOR
    private String restauranteId;
}