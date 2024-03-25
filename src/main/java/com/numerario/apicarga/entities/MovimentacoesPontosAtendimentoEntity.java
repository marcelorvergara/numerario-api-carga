package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PA", referencedColumnName = "IDUNIDADEINST"),
            @JoinColumn(name = "TERMINAL", referencedColumnName = "NUMTERMINAL"),
            @JoinColumn(name = "TIPOTERMINAL", referencedColumnName = "IDTIPOTERMINAL"),
            @JoinColumn(name = "usuario", referencedColumnName = "IDUSUARIO")
    })
    private TerminaisEntity terminaisEntity;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA", nullable = false)
    private LocalDate data;

    @Column(name = "VALOR", nullable = false)
    private BigDecimal valor;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
