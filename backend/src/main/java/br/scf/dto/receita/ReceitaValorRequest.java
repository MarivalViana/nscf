package br.scf.dto.receita;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ReceitaValorRequest(

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "Mês de início é obrigatório")
        @Min(value = 1, message = "Mês inválido") @Max(value = 12, message = "Mês inválido")
        Integer mesInicio,

        @NotNull(message = "Ano de início é obrigatório")
        @Min(value = 2000, message = "Ano inválido")
        Integer anoInicio,

        @Min(value = 1, message = "Mês inválido") @Max(value = 12, message = "Mês inválido")
        Integer mesFim,

        @Min(value = 2000, message = "Ano inválido")
        Integer anoFim
) {}
