CREATE TABLE contas (
    id         BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome       VARCHAR(80)  NOT NULL,
    icone      VARCHAR(50)  NOT NULL,
    ativo      BIT          NOT NULL DEFAULT 1,
    created_at DATETIME2    NOT NULL,
    updated_at DATETIME2    NULL,
    created_by VARCHAR(150) NULL,
    updated_by VARCHAR(150) NULL
);

-- Seeds: contas padrão
INSERT INTO contas (nome, icone, ativo, created_at) VALUES
    ('Cartão Nubank',   'pi pi-credit-card', 1, GETDATE()),
    ('Cartão BB',       'pi pi-credit-card', 1, GETDATE()),
    ('Conta Corrente',  'pi pi-building',    1, GETDATE()),
    ('Conta Poupança',  'pi pi-piggy-bank',  1, GETDATE()),
    ('Dinheiro',        'pi pi-wallet',      1, GETDATE()),
    ('Empréstimo',      'pi pi-dollar',      1, GETDATE()),
    ('Financiamento',   'pi pi-home',        1, GETDATE());

-- FK em despesas
ALTER TABLE despesas ADD conta_id BIGINT NULL
    CONSTRAINT fk_despesas_contas FOREIGN KEY REFERENCES contas(id);
