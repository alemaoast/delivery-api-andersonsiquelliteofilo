package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.services.UsuarioService;
import com.deliverytech.delivery.dto.request.RegisterRequestDTO;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.UsuarioRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Usuario salvar(RegisterRequestDTO dto) {
        validarEmailUnico(dto.getEmail());
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void inativarUsuario(Long id) {
        Usuario usuario = buscarPorId(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    private void validarEmailUnico(String email) {
        if (existePorEmail(email)) {
            // Mensagem genérica para previnir ataques de enumeração do usuários.
            throw new RuntimeException("Erro, verificar com administrador da plataforma.");
        }
    }
}