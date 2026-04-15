CREATE TABLE categorias (
    id         BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome       VARCHAR(80)  NOT NULL,
    icone      VARCHAR(50)  NOT NULL,
    ativo      BIT          NOT NULL DEFAULT 1,
    created_at DATETIME2    NOT NULL,
    updated_at DATETIME2    NULL,
    created_by VARCHAR(150) NULL,
    updated_by VARCHAR(150) NULL
);

-- Seeds: categorias padrão
INSERT INTO categorias (nome, icone, ativo, created_at) VALUES
    ('Abastecimento',      'pi pi-car',          1, GETDATE()),
    ('Alimentação',        'pi pi-shopping-cart', 1, GETDATE()),
    ('Educação',           'pi pi-book',          1, GETDATE()),
    ('Investimento',       'pi pi-chart-line',    1, GETDATE()),
    ('Lazer',              'pi pi-star',          1, GETDATE()),
    ('Manutenção do Carro','pi pi-wrench',        1, GETDATE()),
    ('Moradia',            'pi pi-home',          1, GETDATE()),
    ('Saúde',              'pi pi-heart',         1, GETDATE()),
    ('Serviços',           'pi pi-cog',           1, GETDATE()),
    ('Transporte',         'pi pi-send',          1, GETDATE()),
    ('Vestuário',          'pi pi-tag',           1, GETDATE()),
    ('Outros',             'pi pi-ellipsis-h',    1, GETDATE());

-- FK em despesas
ALTER TABLE despesas ADD categoria_id BIGINT NULL
    CONSTRAINT fk_despesas_categorias FOREIGN KEY REFERENCES categorias(id);
