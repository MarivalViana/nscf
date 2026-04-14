package br.scf.service;

import br.scf.config.JwtService;
import br.scf.dto.auth.LoginRequest;
import br.scf.dto.auth.LoginResponse;
import br.scf.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        var usuario = (Usuario) auth.getPrincipal();
        String token = jwtService.gerarToken(usuario, Map.of("nome", usuario.getNome()));

        return new LoginResponse(token, usuario.getNome(), usuario.getEmail());
    }
}
