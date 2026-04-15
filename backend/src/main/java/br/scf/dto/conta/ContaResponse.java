package br.scf.dto.conta;

import br.scf.entity.Conta;

public record ContaResponse(
        Long id,
        String nome,
        String icone
) {
    public static ContaResponse from(Conta c) {
        return new ContaResponse(c.getId(), c.getNome(), c.getIcone());
    }
}
