package br.scf.service;

import br.scf.dto.despesa.DespesaRequest;
import br.scf.dto.despesa.DespesaResponse;
import br.scf.entity.Categoria;
import br.scf.entity.Conta;
import br.scf.entity.Despesa;
import br.scf.repository.DespesaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final CategoriaService categoriaService;
    private final ContaService contaService;

    public List<DespesaResponse> listar() {
        return despesaRepository.findByAtivoTrueOrderByDescricaoAsc()
                .stream().map(DespesaResponse::from).toList();
    }

    public DespesaResponse buscarPorId(Long id) {
        return DespesaResponse.from(buscarEntidade(id));
    }

    @Transactional
    public DespesaResponse criar(DespesaRequest request) {
        var despesa = Despesa.builder()
                .descricao(request.descricao())
                .tipo(request.tipo())
                .valor(request.valor())
                .qtdParcelas(resolverQtdParcelas(request))
                .mesInicio(request.mesInicio())
                .anoInicio(request.anoInicio())
                .categoria(resolverCategoria(request.categoriaId()))
                .conta(resolverConta(request.contaId()))
                .ativo(true)
                .build();
        return DespesaResponse.from(despesaRepository.save(despesa));
    }

    @Transactional
    public DespesaResponse atualizar(Long id, DespesaRequest request) {
        var despesa = buscarEntidade(id);
        despesa.setDescricao(request.descricao());
        despesa.setTipo(request.tipo());
        despesa.setValor(request.valor());
        despesa.setQtdParcelas(resolverQtdParcelas(request));
        despesa.setMesInicio(request.mesInicio());
        despesa.setAnoInicio(request.anoInicio());
        despesa.setCategoria(resolverCategoria(request.categoriaId()));
        despesa.setConta(resolverConta(request.contaId()));
        return DespesaResponse.from(despesaRepository.save(despesa));
    }

    @Transactional
    public void desativar(Long id) {
        var despesa = buscarEntidade(id);
        despesa.setAtivo(false);
        despesaRepository.save(despesa);
    }

    private Integer resolverQtdParcelas(DespesaRequest request) {
        return switch (request.tipo()) {
            case AVISTA, ANUAL -> 1;
            case PARCELADA -> request.qtdParcelas();
            case RECORRENTE -> null;
        };
    }

    private Categoria resolverCategoria(Long categoriaId) {
        if (categoriaId == null) return null;
        return categoriaService.buscarEntidade(categoriaId);
    }

    private Conta resolverConta(Long contaId) {
        if (contaId == null) return null;
        return contaService.buscarEntidade(contaId);
    }

    private Despesa buscarEntidade(Long id) {
        return despesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada: " + id));
    }
}
