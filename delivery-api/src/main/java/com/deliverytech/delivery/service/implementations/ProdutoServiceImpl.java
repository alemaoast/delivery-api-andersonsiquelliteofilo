package com.deliverytech.delivery.service.implementations;

import com.deliverytech.delivery.dto.produto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.produto.ProdutoResponseDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.service.interfaces.ProdutoService;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    /*
     * Cadastro de um novo produto
     */
    @Override
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {

        Produto produto = modelMapper.map(dto, Produto.class);

        Produto produtoSalvo = produtoRepository.save(produto);

        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    /*
     * Busca de produto por ID
     */
    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    /*
     * Buscar produtos por restaurante ID
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }

    /*
     * Buscar produtos por categoria
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaContainingIgnoreCase(categoria)
                .stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }

    /*
     * Atualização de produto
     */
    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setDisponivel(dto.getDisponivel());

        Produto produtoSalvo = produtoRepository.save(produto);

        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    /*
     * Alterar disponibilidade
     */
    @Override
    public ProdutoResponseDTO alterarDisponibilidade(Long id, boolean disponivel) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

        produto.setDisponivel(disponivel);

        Produto produtoSalvo = produtoRepository.save(produto);

        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }
}
