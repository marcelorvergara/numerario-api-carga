package com.numerario.apicarga.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "TiposOperacao")
public class TiposOperacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "IDGRUPOCAIXA",nullable = false)
    private int idGrupoCaixa;

    @Column(name = "IDOPERACAOCAIXA", nullable = false)
    private int idOperacaoCaixa;

    @Column(name = "Operacao", nullable = false)
    private String operacao;

    @Column(name ="Descricao", nullable = false)
    private String descricao;

    @Column(name = "Historico", nullable = false)
    private int historico;

    @Column(name = "Sensibilizacao", nullable = false)
    private String sensibilizacao;
}
