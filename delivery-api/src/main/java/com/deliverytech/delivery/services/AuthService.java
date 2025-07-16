package com.deliverytech.delivery.services;

import com.deliverytech.delivery.dto.request.LoginRequestDTO;
import com.deliverytech.delivery.dto.request.RegisterRequestDTO;
import com.deliverytech.delivery.dto.response.LoginResponseDTO;
import com.deliverytech.delivery.dto.response.UserResponseDTO;
import com.deliverytech.delivery.entity.Usuario;

public interface AuthService {
    
    LoginResponseDTO login(LoginRequestDTO request);

    Usuario register(RegisterRequestDTO request);

    UserResponseDTO getCurrentUser();
}
