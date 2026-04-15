package br.scf.dto.conta;

import jakarta.validation.constraints.NotBlank;

public record ContaRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Ícone é obrigatório")
        String icone
) {}
