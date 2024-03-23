package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
@Entity
@Table(name = "Terminais")
public class TerminaisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "IDUNIDADEINST", referencedColumnName = "IDUNIDADEINST")
    private PontosAtendimentoEntity pontosAtendimentoEntity;

    @Column(name = "NUMTERMINAL", nullable = false)
    private int numTerminal;

    @ManyToOne
    @JoinColumn(name = "IDTIPOTERMINAL", referencedColumnName = "CODIGO")
    private TipoTerminalEntity tipoTerminalEntity;

    @ManyToOne
    @JoinColumn(name = "IDUSUARIO", referencedColumnName = "IDUSUARIO")
    private UsuariosEntity usuariosEntity;

    @Column(name = "VALORLIMITESAQUE")
    private BigDecimal valorLimiteSaque;

    @Column(name = "VALORLIMITETERMINAL")
    private BigDecimal valorLimiteTerminal;

}
// IDUNIDADEINST -  NUMTERMINAL - IDTIPOTERMINAL - IDUSUARIO
