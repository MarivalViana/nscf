CREATE TABLE usuario (
    id    BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome  VARCHAR(100)  NOT NULL,
    email VARCHAR(150)  NOT NULL UNIQUE,
    senha VARCHAR(255)  NOT NULL,
    ativo BIT           NOT NULL DEFAULT 1
);

-- Admin padrão: senha = admin@2025
INSERT INTO usuario (nome, email, senha, ativo)
VALUES ('Administrador', 'admin@scf.com',
        '$2b$10$/ww94KZjDqAlJIG6Si7HHeVUk3OI2naxbzPdJp8j15SiTdTQUGLgG', 1);
