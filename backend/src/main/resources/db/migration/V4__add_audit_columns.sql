-- Auditoria na tabela usuario
ALTER TABLE usuario
    ADD created_at  DATETIME2    NOT NULL DEFAULT GETDATE(),
        updated_at  DATETIME2    NULL,
        created_by  VARCHAR(150) NULL,
        updated_by  VARCHAR(150) NULL;

-- Auditoria na tabela receita
ALTER TABLE receita
    ADD created_at  DATETIME2    NOT NULL DEFAULT GETDATE(),
        updated_at  DATETIME2    NULL,
        created_by  VARCHAR(150) NULL,
        updated_by  VARCHAR(150) NULL;

-- Auditoria na tabela receita_valor
ALTER TABLE receita_valor
    ADD created_at  DATETIME2    NOT NULL DEFAULT GETDATE(),
        updated_at  DATETIME2    NULL,
        created_by  VARCHAR(150) NULL,
        updated_by  VARCHAR(150) NULL;
