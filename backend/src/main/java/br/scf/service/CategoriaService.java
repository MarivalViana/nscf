package br.scf.service;

import br.scf.dto.categoria.CategoriaRequest;
import br.scf.dto.categoria.CategoriaResponse;
import br.scf.entity.Categoria;
import br.scf.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findByAtivoTrueOrderByNomeAsc()
                .stream().map(CategoriaResponse::from).toList();
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        var categoria = Categoria.builder()
                .nome(request.nome())
                .icone(request.icone())
                .ativo(true)
                .build();
        return CategoriaResponse.from(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        var categoria = buscarEntidade(id);
        categoria.setNome(request.nome());
        categoria.setIcone(request.icone());
        return CategoriaResponse.from(categoriaRepository.save(categoria));
    }

    @Transactional
    public void desativar(Long id) {
        var categoria = buscarEntidade(id);
        categoria.setAtivo(false);
        categoriaRepository.save(categoria);
    }

    public Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + id));
    }
}
