package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
// IDUNIDADEINST -  NUMTERMINAL - IDTIPOTERMINAL - IDUSUARIO
