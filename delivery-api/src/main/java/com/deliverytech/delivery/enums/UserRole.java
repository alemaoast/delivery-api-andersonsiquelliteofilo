package com.deliverytech.delivery.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN, CLIENTE, RESTAURANTE, ENTREGADOR;

    @Override
    public String getAuthority() {
        return name();
    }
}