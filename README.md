# Serverest API Automation

Framework de automação de testes de API desenvolvido em Java utilizando Rest Assured, JUnit 5 e Maven para validação dos principais fluxos da API ServeRest.

O projeto contempla testes funcionais positivos e negativos, validação de contrato com JSON Schema, geração de evidências com Allure Report, gerenciamento automático de massa de testes e execução automatizada através de pipeline CI/CD no GitLab.

---

## Tecnologias utilizadas

- Java 21
- Maven
- Rest Assured
- JUnit 5
- AssertJ
- Jackson
- Lombok
- Datafaker
- JSON Schema Validator
- Allure Report
- GitLab CI/CD

---

## Arquitetura do projeto

O framework utiliza separação de responsabilidades para facilitar manutenção, escalabilidade e reutilização de código.

```text
serverest-api-automation/
├── .gitlab-ci.yml
├── .gitignore
├── pom.xml
├── README.md
│
└── src/
    ├── main/
    │   └── resources/
    │       └── application.properties
    │
    └── test/
        ├── java/
        │   └── com/
        │       └── serverest/
        │           └── automation/
        │               ├── base/
        │               │   └── BaseTest.java
        │               │
        │               ├── client/
        │               │   └── ApiClient.java
        │               │
        │               ├── config/
        │               │   └── EnvironmentConfig.java
        │               │
        │               ├── constants/
        │               │   └── Endpoints.java
        │               │
        │               ├── factories/
        │               │   └── UserFactory.java
        │               │
        │               ├── models/
        │               │   └── User.java
        │               │
        │               ├── services/
        │               │   └── UserService.java
        │               │
        │               ├── specifications/
        │               │   └── RequestSpecificationFactory.java
        │               │
        │               └── tests/
        │                   ├── HealthCheckTest.java
        │                   └── users/
        │                       ├── CreateUserTest.java
        │                       ├── GetUserByIdTest.java
        │                       ├── UpdateUserTest.java
        │                       └── DeleteUserTest.java
        │
        └── resources/
            ├── allure.properties
            ├── create-user-schema.json
            ├── get-user-by-id-schema.json
            └── validation-error-schema.json
```

---

## Responsabilidades das camadas

| Camada | Responsabilidade |
|---|---|
| `base` | Configuração compartilhada e cleanup automático das massas criadas |
| `client` | Execução genérica das requisições HTTP |
| `config` | Leitura das configurações de ambiente |
| `constants` | Centralização dos endpoints da API |
| `factories` | Geração de massas de testes válidas e inválidas |
| `models` | Representação dos objetos de domínio |
| `services` | Encapsulamento das operações de negócio da API |
| `specifications` | Configuração centralizada das requisições Rest Assured |
| `tests` | Cenários automatizados e suas validações |
| `resources` | Configurações do Allure e contratos JSON Schema |

---

## Cenários automatizados

O projeto possui atualmente **10 testes automatizados**, cobrindo os principais fluxos positivos e negativos da API de usuários.

| Funcionalidade | Cenário |
|---|---|
| Health Check | Listar usuários |
| Criação | Criar usuário com sucesso |
| Criação | Impedir criação com e-mail duplicado |
| Criação | Impedir criação sem campos obrigatórios |
| Consulta | Consultar usuário existente por ID |
| Consulta | Consultar usuário inexistente |
| Atualização | Atualizar usuário existente |
| Atualização | Impedir atualização com dados inválidos |
| Exclusão | Excluir usuário existente |
| Exclusão | Validar exclusão de usuário inexistente |

---

## Pré-requisitos

Para executar o projeto localmente:

- Java 21
- Maven 3.9 ou superior
- Git

Valide as instalações:

```bash
java -version
```

```bash
mvn -version
```

```bash
git --version
```

---

## Configuração

O arquivo de configuração da API está localizado em:

```text
src/main/resources/application.properties
```

Conteúdo:

```properties
api.base.url=https://serverest.dev
api.timeout=30000
```

A classe `EnvironmentConfig` é responsável por carregar essas propriedades durante a execução.

---

## Executando os testes

### Executar todos os testes

```bash
mvn clean test
```

### Executar uma classe específica

Exemplo:

```bash
mvn clean -Dtest=CreateUserTest test
```

### Executar um único cenário

Exemplo:

```bash
mvn clean -Dtest=CreateUserTest#shouldCreateUserSuccessfully test
```

---

## Gerenciamento de massa e cleanup

Os testes que criam usuários utilizam um mecanismo centralizado de cleanup através da classe `BaseTest`.

Cada usuário criado durante um cenário é registrado:

```java
registerUserForCleanup(userId);
```

Após a execução de cada teste, o método anotado com `@AfterEach` remove automaticamente as massas registradas.

Quando o próprio cenário já exclui o recurso, o ID pode ser removido do controle:

```java
unregisterUserFromCleanup(userId);
```

Essa abordagem ajuda a manter os testes:

- Independentes
- Repetíveis
- Isolados
- Com menor impacto no ambiente compartilhado

---

## Validação de contrato com JSON Schema

O projeto realiza validação estrutural das respostas utilizando JSON Schema.

Contratos implementados:

```text
src/test/resources/
├── create-user-schema.json
├── get-user-by-id-schema.json
└── validation-error-schema.json
```

Exemplo:

```java
response.then()
        .body(matchesJsonSchemaInClasspath(
                "create-user-schema.json"
        ));
```

As validações cobrem respostas de:

- Criação de usuário
- Consulta de usuário por ID
- Erros de validação de campos obrigatórios

---

## Allure Report

O projeto utiliza Allure para geração de relatórios e evidências dos testes.

Os resultados são gerados em:

```text
target/allure-results
```

O relatório inclui informações como:

- Epic
- Feature
- Story
- Description
- Severity
- Steps de negócio
- Request HTTP
- Response HTTP
- Status do teste
- Tempo de execução

### Executar os testes e gerar os resultados

```bash
mvn clean test
```

### Abrir o relatório Allure

Neste projeto:

Primeiro rode este comando para trazer as dependências:
```bash
mvn io.qameta.allure:allure-maven:2.15.0:serve
```
Depois este:

```bash
./.allure/allure-2.30.0/bin/allure serve target/allure-results
```

> A pasta `.allure` é gerada localmente e não deve ser versionada.

---

## CI/CD com GitLab

O projeto possui pipeline configurada através do arquivo:

```text
.gitlab-ci.yml
```

A pipeline:

1. Utiliza Java 21 e Maven.
2. Executa a suíte automatizada.
3. Publica os resultados JUnit no GitLab.
4. Preserva os resultados do Allure como artifacts.
5. Preserva os relatórios do Maven Surefire para diagnóstico.

Comando executado na pipeline:

```bash
mvn clean test
```

Artefatos preservados:

```text
target/allure-results/
target/surefire-reports/
```

---

## Evidências de API

As requisições e respostas HTTP são capturadas pelo Allure através da integração com Rest Assured.

Isso permite visualizar no relatório:

- Método HTTP
- URL
- Headers
- Request body
- Status code
- Response headers
- Response body

Essa abordagem centraliza as evidências sem duplicar código em cada teste.

---

## Boas práticas aplicadas

- Separação de responsabilidades
- Reutilização de código
- Testes independentes
- Geração dinâmica de massa com Datafaker
- Cleanup automático
- Configuração externa de ambiente
- Centralização de endpoints
- Abstração do cliente HTTP
- Service Layer
- Validação de regras de negócio com AssertJ
- Validação de contrato com JSON Schema
- Evidências automáticas com Allure
- Integração contínua com GitLab CI/CD

---

## Melhorias futuras

Como possíveis evoluções:

- Suporte a múltiplos ambientes
- Execução paralela
- Retry controlado para falhas transitórias
- Expansão da validação de contratos
- Cobertura de autenticação
- Testes de produtos e carrinhos
- Publicação automática do relatório Allure
- Execução parametrizada por ambiente

---

## Autor

**Lawrense Marçal Cantão**

Projeto desenvolvido como demonstração de arquitetura e automação de testes de API.
