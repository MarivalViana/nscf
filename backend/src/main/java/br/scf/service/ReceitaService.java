package br.scf.service;

import br.scf.dto.receita.*;
import br.scf.entity.Receita;
import br.scf.entity.ReceitaValor;
import br.scf.repository.ReceitaRepository;
import br.scf.repository.ReceitaValorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ReceitaValorRepository receitaValorRepository;

    public List<ReceitaResponse> listar() {
        return receitaRepository.findByAtivoTrueOrderByDescricaoAsc()
                .stream().map(ReceitaResponse::from).toList();
    }

    public ReceitaResponse buscarPorId(Long id) {
        return ReceitaResponse.from(buscarEntidade(id));
    }

    @Transactional
    public ReceitaResponse criar(ReceitaRequest request) {
        var receita = Receita.builder()
                .descricao(request.descricao())
                .tipo(request.tipo())
                .ativo(true)
                .build();
        return ReceitaResponse.from(receitaRepository.save(receita));
    }

    @Transactional
    public ReceitaResponse atualizar(Long id, ReceitaRequest request) {
        var receita = buscarEntidade(id);
        receita.setDescricao(request.descricao());
        receita.setTipo(request.tipo());
        return ReceitaResponse.from(receitaRepository.save(receita));
    }

    @Transactional
    public void desativar(Long id) {
        var receita = buscarEntidade(id);
        receita.setAtivo(false);
        receitaRepository.save(receita);
    }

    @Transactional
    public ReceitaValorResponse adicionarValor(Long receitaId, ReceitaValorRequest request) {
        var receita = buscarEntidade(receitaId);
        var valor = ReceitaValor.builder()
                .receita(receita)
                .valor(request.valor())
                .mesInicio(request.mesInicio())
                .anoInicio(request.anoInicio())
                .mesFim(request.mesFim())
                .anoFim(request.anoFim())
                .build();
        return ReceitaValorResponse.from(receitaValorRepository.save(valor));
    }

    @Transactional
    public ReceitaValorResponse atualizarValor(Long valorId, ReceitaValorRequest request) {
        var valor = receitaValorRepository.findById(valorId)
                .orElseThrow(() -> new EntityNotFoundException("Valor não encontrado: " + valorId));
        valor.setValor(request.valor());
        valor.setMesInicio(request.mesInicio());
        valor.setAnoInicio(request.anoInicio());
        valor.setMesFim(request.mesFim());
        valor.setAnoFim(request.anoFim());
        return ReceitaValorResponse.from(receitaValorRepository.save(valor));
    }

    @Transactional
    public void removerValor(Long valorId) {
        receitaValorRepository.deleteById(valorId);
    }

    private Receita buscarEntidade(Long id) {
        return receitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada: " + id));
    }
}
