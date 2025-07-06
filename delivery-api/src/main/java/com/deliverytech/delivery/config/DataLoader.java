package com.deliverytech.delivery.config;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

// @Component (apenas para exemplo: não inicializar os dados via command line)
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO O CARREGAMENTO DE DADOS ===");
        inserirClientes();
        inserirRestaurantes();
        inserirProdutos();
        inserirPedidos();

        System.out.println("=== CARGA DE DADOS CONCLUIDA ===");

        testarConsultas();
    }

    private void inserirClientes() {
        System.out.println("--- Inserindo Clientes ---");

        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@email.com");
        cliente1.setTelefone("(11) 99999-1111");
        cliente1.setEndereco("Rua A, 123 - São Paulo/SP");
        cliente1.setDataCadastro(java.time.LocalDateTime.now());
        cliente1.setAtivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria Santos");
        cliente2.setEmail("maria@email.com");
        cliente2.setTelefone("(11) 99999-2222");
        cliente2.setEndereco("Rua B, 456 - São Paulo/SP");
        cliente2.setDataCadastro(java.time.LocalDateTime.now());
        cliente2.setAtivo(true);

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Pedro Oliveira");
        cliente3.setEmail("pedro@email.com");
        cliente3.setTelefone("(11) 99999-3333");
        cliente3.setEndereco("Rua C, 789 - São Paulo/SP");
        cliente3.setDataCadastro(java.time.LocalDateTime.now());
        cliente3.setAtivo(true);

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
        System.out.println("Clientes inseridos com sucesso! Total: " + clienteRepository.count());
    }

    private void inserirRestaurantes() {
        System.out.println("--- Inserindo Restaurantes ---");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Pizzaria Bella");
        restaurante1.setCategoria("Italiana");
        restaurante1.setEndereco("Av. Paulista, 1000 - São Paulo/SP");
        restaurante1.setTelefone("(11) 3333-1111");
        restaurante1.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante1.setAvaliacao(new BigDecimal("4.5"));
        restaurante1.setAtivo(true);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Burger House");
        restaurante2.setCategoria("Hamburgueria");
        restaurante2.setEndereco("Rua Augusta, 500 - São Paulo/SP");
        restaurante2.setTelefone("(11) 3333-2222");
        restaurante2.setTaxaEntrega(new BigDecimal("3.50"));
        restaurante2.setAvaliacao(new BigDecimal("4.2"));
        restaurante2.setAtivo(true);

        Restaurante restaurante3 = new Restaurante();
        restaurante3.setNome("Sushi Master");
        restaurante3.setCategoria("Japonesa");
        restaurante3.setEndereco("Rua Liberdade, 200 - São Paulo/SP");
        restaurante3.setTelefone("(11) 3333-3333");
        restaurante3.setTaxaEntrega(new BigDecimal("8.00"));
        restaurante3.setAvaliacao(new BigDecimal("4.8"));
        restaurante3.setAtivo(true);

        restauranteRepository.saveAll(Arrays.asList(restaurante1, restaurante2, restaurante3));
        System.out.println("Restaurantes inseridos com sucesso! Total: " + restauranteRepository.count());
    }

    private void inserirProdutos() {
        System.out.println("--- Inserindo Produtos ---");

        Produto produto1 = new Produto();
        produto1.setNome("Pizza Margherita");
        produto1.setDescricao("Molho de tomate, mussarela e manjericão");
        produto1.setPreco(new BigDecimal("35.90"));
        produto1.setCategoria("Pizza");
        produto1.setDisponivel(true);
        produto1.setRestaurante(restauranteRepository.findById(1L).orElse(null));

        Produto produto2 = new Produto();
        produto2.setNome("Pizza Calabresa");
        produto2.setDescricao("Molho de tomate, mussarela e calabresa");
        produto2.setPreco(new BigDecimal("38.90"));
        produto2.setCategoria("Pizza");
        produto2.setDisponivel(true);
        produto2.setRestaurante(restauranteRepository.findById(1L).orElse(null));

        Produto produto3 = new Produto();
        produto3.setNome("Lasanha Bolonhesa");
        produto3.setDescricao("Lasanha tradicional com molho bolonhesa");
        produto3.setPreco(new BigDecimal("28.90"));
        produto3.setCategoria("Massa");
        produto3.setDisponivel(true);
        produto3.setRestaurante(restauranteRepository.findById(1L).orElse(null));

        Produto produto4 = new Produto();
        produto4.setNome("X-Burger");
        produto4.setDescricao("Hambúrguer, queijo, alface e tomate");
        produto4.setPreco(new BigDecimal("18.90"));
        produto4.setCategoria("Hambúrguer");
        produto4.setDisponivel(true);
        produto4.setRestaurante(restauranteRepository.findById(2L).orElse(null));

        Produto produto5 = new Produto();
        produto5.setNome("X-Bacon");
        produto5.setDescricao("Hambúrguer, queijo, bacon, alface e tomate");
        produto5.setPreco(new BigDecimal("22.90"));
        produto5.setCategoria("Hambúrguer");
        produto5.setDisponivel(true);
        produto5.setRestaurante(restauranteRepository.findById(2L).orElse(null));

        Produto produto6 = new Produto();
        produto6.setNome("Batata Frita");
        produto6.setDescricao("Porção de batata frita crocante");
        produto6.setPreco(new BigDecimal("12.90"));
        produto6.setCategoria("Acompanhamento");
        produto6.setDisponivel(true);
        produto6.setRestaurante(restauranteRepository.findById(2L).orElse(null));

        Produto produto7 = new Produto();
        produto7.setNome("Combo Sashimi");
        produto7.setDescricao("15 peças de sashimi variado");
        produto7.setPreco(new BigDecimal("45.90"));
        produto7.setCategoria("Sashimi");
        produto7.setDisponivel(true);
        produto7.setRestaurante(restauranteRepository.findById(3L).orElse(null));

        Produto produto8 = new Produto();
        produto8.setNome("Hot Roll Salmão");
        produto8.setDescricao("8 peças de hot roll de salmão");
        produto8.setPreco(new BigDecimal("32.90"));
        produto8.setCategoria("Hot Roll");
        produto8.setDisponivel(true);
        produto8.setRestaurante(restauranteRepository.findById(3L).orElse(null));

        Produto produto9 = new Produto();
        produto9.setNome("Temaki Atum");
        produto9.setDescricao("Temaki de atum com cream cheese");
        produto9.setPreco(new BigDecimal("15.90"));
        produto9.setCategoria("Temaki");
        produto9.setDisponivel(true);
        produto9.setRestaurante(restauranteRepository.findById(3L).orElse(null));

        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3, produto4, produto5, produto6, produto7,
                produto8, produto9));
        System.out.println("Produtos inseridos com sucesso! Total: " + produtoRepository.count());
    }

    private void inserirPedidos() {
        System.out.println("--- Inserindo Pedidos ---");

        Pedido pedido1 = new Pedido();
        pedido1.setNumeroPedido("PED1234567890");
        pedido1.setDataPedido(java.time.LocalDateTime.now());
        pedido1.setStatus(StatusPedido.PENDENTE.name());
        pedido1.setValorTotal(new BigDecimal("54.80"));
        pedido1.setObservacoes("Sem cebola na pizza");
        pedido1.setCliente(clienteRepository.findById(1L).orElse(null));
        pedido1.setRestaurante(restauranteRepository.findById(1L).orElse(null));

        Pedido pedido2 = new Pedido();
        pedido2.setNumeroPedido("PED1234567891");
        pedido2.setDataPedido(java.time.LocalDateTime.now());
        pedido2.setStatus(StatusPedido.CONFIRMADO.name());
        pedido2.setValorTotal(new BigDecimal("41.80"));
        pedido2.setObservacoes("");
        pedido2.setCliente(clienteRepository.findById(2L).orElse(null));
        pedido2.setRestaurante(restauranteRepository.findById(2L).orElse(null));

        Pedido pedido3 = new Pedido();
        pedido3.setNumeroPedido("PED1234567892");
        pedido3.setDataPedido(java.time.LocalDateTime.now());
        pedido3.setStatus(StatusPedido.ENTREGUE.name());
        pedido3.setValorTotal(new BigDecimal("78.80"));
        pedido3.setObservacoes("Wasabi à parte");
        pedido3.setCliente(clienteRepository.findById(3L).orElse(null));
        pedido3.setRestaurante(restauranteRepository.findById(3L).orElse(null));

        System.out.println("--- Inserindo Itens Pedidos ---");
        ItemPedido item1 = new ItemPedido();
        item1.setQuantidade(1);
        item1.setPrecoUnitario(new BigDecimal("35.90"));
        item1.setSubtotal(new BigDecimal("35.90"));
        item1.setProduto(produtoRepository.findById(1L).orElse(null));
        item1.setPedido(pedido1);

        ItemPedido item2 = new ItemPedido();
        item2.setQuantidade(1);
        item2.setPrecoUnitario(new BigDecimal("28.90"));
        item2.setSubtotal(new BigDecimal("28.90"));
        item2.setProduto(produtoRepository.findById(3L).orElse(null));
        item2.setPedido(pedido1);

        pedido1.setItens(Arrays.asList(item1, item2));

        ItemPedido item3 = new ItemPedido();
        item3.setQuantidade(1);
        item3.setPrecoUnitario(new BigDecimal("22.90"));
        item3.setSubtotal(new BigDecimal("22.90"));
        item3.setProduto(produtoRepository.findById(5L).orElse(null));
        item3.setPedido(pedido2);

        ItemPedido item4 = new ItemPedido();
        item4.setQuantidade(1);
        item4.setPrecoUnitario(new BigDecimal("18.90"));
        item4.setSubtotal(new BigDecimal("18.90"));
        item4.setProduto(produtoRepository.findById(4L).orElse(null));
        item4.setPedido(pedido2);

        pedido2.setItens(Arrays.asList(item3, item4));

        ItemPedido item5 = new ItemPedido();
        item5.setQuantidade(1);
        item5.setPrecoUnitario(new BigDecimal("45.90"));
        item5.setSubtotal(new BigDecimal("45.90"));
        item5.setProduto(produtoRepository.findById(7L).orElse(null));
        item5.setPedido(pedido3);

        ItemPedido item6 = new ItemPedido();
        item6.setQuantidade(1);
        item6.setPrecoUnitario(new BigDecimal("32.90"));
        item6.setSubtotal(new BigDecimal("32.90"));
        item6.setProduto(produtoRepository.findById(8L).orElse(null));
        item6.setPedido(pedido3);

        pedido3.setItens(Arrays.asList(item5, item6));

        // Salvar os pedidos
        pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2, pedido3));
        System.out.println("Pedidos inseridos com sucesso! Total: " + pedidoRepository.count());
    }

    private void testarConsultas() {
        System.out.println("=== TESTANDO CONSULTAS DOS REPOSITOREIS ===");

        // Teste ClienteRepository
        System.out.println("\n--- Testando ClienteRepository ---");

        var clientesAtivos = clienteRepository.findByAtivoTrue();
        System.out.println("Clientes Ativos: " + clientesAtivos.size());

        var pedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        for (var pedido : pedidos) {
            System.out.println("Pedido: " + pedido.getNumeroPedido() + " - Cliente: " + pedido.getCliente().getNome() +
                    " - Restaurante: " + pedido.getRestaurante().getNome() + " - Status: " + pedido.getStatus());
        }
    }
}
