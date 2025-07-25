# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias

- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados

- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como executar

1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acesse: http://localhost:8080/health

## 🧪 Como executar os testes unitários

1. Execute: `./mvnw test`
1. Para obter a cobertura de teste, execute: `./mvnw clean test jacoco:report`
- Após executado, o arquivo de saída da covertura de teste estará em: `target > site > jacoco > index.html`

## 📋 Endpoints

- GET /health - Status da aplicação (inclui versão Java)
- GET /info - Informações da aplicação
- GET /h2-console - Console do banco H2
- GET /actuator/metrics - Métricas detalhadas da aplicação (requer Spring Boot Actuator)

## 🔧 Configuração

- Porta: 8080
- Banco: H2 em memória
- Profile: development

## 👨‍💻 Desenvolvedor

[Anderson S.T.] - [SDE - TI 28 - ARQUITETURA DE SISTEMAS (NOITE) 02459 19h00 às 20h00]  
Desenvolvido com JDK 21 e Spring Boot 3.4.7
