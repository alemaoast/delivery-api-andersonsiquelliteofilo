package com.deliverytech.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.deliverytech.delivery.dto.request.LoginRequestDTO;
import com.deliverytech.delivery.dto.request.RegisterRequestDTO;
import com.deliverytech.delivery.dto.response.LoginResponseDTO;
import com.deliverytech.delivery.dto.response.UserResponseDTO;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.security.JwtUtil;
import com.deliverytech.delivery.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas com autenticação e cadastro de usuários")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Login de usuário", description = "Realizar o login do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Login inválido", content = @Content(schema = @Schema(implementation = Void.class))),
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            // Tenta autenticar com as credenciais fornecidas
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

            // Se a autenticação for bem-sucedida, gera um token JWT
            String email = authentication.getName();
            String token = jwtUtil.gerarToken(email);

            LoginResponseDTO dto = new LoginResponseDTO(token);
            return ResponseEntity.ok(dto);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registrar um usuário", description = "Cadastrar um novo usuário na plataforma")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Registro inválido", content = @Content(schema = @Schema(implementation = Void.class))),
    })
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        Usuario novoUsuario = usuarioService.salvar(request);
        UserResponseDTO response = UserResponseDTO.fromEntity(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
