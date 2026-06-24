# Sistema Açaiteria 🍓

Sistema de gerenciamento de pedidos para uma loja de açaí, desenvolvido em **Java** com **JavaFX**, como projeto da disciplina de Programação Orientada a Objetos (UFERSA).

Permite cadastrar produtos, adicionais, clientes e funcionários, registrar pedidos, gerar relatórios de vendas e controlar o estoque de adicionais.

## Tecnologias utilizadas

- **Java 24**
- **JavaFX 22** (interface gráfica)
- **MySQL 8** (banco de dados)
- **Maven** (gerenciamento de dependências)
- **MySQL Connector/J 9.3.0**

## Pré-requisitos

Antes de começar, você precisa ter instalado na sua máquina:

- [JDK 24](https://www.oracle.com/java/technologies/downloads/) (ou superior)
- [MySQL Server](https://dev.mysql.com/downloads/mysql/) (e algum cliente para gerenciá-lo, como o [MySQL Workbench](https://dev.mysql.com/downloads/workbench/))
- [Maven](https://maven.apache.org/download.cgi) (geralmente já vem embutido na IDE)
- Uma IDE com suporte a JavaFX, como [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

## 1. Clonando o projeto

```bash
git clone https://github.com/MariaJuliaAS/ProjetoAcaiteria.git
cd ProjetoAcaiteria
```

## 2. Criando o banco de dados

O arquivo `db.properties` (configuração de conexão) **não é enviado para o GitHub** por segurança — ele contém usuário e senha do banco, e cada pessoa precisa ter o seu próprio. Por isso, antes de rodar o projeto, você precisa:

### 2.1. Criar o banco e as tabelas

1. Abra o MySQL Workbench (ou seu cliente MySQL de preferência) e conecte-se ao seu servidor MySQL local
2. Abra o arquivo **`script_banco_dados.sql`** (que acompanha este repositório/README)
3. Execute o script inteiro (ele cria o banco `acaiteria`, todas as tabelas, e já insere um usuário administrador padrão)

### 2.2. Confirmar o usuário administrador

O script já cadastra um administrador padrão para você conseguir entrar no sistema na primeira vez:

| Usuário | Senha |
|---------|-------|
| `admin` | `admin` |

Depois de logar, você pode cadastrar outros funcionários pela própria tela do sistema (menu **Funcionários**, visível apenas para quem tem cargo Admin).

## 3. Configurando a conexão com o banco

Na **raiz do projeto** (mesma pasta do `pom.xml`), crie um arquivo chamado **`db.properties`** com o seguinte conteúdo:

```properties
user=SEU_USUARIO_MYSQL
password=SUA_SENHA_MYSQL
dburl=jdbc:mysql://localhost/acaiteria
```

Substitua `SEU_USUARIO_MYSQL` e `SUA_SENHA_MYSQL` pelas credenciais do seu MySQL local.

> ⚠️ **Atenção ao local do arquivo**: ele precisa ficar exatamente na raiz do projeto (ao lado do `pom.xml`), e não dentro de `src/` ou `resources/`. O programa lê esse arquivo com um caminho relativo, então se ele estiver no lugar errado, a conexão com o banco vai falhar.

> 🔒 Esse arquivo já está listado no `.gitignore` — ou seja, mesmo que você o crie, ele não vai ser enviado para o GitHub acidentalmente. Isso é intencional: cada pessoa que for rodar o projeto deve ter sua própria cópia local, com suas próprias credenciais.

## 4. Abrindo o projeto na IDE

### IntelliJ IDEA

1. Abra o IntelliJ e selecione **File > Open**, escolhendo a pasta do projeto clonado
2. O IntelliJ deve reconhecer automaticamente que é um projeto Maven e baixar as dependências (`mysql-connector-j`, `javafx-controls`, `javafx-fxml`) — isso pode levar alguns minutos na primeira vez
3. Se o IntelliJ não baixar automaticamente, clique com o botão direito no arquivo `pom.xml` e selecione **Maven > Reload Project**

### Configurando o JDK do projeto

Confirme que o projeto está usando o JDK 24:

1. **File > Project Structure > Project**
2. Em **SDK**, selecione o JDK 24 instalado (ou baixe um pelo próprio IntelliJ, clicando em "Add SDK")

## 5. Executando o projeto

1. Localize a classe principal: `src/main/java/br/edu/ufersa/view/MainApp.java`
2. Clique no ícone de play (▶) ao lado do método `main`, ou clique com o botão direito no arquivo e escolha **Run 'MainApp.main()'**
3. A tela de login deve abrir. Entre com usuário `admin` e senha `admin` (ou outro funcionário que você já tenha cadastrado)

## Estrutura do projeto

```
ProjetoAcaiteria/
├── pom.xml                      # Dependências e configuração do Maven
├── db.properties                # Configuração do banco (você precisa criar — não versionado)
├── script_banco_dados.sql       # Script para criar o banco e as tabelas
└── src/main/
    ├── java/br/edu/ufersa/
    │   ├── controller/          # Controllers do JavaFX (um por tela/modal)
    │   ├── facade/               # SistemaAcaiteria (Facade — ponto único de acesso ao sistema)
    │   ├── model/
    │   │   ├── DAO/              # Acesso ao banco de dados (um por entidade)
    │   │   ├── connectionFactory/# ConnectionFactory (Singleton da conexão com o banco)
    │   │   ├── entities/         # Classes de domínio (Produto, Cliente, Pedido, etc.)
    │   │   ├── exceptions/       # Exceptions customizadas de regra de negócio
    │   │   └── services/         # Regras de negócio e validações (um por entidade)
    │   └── view/
    │       └── MainApp.java      # Classe principal (ponto de entrada da aplicação)
    └── resources/
        └── fxml/                 # Telas e modais (.fxml) do JavaFX
```

## Problemas comuns

**Erro `FileNotFoundException: db.properties`**
O arquivo não está na raiz do projeto, ou não foi criado. Revise o passo 3.

**Erro de conexão com o banco (`Communications link failure` ou similar)**
Confirme que o serviço do MySQL está rodando na sua máquina, e que usuário/senha em `db.properties` estão corretos.

**Tela de login não avança / "usuário ou senha incorretos"**
Confirme que o script SQL foi executado por completo (incluindo o `INSERT` do usuário admin no final do arquivo).

**Erro relacionado a `module-info.java` ou versão do JavaFX**
Confirme que está usando o JDK 24 no projeto (Project Structure > Project > SDK) e que as dependências do Maven foram baixadas corretamente (Maven > Reload Project).

**Aviso sobre `module javafx.graphics` / "restricted method"**
Pode ignorar — é apenas um aviso do Java sobre acesso nativo, não impede a execução do programa.
