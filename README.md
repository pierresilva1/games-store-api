# Projeto Game Store API

Este projeto é um microserviço de e-commerce para uma loja de video games desenvolvido com Spring Boot, utilizando banco de dados relacional MySQL e preparado para deploy gratuito no Render.

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL (compatível com FreeSQL)
- Swagger (Documentação)
- Docker & Docker Compose
- JUnit 5 + Mockito
- Maven

## 🧠 Entidade Principal

A entidade principal do projeto é `Produto`, com os seguintes campos:

- `id`: Long
- `nome`: String
- `plataforma`: String
- `preco`: BigDecimal
- `estoque`: Integer

## ✅ Como Executar Localmente com MySQL

### 1. Crie um banco de dados no FreeSQL

No [https://freesqldatabase.com](https://freesqldatabase.com), ao criar uma conta gratuita, será fornecido:

- Nome do banco (`DB Name`)
- Usuário (`DB Username`)
- Senha (`DB Password`)
- Host e porta

**Importante:** o banco já vem criado, então apenas conecte-se a ele:

```sql
-- Conecte-se ao banco existente usando:
USE nome_do_banco; -- Substitua com o nome exato fornecido
```

### 2. Configure o `application.properties`

```properties
spring.datasource.url=jdbc:mysql://<host>:<port>/<nome_do_banco>
spring.datasource.username=<usuario>
spring.datasource.password=<senha>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 3. Execute localmente

```bash
./mvnw spring-boot:run
```

Acesse a documentação Swagger:

📍 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## 🐳 Rodando com Docker

```bash
docker-compose down -v
docker-compose up --build
```

## ☁️ Deploy Gratuito no Render

1. Faça login no [https://render.com](https://render.com)
2. Clique em "New Web Service"
3. Selecione o repositório com este projeto
4. Configure:
   - Build Command: `./mvnw clean package -DskipTests`
   - Start Command: `java -jar target/*.jar`
   - Environment: `Docker`
5. Adicione as variáveis de ambiente:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`

Exemplo:

```
SPRING_DATASOURCE_URL=jdbc:mysql://sql.freedb.tech:3306/freedb_nome
SPRING_DATASOURCE_USERNAME=freedb_user
SPRING_DATASOURCE_PASSWORD=senha
```

## 🧪 Testes

Execute os testes:

```bash
./mvnw test
```

## 📂 Estrutura Padrão

- `controller/`: Camada de controle da API
- `service/`: Lógica de negócios
- `repository/`: Repositórios JPA
- `model/`: Entidades JPA
- `config/`: Configurações (Swagger, etc.)