package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
