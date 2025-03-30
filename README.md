# AluMind Project

Este é um projeto que envolve a análise de feedbacks de usuários, com um backend em Java utilizando Spring Boot, banco de dados PostgreSQL, e um frontend desenvolvido com React.

## Estrutura do Projeto

O projeto está dividido em duas partes principais:

- **Backend**: Responsável pela lógica do servidor, comunicação com o banco de dados e processamento de feedbacks.
- **Frontend**: Responsável pela interface do usuário e exibição dos dados provenientes do backend.

A seguir, estão os passos para configurar e executar o projeto em seu ambiente local ou em contêineres Docker.

---

## Como Executar o Projeto

### Requisitos

- **Docker**: Certifique-se de ter o Docker instalado. Para instruções de instalação, consulte a [documentação do Docker](https://docs.docker.com/get-docker/).
- **Docker Compose**: Usado para orquestrar os contêineres. O Docker Compose é incluído no Docker Desktop.

### Passo 1: Subir o Projeto Usando o Docker Compose

1. **Clone o repositório**:

    ```bash
    git clone <link-do-repositorio>
    cd ProjectAlumind
    ```

2. **Suba os contêineres**:

    Execute o seguinte comando na raiz do projeto para subir os contêineres:

    ```bash
    docker-compose up --build
    ```

3. **Acesse a aplicação**:

    - Backend estará disponível em http://localhost:8080.
    - Frontend estará disponível em http://localhost:5173.

### Passo 2: Rodando Separadamente

#### Rodando o Backend Separado (Spring Boot)

1. **Configuração**:

    Abra o arquivo `Backend/alumind/src/main/resources/application.properties`.

    Configure as variáveis de ambiente de banco de dados para que o Spring Boot se conecte ao banco (caso queira rodar localmente e não no Docker):

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
    spring.datasource.username=postgres
    spring.datasource.password=admin
    spring.jpa.hibernate.ddl-auto=update
    ```

2. **Rodando o Backend**:

    Se você não quer usar o Docker para o backend, você pode rodar o backend localmente com o comando Maven:

    ```bash
    cd Backend/alumind
    ./mvnw spring-boot:run
    ```

    O backend estará disponível em http://localhost:8080.

#### Rodando o Frontend Separado (React + Vite)

1. **Configuração**:

    Abra o arquivo `Frontend/package.json` e verifique a configuração do servidor de desenvolvimento (dev).

2. **Rodando o Frontend**:

    Navegue até o diretório do frontend e execute:

    ```bash
    cd Frontend
    npm install
    npm run dev
    ```

    O frontend estará disponível em http://localhost:5173.

## Variáveis de Ambiente

Algumas variáveis de ambiente importantes para o backend:

- `SPRING_DATASOURCE_URL`: URL de conexão com o banco de dados.
- `SPRING_DATASOURCE_USERNAME`: Nome de usuário do banco de dados.
- `SPRING_DATASOURCE_PASSWORD`: Senha do banco de dados.

### Backend (application.properties):

Caso o backend não esteja sendo executado dentro do Docker, você precisará configurar as variáveis de ambiente no arquivo `application.properties` para o banco de dados corretamente.

## Comandos Úteis

- **Parar todos os containers**:

    ```bash
    docker-compose down
    ```

- **Reiniciar os containers**:

    ```bash
    docker-compose restart
    ```

- **Ver os logs de um container específico**:

    ```bash
    docker logs <nome-do-container>
    ```

- **Construir e rodar novamente o docker-compose**:

    ```bash
    docker-compose up --build
    ```

## Considerações Finais

Este projeto foi desenvolvido utilizando Docker Compose para orquestrar os contêineres, garantindo que o ambiente de desenvolvimento seja consistente e fácil de configurar.

O backend foi feito com Spring Boot e o banco de dados utilizado é o PostgreSQL.

O frontend foi feito com React e utiliza o Vite como bundler.

### Envio de Relatórios Semanais

Foi criada uma thread para o envio de e-mails com o relatório semanal. A configuração dessa thread pode ser encontrada no arquivo:

`src/main/java/com/api/v1/alumind/scheduler/SchuedulerAPI.java`

Na linha **34**, você encontrará a configuração para a execução da tarefa todos os **sábados às 7h da manhã**, enviando e-mails para os stakeholders. Os emails devem ser configurados no arquivo `application.properties` da seguinte forma:

```
Properties:
stakeholders.emails={STAKHOLDERS_EMAILS} Alterar Para "teste@teste.com.br, teste2@teste.com"

Poderá também alterar a variável STAKHOLDERS_EMAILS do docker-compose

```

- **Importante**: Para o teste do envio de email ao subir a aplicação insira um email válido no properties que atualmente está configurado para sempre enviar email ao subir a aplicação.

**Após essa configuração, basta reiniciar a aplicação para testar o envio do relatório semanal.**

Se tiver qualquer dúvida, entre em contato!

WhatsApp: (21) 97306-3532

Email: dev.felixarthur@gmail.com