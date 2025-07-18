package com.deliverytech.delivery.dto.request;

import com.deliverytech.delivery.exception.ExceptionMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
    description = "Dados necessários para criar ou atualizar um cliente",
    title = "Cliente Request DTO")
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
