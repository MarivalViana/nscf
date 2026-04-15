CREATE TABLE despesas (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    descricao    VARCHAR(150)   NOT NULL,
    tipo         VARCHAR(20)    NOT NULL,
    valor        DECIMAL(15,2)  NOT NULL,
    qtd_parcelas INT            NULL,
    mes_inicio   INT            NOT NULL,
    ano_inicio   INT            NOT NULL,
    ativo        BIT            NOT NULL DEFAULT 1,
    created_at   DATETIME2      NOT NULL,
    updated_at   DATETIME2      NULL,
    created_by   VARCHAR(150)   NULL,
    updated_by   VARCHAR(150)   NULL
);
