package br.scf.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Ícone é obrigatório")
        String icone
) {}
