package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.repository.PedidoRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Pedido salvar(Pedido pedido) {
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE"); // status como string
        return repository.save(pedido);
    }

    public Pedido atualizarStatus(Integer id, String novoStatus) {
        return repository.findById(id).map(p -> {
            p.setStatus(novoStatus.toUpperCase()); // padroniza para caixa alta
            return repository.save(p);
        }).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public void excluir(Integer id) {
        repository.deleteById(id);
    }

    public List<Pedido> buscarPorClienteId(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public List<Pedido> listarPorStatus(String status) {
        return repository.findByStatus(status.toUpperCase());
    }

    public List<Object[]> totalGastoPorCliente() {
        return repository.totalGastoPorCliente();
    }
}