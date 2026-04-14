CREATE TABLE receita (
    id        BIGINT IDENTITY(1,1) PRIMARY KEY,
    descricao VARCHAR(150) NOT NULL,
    tipo      VARCHAR(20)  NOT NULL,  -- RECORRENTE | PONTUAL
    ativo     BIT          NOT NULL DEFAULT 1
);

CREATE TABLE receita_valor (
    id          BIGINT         IDENTITY(1,1) PRIMARY KEY,
    receita_id  BIGINT         NOT NULL,
    valor       DECIMAL(15,2)  NOT NULL,
    mes_inicio  INT            NOT NULL,
    ano_inicio  INT            NOT NULL,
    mes_fim     INT            NULL,   -- null = ainda vigente (RECORRENTE)
    ano_fim     INT            NULL,   -- null = ainda vigente (RECORRENTE)

    CONSTRAINT fk_receita_valor_receita
        FOREIGN KEY (receita_id) REFERENCES receita(id)
);
