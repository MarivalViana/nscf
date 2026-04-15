package br.scf.dto.despesa;

import br.scf.entity.Despesa.TipoDespesa;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DespesaRequest(

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Tipo é obrigatório")
        TipoDespesa tipo,

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor,

        /** Null para RECORRENTE. 1 para AVISTA. N para PARCELADA. */
        @Positive(message = "Quantidade de parcelas deve ser positiva")
        Integer qtdParcelas,

        @NotNull(message = "Mês de início é obrigatório")
        @Min(value = 1, message = "Mês inválido")
        @Max(value = 12, message = "Mês inválido")
        Integer mesInicio,

        @NotNull(message = "Ano de início é obrigatório")
        @Min(value = 2000, message = "Ano inválido")
        Integer anoInicio,

        Long categoriaId,

        Long contaId
) {}
