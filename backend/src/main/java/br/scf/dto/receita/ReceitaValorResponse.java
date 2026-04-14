package br.scf.dto.receita;

import br.scf.entity.ReceitaValor;

import java.math.BigDecimal;

public record ReceitaValorResponse(
        Long id,
        BigDecimal valor,
        Integer mesInicio,
        Integer anoInicio,
        Integer mesFim,
        Integer anoFim,
        boolean vigente
) {
    public static ReceitaValorResponse from(ReceitaValor rv) {
        return new ReceitaValorResponse(
                rv.getId(),
                rv.getValor(),
                rv.getMesInicio(),
                rv.getAnoInicio(),
                rv.getMesFim(),
                rv.getAnoFim(),
                rv.getMesFim() == null && rv.getAnoFim() == null
        );
    }
}
