package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TiposTerminal")
public class TipoTerminalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "IDUNIDADEINST", nullable = false) // PA
    private PontosAtendimentoEntity pontosAtendimento;

    @Column(name = "CODIGO", nullable = false, unique = true)
    private int codigo;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    @Column(name = "LIM_SUPERIOR")
    private BigDecimal limSuperior;

    @Column(name = "LIM_INFERIOR")
    private BigDecimal limInferior;
}