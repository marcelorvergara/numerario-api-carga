package com.numerario.apicarga.entities;

import com.numerario.apicarga.entities.composite_keys.TiposOperacaoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TiposOperacao")
public class TiposOperacaoEntity {

    @EmbeddedId
    private TiposOperacaoId id;

    @Column(name = "Operacao", nullable = false)
    private String operacao;

    @Column(name = "DescricaoOperacao", nullable = false)
    private String descricaoOperacao;

    @Column(name = "DescricaoHistorico", nullable = false)
    private String descricaoHistorico;

    @Column(name = "Sensibilizacao", nullable = false)
    private String sensibilizacao;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
