package com.deliverytech.delivery.dto.cliente;

import com.deliverytech.delivery.exception.ExceptionMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {
  @NotBlank(message = ExceptionMessage.NomeObrigatorio)
  private String nome;

  @Email(message = ExceptionMessage.EmailInvalido)
  @NotBlank(message = ExceptionMessage.EmailObrigatorio)
  private String email;

  @NotBlank(message = ExceptionMessage.TelefoneObrigatorio)
  @Pattern(regexp = "\\d{10,11}", message = ExceptionMessage.TelefoneInvalido)
  private String telefone;

  @NotBlank(message = ExceptionMessage.EnderecoObrigatorio)
  private String endereco;
}
