package com.deliverytech.delivery.dto.response;

import com.deliverytech.delivery.entity.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(
    description = "Resposta quando criar um usuario",
    title = "User Response DTO")
@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;
    private Long restauranteId;

    public static UserResponseDTO fromEntity(Usuario usuario) {
        return UserResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .restauranteId(usuario.getRestauranteId())
                .build();
    }
}
