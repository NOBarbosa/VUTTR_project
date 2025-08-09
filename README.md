# VUTTR API — README

API simples para gerenciamento de **Users** e suas **Tools**, com autenticação **HTTP Basic**.

---

## Sumário

* [Como rodar o projeto](#como-rodar-o-projeto)
* [Banco H2 (console e acesso)](#banco-h2-console-e-acesso)
* [Spring doc](#spring-doc)
* [Autenticação (HTTP Basic)](#autenticação-http-basic)
* [Rotas](#rotas)

  * [Users](#users)
  * [Tools (por usuário)](#tools-por-usuário)
* [Payloads de exemplo](#payloads-de-exemplo)
* [Testes com cURL](#testes-com-curl)
* [Autenticar no Postman](#autenticar-no-postman)
* [Códigos de status & erros](#códigos-de-status--erros)

---

## Como rodar o projeto

Pré-requisitos:

* Java mais recente (ou compatível com o projeto)
* Maven 3.8+

Comando:

`new -> project from existing resouces -> meaven -> ok
VuttrApplication.java then play`

ou 
```bash
mvn spring-boot:run
```

Configurações típicas (em `application.properties`):

```properties
# H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# DataSource H2 em memória
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```


---

## Banco H2 (console e acesso)

* Console: **`http://localhost:8080/h2-console`**
* **JDBC URL**: `jdbc:h2:mem:testdb`
* **User**: ``
* **Password**: *(vazio)*
* **Observação**: o console H2 já está liberado no `SecurityConfig` (frameOptions sameOrigin e `permitAll`).

------

## Spring doc

* Console: **`http://localhost:8080/swagger-ui/index.html`**

---

## Autenticação (HTTP Basic)

* **Tipo**: Basic (usuário/senha)
* **Usuário**: o **email** cadastrado
* **Senha**: a senha cadastrada (salva como **BCrypt** no banco)

### Regras atuais do `SecurityConfig` (recomendado)

* `POST /user` → **público** (cadastro)
* `/h2-console/**` → **público** (dev)
* Demais rotas → **autenticadas** (HTTP Basic)

> No backend, o usuário autenticado é carregado via `UserDetailsService` por **email**.

---

## Rotas

### Base

Todas as rotas de usuário estão sob o prefixo **`/user`**.

### Users

#### GET `/user`

Lista usuários.

* **Auth**: Requerida (Basic)
* **Response**: `200 OK` → `UserCreateResponseDTo[]`

#### POST `/user`

Cria um usuário (cadastro).

* **Auth**: **Não** requer
* **Body**: `UserCreateDto`
* **Response**: `201 Created` (recomendado) ou `200 OK` (implementado no seu controller retorna 200 com DTO)

#### DELETE `/user/{userId}`

Remove um usuário por ID.

* **Auth**: Requerida
* **Response**: `200 OK` (sem body)

### Tools (por usuário)

#### POST `/user/{userId}/tools`

Cria uma tool para o usuário.

* **Auth**: Requerida
* **Body**: `ToolsCreateDto`
* **Response**: `200 OK` (sem body)

#### GET `/user/{userId}/tools`

Lista tools do usuário, com filtro opcional por tag.

* **Auth**: Requerida
* **Query**: `?tag=node` (opcional)
* **Response**: `200 OK` → `Tools[]`

#### GET `/user/{userId}/tools/{toolId}`

Busca uma tool específica do usuário.

* **Auth**: Requerida
* **Response**: `200 OK` → `Tools`

#### PATCH `/user/{userId}/tools/{toolId}`

Edição **parcial** de uma tool: atualiza **somente** os campos enviados.

* **Auth**: Requerida
* **Body**: `ToolEditDto` (todos os campos opcionais)
* **Response**: `200 OK` → `Tools` (atualizada)

#### DELETE `/user/{userId}/tools/{toolId}`

Remove uma tool do usuário.

* **Auth**: Requerida
* **Response**: `200 OK` (sem body)

> Observação recomendada: validar **ownership** (o `{userId}` do path deve ser o mesmo do usuário autenticado), evitando IDOR.

---

## Payloads de exemplo

### `UserCreateDto` (POST `/user`)

```json
{
  "name": "Natália Oliveira",
  "email": "nati@email.com",
  "password": "minhaSenha123"
}
```

### `ToolsCreateDto` (POST `/user/{userId}/tools`)

```json
{
  "title": "json-server",
  "link": "https://github.com/typicode/json-server",
  "description": "Fake REST API based on a JSON schema",
  "tags": ["api", "json", "mock"]
}
```

### `ToolEditDto` (PATCH `/user/{userId}/tools/{toolId}`)

> Todos os campos são opcionais. Só os campos presentes serão atualizados.

* Alterar todos:

```json
{
  "title": "json-server atualizado",
  "link": "https://github.com/typicode/json-server",
  "description": "REST API fake para prototipagem",
  "tags": ["api", "mock", "devtools"]
}
```

* Alterar só o link:

```json
{
  "link": "https://novo-link.dev/tool"
}
```

* Alterar descrição e limpar tags:

```json
{
  "description": "Ferramenta para APIs fake",
  "tags": []
}
```

---

## Testes com cURL

### 1) Criar usuário (público)

```bash
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Natália Oliveira",
    "email": "nati@email.com",
    "password": "minhaSenha123"
  }'
```

### 2) Criar tool para o usuário (autenticado)

```bash
curl -u "nati@email.com:minhaSenha123" \
  -X POST http://localhost:8080/user/1/tools \
  -H "Content-Type: application/json" \
  -d '{
    "title": "json-server",
    "link": "https://github.com/typicode/json-server",
    "description": "Fake REST API based on a JSON schema",
    "tags": ["api", "json", "mock"]
  }'
```

### 3) Editar parcialmente uma tool (autenticado)

```bash
curl -u "nati@email.com:minhaSenha123" \
  -X PATCH http://localhost:8080/user/1/tools/5 \
  -H "Content-Type: application/json" \
  -d '{"link":"https://novo-link.dev/tool"}'
```

### 4) Listar tools (com filtro opcional)

```bash
curl -u "email:senha" \
  "http://localhost:8080/user/1/tools?tag=node"
```

### 5) Deletar tool

```bash
curl -u "email:senha" \
  -X DELETE http://localhost:8080/user/1/tools/5
```

---

## Autenticar no Postman

1. Selecione a requisição (ex.: `PATCH /user/{userId}/tools/{toolId}`).
2. Aba **Authorization** → **Type: Basic Auth**.
3. **Username**: seu email cadastrado (ex.: `nati@email.com`)
4. **Password**: sua senha (ex.: `minhaSenha123`)
5. O Postman adicionará automaticamente o header `Authorization: Basic <base64>`.

> Para rotas públicas (ex.: `POST /user`), não configure Auth.

---

## Códigos de status & erros

* `200 OK` — sucesso em GET/PATCH/DELETE/POST (conforme seu controller).
* `201 Created` — recomendado para criação (ajustável no controller).
* `400 Bad Request` — payload inválido (ex.: tags com item vazio, se validado).
* `401 Unauthorized` — credenciais ausentes/erradas.
* `403 Forbidden` — tentar acessar/alterar recurso de outro usuário (sugestão implementar).
* `404 Not Found` — usuário/tool não encontrados.
* `409 Conflict` — email já cadastrado (`UniqueEmailException`).

---
