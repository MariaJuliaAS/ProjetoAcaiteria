-- ============================================================
-- Script de criação do banco de dados — Sistema Açaiteria
-- ============================================================
-- Execute este script inteiro no MySQL Workbench (ou outro
-- cliente MySQL de sua preferência) para criar o banco e todas
-- as tabelas necessárias para rodar o projeto.
-- ============================================================

CREATE DATABASE IF NOT EXISTS acaiteria
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE acaiteria;

-- ------------------------------------------------------------
-- Tabela: adicional
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `adicional`;
CREATE TABLE `adicional` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `preco` decimal(10,2) NOT NULL,
  `qtd_estoque` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ------------------------------------------------------------
-- Tabela: cliente
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `endereco` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ------------------------------------------------------------
-- Tabela: funcionario
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `funcionario`;
CREATE TABLE `funcionario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `login` varchar(50) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `tipo` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ------------------------------------------------------------
-- Tabela: produto
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `produto`;
CREATE TABLE `produto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `preco` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ------------------------------------------------------------
-- Tabela: pedido
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `pedido`;
CREATE TABLE `pedido` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `forma_pagamento` varchar(50) DEFAULT NULL,
  `cliente_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pedido_cliente` (`cliente_id`),
  CONSTRAINT `fk_pedido_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ------------------------------------------------------------
-- Tabela: item_pedido
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `item_pedido`;
CREATE TABLE `item_pedido` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pedido_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `quantidade` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_item_produto` (`produto_id`),
  KEY `fk_item_pedido` (`pedido_id`),
  CONSTRAINT `fk_item_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_item_produto` FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ------------------------------------------------------------
-- Tabela: item_pedido_adicional (N:N entre item_pedido e adicional)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `item_pedido_adicional`;
CREATE TABLE `item_pedido_adicional` (
  `item_pedido_id` int NOT NULL,
  `adicional_id` int NOT NULL,
  PRIMARY KEY (`item_pedido_id`,`adicional_id`),
  KEY `fk_item_adicional_adicional` (`adicional_id`),
  CONSTRAINT `fk_item_adicional_adicional` FOREIGN KEY (`adicional_id`) REFERENCES `adicional` (`id`),
  CONSTRAINT `fk_item_adicional_item` FOREIGN KEY (`item_pedido_id`) REFERENCES `item_pedido` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ------------------------------------------------------------
-- Tabela: produto_adicional (N:N entre produto e adicional)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `produto_adicional`;
CREATE TABLE `produto_adicional` (
  `produto_id` int NOT NULL,
  `adicional_id` int NOT NULL,
  PRIMARY KEY (`produto_id`,`adicional_id`),
  KEY `fk_produto_adicional_adicional` (`adicional_id`),
  CONSTRAINT `fk_produto_adicional_adicional` FOREIGN KEY (`adicional_id`) REFERENCES `adicional` (`id`),
  CONSTRAINT `fk_produto_adicional_produto` FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================================
-- Usuário administrador inicial
-- ============================================================
-- Sem este registro, ninguém consegue logar no sistema na
-- primeira execução (o login é validado contra esta tabela).
-- Login: admin   |   Senha: admin
-- ============================================================
INSERT INTO funcionario (nome, login, senha, tipo)
VALUES ('Administrador', 'admin', 'admin', 'Admin');
