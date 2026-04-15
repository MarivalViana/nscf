package br.scf.service;

import br.scf.dto.conta.ContaRequest;
import br.scf.dto.conta.ContaResponse;
import br.scf.entity.Conta;
import br.scf.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContaService {

    private final ContaRepository contaRepository;

    public List<ContaResponse> listar() {
        return contaRepository.findByAtivoTrueOrderByNomeAsc()
                .stream().map(ContaResponse::from).toList();
    }

    @Transactional
    public ContaResponse criar(ContaRequest request) {
        var conta = Conta.builder()
                .nome(request.nome())
                .icone(request.icone())
                .ativo(true)
                .build();
        return ContaResponse.from(contaRepository.save(conta));
    }

    @Transactional
    public ContaResponse atualizar(Long id, ContaRequest request) {
        var conta = buscarEntidade(id);
        conta.setNome(request.nome());
        conta.setIcone(request.icone());
        return ContaResponse.from(contaRepository.save(conta));
    }

    @Transactional
    public void desativar(Long id) {
        var conta = buscarEntidade(id);
        conta.setAtivo(false);
        contaRepository.save(conta);
    }

    public Conta buscarEntidade(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada: " + id));
    }
}
