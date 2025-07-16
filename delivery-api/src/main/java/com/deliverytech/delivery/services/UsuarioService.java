package com.deliverytech.delivery.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import com.deliverytech.delivery.dto.request.RegisterRequestDTO;
import com.deliverytech.delivery.entity.Usuario;

public interface UsuarioService {

    Usuario salvar(RegisterRequestDTO usuario);

    UserDetails buscarPorEmail(String email);

    boolean existePorEmail(String email);

    Optional<Usuario> buscarPorId(Long id);

    void inativarUsuario(Long id);
}