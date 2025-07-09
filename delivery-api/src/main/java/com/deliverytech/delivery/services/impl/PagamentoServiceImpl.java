package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.services.PagamentoService;

import jakarta.transaction.Transactional;

public class PagamentoServiceImpl implements PagamentoService {

    @Override
    @Transactional
    public void processPayment(Long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }
}
