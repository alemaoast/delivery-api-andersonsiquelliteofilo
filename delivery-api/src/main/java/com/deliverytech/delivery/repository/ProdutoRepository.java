package com.deliverytech.delivery.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.projection.RelatorioVendasProdutos;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar produtos por restaurante id
    List<Produto> findByRestauranteId(Long restauranteId);

    // Buscar produtos disponiveis
    List<Produto> findByDisponivelTrue();

    // Buscar produtos por categoria
    List<Produto> findByCategoria(String categoria);

    // Buscar produtos por preço menor ou igual a um valor especificado
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

    // Buscar produtos por nome
    Produto findByNome(String nome);

    // Relatório dos 5 produtos mais vendidos
    @Query("SELECT " +
            "p.id as idProduto, " +
            "p.nome as nomeProduto, " +
            "SUM(i.subtotal) as totalVendas, " +
            "SUM(i.quantidade) as quantidadeItemPedido " +
            "FROM Produto p " +
            "INNER JOIN ItemPedido i ON p.id = i.produto.id " +
            "GROUP BY p.id, p.nome " +
            "ORDER BY quantidadeItemPedido DESC " +
            "LIMIT 5")
    List<RelatorioVendasProdutos> listarTop5ProdutosMaisVendidos();
}
