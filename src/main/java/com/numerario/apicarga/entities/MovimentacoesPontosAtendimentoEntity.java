package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "MovimentacoesPontosAtendimento")
public class MovimentacoesPontosAtendimentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA", nullable = false)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "AG_PA_TERM", referencedColumnName = "NUMTERMINAL")
    private TerminaisEntity terminaisEntity;

    @ManyToOne
    @JoinColumn(name = "IDTIPOOPERACAO", referencedColumnName = "ID")
    private TiposOperacaoEntity tiposOperacaoEntity;

    @Column(name = "HISTORICO",nullable = false)
    private int historico;

    @Column(name = "VALOR", nullable = false)
    private BigDecimal valor;
}
