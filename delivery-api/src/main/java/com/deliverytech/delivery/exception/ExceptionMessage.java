package com.deliverytech.delivery.exception;

public class ExceptionMessage {

  // É obrigatório
  public static final String EmailObrigatorio = "O e-mail é obrigatório.";
  public static final String NomeObrigatorio = "O nome é obrigatório.";
  public static final String TelefoneObrigatorio = "O telefone é obrigatório.";
  public static final String TelefoneInvalido = "O telefone informado é inválido.";
  public static final String EnderecoObrigatorio = "O endereço é obrigatório.";
  public static final String CategoriaObrigatoria = "A categoria é obrigatória.";
  public static final String DescricaoObrigatoria = "A descrição é obrigatória.";
  public static final String NumeroPedidoObrigatorio = "O número do pedido é obrigatório.";
  public static final String StatusPedidoObrigatorio = "O status do pedido é obrigatório.";

  // Inválido
  public static final String EmailInvalido = "O e-mail informado é inválido.";
  public static final String RestauranteInvalido = "O restaurante informado é inválido.";

  // Já cadastrado
  public static final String EmailJaCadastrado = "E-mail já cadastrado.";

  // Não encontrado
  public static final String ClienteNaoEncontrado = "Cliente não encontrado.";
  public static final String RestauranteNaoEncontrado = "Restaurante não encontrado.";
  public static final String ProdutoNaoEncontrado = "Produto não encontrado.";
  public static final String PedidoNaoEncontrado = "Pedido não encontrado.";

  // Inativo
  public static final String ClienteInativo = "O cliente está inativo.";
  public static final String RestauranteInativo = "O restaurante está inativo.";

  // Outros
  public static final String PrecoDeveSerMaiorQueZero = "O preço deve ser maior que zero.";
  public static final String ProdutoNaoPertenceAoRestaurante = "O produto não pertence ao restaurante.";
  public static final String ProdutoNaoDisponivel = "O produto não está disponível.";
  public static final String PedidoNaoPodeSerCancelado = "O pedido não pode ser cancelado.";
}
