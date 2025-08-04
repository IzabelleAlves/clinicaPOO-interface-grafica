# Clínica

## 📌 Sobre o Projeto
Este projeto é um sistema de linha de comando (Java Console) com persistência em banco de dados MySQL. Ele permite a gestão de pacientes e exames médicos, com funcionalidades de cadastro, listagem, edição e remoção (CRUD completo). O projeto segue o padrão DAO (Data Access Object) para separação da lógica de persistência.

## 🎯 Objetivo
O sistema foi desenvolvido como um Toy Project educacional para aplicação prática dos conceitos de Programação Orientada a Objetos (POO), banco de dados relacionais e boas práticas de desenvolvimento Java. Ele simula uma clínica médica simples, onde é possível gerenciar pacientes e os exames associados.

## 🧩 Funcionalidades
✅ Cadastrar, listar, editar e remover pacientes.
✅ Cadastrar, listar, editar e remover exames.
✅ Visualizar exames associados a um paciente.
✅ Persistência completa em banco de dados relacional.
✅ Leitura automática da configuração do banco via config.txt.

## Requisitos Funcionais (Principais)
### Módulo Pacientes
- Cadastro de nome e CPF
- Listagem em tabela
- Edição e exclusão de dados
- Associação de exames

### Módulo Exames
- Cadastro com descrição, data e vínculo com paciente
- Listagem, edição e exclusão

### Módulo Sistema
- Inicialização automática do banco, caso necessário
- Leitura dos parâmetros de conexão (DBNAME, DBUSER, DBPASSWORD, etc.) a partir de arquivo externo de configuração.

## Requisitos Não Funcionais
- Interface via Java Console
- Banco de dados MySQL
- Empacotamento em .jar para portabilidade

#  Script SQL de Criação do Banco de Dados:

```bash
CREATE DATABASE clinica;
USE clinica;

CREATE TABLE pacientes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255) NOT NULL,
  cpf VARCHAR(14) NOT NULL UNIQUE
);

CREATE TABLE exames (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  descricao VARCHAR(255) NOT NULL,
  data_exame DATETIME NOT NULL,
  paciente_id BIGINT,
  FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE
);
```

## Tabelas do Banco de Dados `clinica`

### Tabela: `pacientes`

| Coluna | Tipo de Dado     | Restrições                    |
|--------|------------------|---------------------------------|
| id     | BIGINT           | PRIMARY KEY, AUTO_INCREMENT    |
| nome   | VARCHAR(255)     | NOT NULL                       |
| cpf    | VARCHAR(14)      | NOT NULL, UNIQUE               |

---

### Tabela: `exames`

| Coluna      | Tipo de Dado     | Restrições                                             |
|------------|------------------|--------------------------------------------------------|
| id         | BIGINT           | PRIMARY KEY, AUTO_INCREMENT                            |
| descricao  | VARCHAR(255)     | NOT NULL                                               |
| data_exame | DATETIME         | NOT NULL                                               |
| paciente_id| BIGINT           | FOREIGN KEY → `pacientes(id)` ON DELETE CASCADE        |

## Gerar e Executar o JAR

### 1️⃣ Gerar o arquivo `.jar`:

```bash
jar cfm clinica.jar manifest.txt -C bin .
```

### 2️⃣ Executar o JAR:

```bash
java -jar clinica.jar
```
