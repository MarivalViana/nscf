-- Renomeia tabelas para padrão plural (idempotente via IF EXISTS)
IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'usuario' AND schema_id = SCHEMA_ID('dbo'))
    EXEC sp_rename 'usuario', 'usuarios';

IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'receita' AND schema_id = SCHEMA_ID('dbo'))
    EXEC sp_rename 'receita', 'receitas';

IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'receita_valor' AND schema_id = SCHEMA_ID('dbo'))
    EXEC sp_rename 'receita_valor', 'receita_valores';
