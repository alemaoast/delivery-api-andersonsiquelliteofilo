## Sumário

1. [Pré-requisitos](#pré-requisitos)
2. [Baixar a imagem do MySQL](#baixar-a-imagem-do-mysql)
3. [Criar e iniciar o container](#criar-e-iniciar-o-container)
4. [Acessar o MySQL no container](#acessar-o-mysql-no-container)
5. [Parar e remover o container](#parar-e-remover-o-container)

---

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) instalado no sistema
- PowerShell, CMD ou terminal de sua preferência

---

## Baixar a imagem do MySQL

Execute o comando abaixo para baixar a imagem oficial do MySQL 8:

```sh
docker pull mysql:8
```

---

## Criar e iniciar o container

A tabela abaixo explica os principais parâmetros usados no comando:

| Parâmetro         | Função                                         |
|-------------------|------------------------------------------------|
| `run`             | Cria e executa o container                     |
| `-d`              | Executa em segundo plano (detached)            |
| `--name`          | Define um nome amigável para o container       |
| `-e`              | Define variáveis de ambiente                   |
| `-p`              | Mapeia portas entre host e container           |
| `-v`              | Cria volume para persistência de dados         |
| `mysql:8`         | Imagem usada para o container                  |

Exemplo de comando:

```sh
docker run -d -p 3306:3306 --name delivery_db \
    -e MYSQL_ROOT_PASSWORD=123456 \
    -e MYSQL_ROOT_HOST='%' \
    -e MYSQL_DATABASE=delivery_db \
    -v mysql_data:/var/lib/mysql \
    mysql:8
```

---

## Acessar o MySQL no container

Para acessar o MySQL rodando no container, utilize:

```sh
docker exec -it delivery_db mysql -u root -p
```

Digite a senha definida em `MYSQL_ROOT_PASSWORD` (no exemplo: `123456`).

---

## Parar e remover o container

Para parar o container:

```sh
docker stop delivery_db
```

Para remover o container:

```sh
docker rm delivery_db
```

Para remover o volume de dados:

```sh
docker volume rm mysql_data
```

---
## Usando Docker Compose

Para subir todos os containers (aplicação e banco de dados) de uma só vez, utilize o comando abaixo no diretório onde está o arquivo `docker-compose.yml`:

```sh
docker-compose up --build -d
```

- O parâmetro `--build` garante que as imagens sejam reconstruídas caso haja alterações no código ou no Dockerfile.
- O parâmetro `-d` executa os containers em segundo plano (modo "detached").

Para visualizar os containers em execução:

```sh
docker-compose ps
```

Para parar e remover todos os containers, redes e volumes definidos no `docker-compose.yml`:

```sh
docker-compose down
```

---


## Referências

- [Documentação oficial do Docker](https://docs.docker.com/)
- [Documentação oficial do MySQL Docker](https://hub.docker.com/_/mysql)