package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "pontosAtendimento")
    private List<TipoTerminalEntity> tiposTerminal;
}
