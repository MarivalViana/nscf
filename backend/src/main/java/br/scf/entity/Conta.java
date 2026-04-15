package br.scf.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conta extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, length = 50)
    private String icone;

    @Column(nullable = false)
    private boolean ativo;
}
