package br.scf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receitas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receita extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TipoReceita tipo;

    @Column(nullable = false)
    private boolean ativo;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ano_inicio ASC, mes_inicio ASC")
    @Builder.Default
    private List<ReceitaValor> valores = new ArrayList<>();

    public enum TipoReceita {
        RECORRENTE, PONTUAL
    }
}
