package br.scf.controller;

import br.scf.dto.receita.*;
import br.scf.service.ReceitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receitas")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService receitaService;

    @GetMapping
    public ResponseEntity<List<ReceitaResponse>> listar() {
        return ResponseEntity.ok(receitaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(receitaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReceitaResponse> criar(@Valid @RequestBody ReceitaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReceitaRequest request) {
        return ResponseEntity.ok(receitaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        receitaService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/valores")
    public ResponseEntity<ReceitaValorResponse> adicionarValor(
            @PathVariable Long id,
            @Valid @RequestBody ReceitaValorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(receitaService.adicionarValor(id, request));
    }

    @PutMapping("/valores/{valorId}")
    public ResponseEntity<ReceitaValorResponse> atualizarValor(
            @PathVariable Long valorId,
            @Valid @RequestBody ReceitaValorRequest request) {
        return ResponseEntity.ok(receitaService.atualizarValor(valorId, request));
    }

    @DeleteMapping("/valores/{valorId}")
    public ResponseEntity<Void> removerValor(@PathVariable Long valorId) {
        receitaService.removerValor(valorId);
        return ResponseEntity.noContent().build();
    }
}
