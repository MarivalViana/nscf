package br.scf.dto.categoria;

import br.scf.entity.Categoria;

public record CategoriaResponse(
        Long id,
        String nome,
        String icone
) {
    public static CategoriaResponse from(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNome(), c.getIcone());
    }
}
