package br.scf.dto.auth;

public record LoginResponse(
        String token,
        String nome,
        String email
) {}
