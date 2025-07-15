package com.deliverytech.delivery.services.impl;

import com.deliverytech.delivery.dto.request.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.response.ProdutoResponseDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.ExceptionMessage;
import com.deliverytech.delivery.projection.RelatorioVendasProdutos;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.services.ProdutoService;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        Produto produto = modelMapper.map(dto, Produto.class);

        produto.setRestaurante(restaurante);

        Produto produtoSalvo = produtoRepository.save(produto);

        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.RestauranteNaoEncontrado));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setDisponivel(dto.getDisponivel());
        produto.setRestaurante(restaurante);

        Produto produtoSalvo = produtoRepository.save(produto);

        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO ativarDesativar(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ProdutoNaoEncontrado));

        produto.setDisponivel(!produto.getDisponivel());

        Produto produtoSalvo = produtoRepository.save(produto);

        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorNome(String nome) {

        Produto produto = produtoRepository.findByNome(nome);

        if (!produto.getDisponivel())
            throw new BusinessException(ExceptionMessage.ProdutoNaoDisponivel, "nok");

        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId) {

        List<Produto> produtos = produtoRepository.findByRestauranteId(restauranteId);

        if (produtos.isEmpty() || produtos.stream().noneMatch(Produto::getDisponivel))
            throw new EntityNotFoundException(ExceptionMessage.ProdutosNaoEncontradosParaRestaurante);

        return produtos.stream()
                .filter(Produto::getDisponivel)
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {

        List<Produto> produtos = produtoRepository.findByCategoria(categoria);

        if (produtos.isEmpty())
            throw new EntityNotFoundException(ExceptionMessage.ProdutosNaoEncontradosParaCategoria);

        return produtos.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {

        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqual(precoMaximo);

        if (produtos.isEmpty())
            throw new EntityNotFoundException(
                    MessageFormat.format(ExceptionMessage.ProdutosNaoEncontradosParaFaixaPreco,
                            precoMinimo,
                            precoMaximo));

        return produtos.stream()
                .filter(produto -> produto.getPreco().compareTo(precoMinimo) >= 0)
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarTodosProdutos() {

        List<Produto> produtos = produtoRepository.findAll();

        if (produtos.isEmpty())
            throw new EntityNotFoundException(ExceptionMessage.NenhumProdutoEncontrado);

        return produtos.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorPrecoMenorOuIgual(BigDecimal valor) {

        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqual(valor);

        if (produtos.isEmpty())
            throw new EntityNotFoundException(
                    MessageFormat.format(ExceptionMessage.ProdutosNaoEncontradosParaPrecoMenor, valor));

        return produtos.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelatorioVendasProdutos> listarTop5MaisVendidos() {
        
        List<RelatorioVendasProdutos> relatorio = produtoRepository.listarTop5ProdutosMaisVendidos();
        if (relatorio.isEmpty())
            throw new EntityNotFoundException(ExceptionMessage.NenhumaVendaEncontrada);

        return relatorio;
    }
}
