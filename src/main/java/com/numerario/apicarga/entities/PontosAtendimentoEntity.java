package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "PontosAtendimento")
public class PontosAtendimentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NOMEUNIDADE", nullable = false)
    private String nomeUnidade;

    @Column(name = "IDUNIDADEINST", nullable = false, unique = true)
    private int idUnidadeInst;

    @ManyToOne
    @JoinColumn(name = "CODTIPOUNIDADE", referencedColumnName = "CODTIPOUNIDADE")
    private UnidadesNegocioEntity unidadesNegocioEntity;
}
