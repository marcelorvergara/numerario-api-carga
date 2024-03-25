package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TiposTerminal")
public class TipoTerminalEntity {

    @Id
    @Column(name = "CODIGO", nullable = false, unique = true)
    private int codigo;

    @ManyToOne
    @JoinColumn(name = "IDUNIDADEINST", nullable = false) // PA
    private PontosAtendimentoEntity pontosAtendimento;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    @Column(name = "LIM_SUPERIOR")
    private BigDecimal limSuperior;

    @Column(name = "LIM_INFERIOR")
    private BigDecimal limInferior;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}