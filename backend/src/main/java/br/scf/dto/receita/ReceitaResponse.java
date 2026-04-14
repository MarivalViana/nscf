package br.scf.dto.receita;

import br.scf.entity.Receita;
import br.scf.entity.Receita.TipoReceita;

import java.util.List;

public record ReceitaResponse(
        Long id,
        String descricao,
        TipoReceita tipo,
        boolean ativo,
        List<ReceitaValorResponse> valores
) {
    public static ReceitaResponse from(Receita r) {
        return new ReceitaResponse(
                r.getId(),
                r.getDescricao(),
                r.getTipo(),
                r.isAtivo(),
                r.getValores().stream().map(ReceitaValorResponse::from).toList()
        );
    }
}
