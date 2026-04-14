package br.scf.dto.receita;

import br.scf.entity.Receita.TipoReceita;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceitaRequest(

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Tipo é obrigatório")
        TipoReceita tipo
) {}
