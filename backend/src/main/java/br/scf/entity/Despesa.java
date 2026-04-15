package br.scf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "despesas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Despesa extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TipoDespesa tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    /** Null para RECORRENTE. 1 para AVISTA. N para PARCELADA. */
    @Column(name = "qtd_parcelas")
    private Integer qtdParcelas;

    @Column(name = "mes_inicio", nullable = false)
    private Integer mesInicio;

    @Column(name = "ano_inicio", nullable = false)
    private Integer anoInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Conta conta;

    @Column(nullable = false)
    private boolean ativo;

    public enum TipoDespesa {
        AVISTA, PARCELADA, RECORRENTE, ANUAL
    }
}
