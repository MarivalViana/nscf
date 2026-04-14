package br.scf.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "receita_valor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaValor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receita_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Receita receita;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "mes_inicio", nullable = false)
    private Integer mesInicio;

    @Column(name = "ano_inicio", nullable = false)
    private Integer anoInicio;

    @Column(name = "mes_fim")
    private Integer mesFim;

    @Column(name = "ano_fim")
    private Integer anoFim;
}
