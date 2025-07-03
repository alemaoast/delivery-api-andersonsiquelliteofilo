package com.deliverytech.delivery.dto.cliente;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
  private Long id;
  private String nome;
  private String email;
  private String telefone;
  private String endereco;
  private LocalDateTime dataCadastro;
  private Boolean ativo;
}
