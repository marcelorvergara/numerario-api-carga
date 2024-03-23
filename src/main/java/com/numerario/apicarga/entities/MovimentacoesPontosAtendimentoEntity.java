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

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "IDGRUPOCAIXA", referencedColumnName = "idGrupoCaixa"),
            @JoinColumn(name = "IDOPERACAOCAIXA", referencedColumnName = "idOperacaoCaixa"),
            @JoinColumn(name = "HISTORICO", referencedColumnName = "historico")
    })
    private TiposOperacaoEntity tiposOperacaoEntity;

    @Column(name = "DESCRICAOOPERACAO")
    private String descOperacao;

    @Column(name = "DESCRICAOHISTORICO")
    private String descHistorico;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA", nullable = false)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "IDTERMINAL", referencedColumnName = "ID")
    private TerminaisEntity terminaisEntity;

    @Column(name = "VALOR", nullable = false)
    private BigDecimal valor;
}
