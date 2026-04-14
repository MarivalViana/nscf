EXEC sp_rename 'usuario',      'usuarios';
EXEC sp_rename 'receita',      'receitas';
EXEC sp_rename 'receita_valor', 'receita_valores';

-- Renomeia também a FK para manter consistência
EXEC sp_rename 'receita_valores.fk_receita_valor_receita',
               'fk_receita_valores_receitas', 'OBJECT';
