package br.scf.dto.despesa;

import br.scf.dto.categoria.CategoriaResponse;
import br.scf.dto.conta.ContaResponse;
import br.scf.entity.Despesa;
import br.scf.entity.Despesa.TipoDespesa;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record DespesaResponse(
        Long id,
        String descricao,
        TipoDespesa tipo,
        BigDecimal valor,
        Integer qtdParcelas,
        Integer mesInicio,
        Integer anoInicio,
        boolean ativo,
        BigDecimal valorParcela,
        CategoriaResponse categoria,
        ContaResponse conta
) {
    public static DespesaResponse from(Despesa d) {
        return new DespesaResponse(
                d.getId(),
                d.getDescricao(),
                d.getTipo(),
                d.getValor(),
                d.getQtdParcelas(),
                d.getMesInicio(),
                d.getAnoInicio(),
                d.isAtivo(),
                calcularValorParcela(d),
                d.getCategoria() != null ? CategoriaResponse.from(d.getCategoria()) : null,
                d.getConta() != null ? ContaResponse.from(d.getConta()) : null
        );
    }

    private static BigDecimal calcularValorParcela(Despesa d) {
        if (d.getTipo() == TipoDespesa.PARCELADA && d.getQtdParcelas() != null && d.getQtdParcelas() > 0) {
            return d.getValor().divide(BigDecimal.valueOf(d.getQtdParcelas()), 2, RoundingMode.HALF_UP);
        }
        return d.getValor();
    }
}
