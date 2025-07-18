package com.deliverytech.delivery.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(
    description = "Resposta quando criar ou atualizar um cliente",
    title = "Cliente Response DTO")
@Data
public class ClienteResponseDTO {
  private Long id;
  private String nome;
  private String email;
  private String telefone;
  private String endereco;
  private Boolean ativo;
}
