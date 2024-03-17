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

    @Column(name = "IDGRUPODOCAIXA", nullable = false)
    private int idGrupoDoCaixa;

    @Column(name = "IDOPERACAOCAIXA", nullable = false)
    private int idOperacaoCaixa;

    @Column(name = "HISTORICO",nullable = false)
    private int historico;

    @Column(name = "VALOR", nullable = false)
    private BigDecimal valor;
}
