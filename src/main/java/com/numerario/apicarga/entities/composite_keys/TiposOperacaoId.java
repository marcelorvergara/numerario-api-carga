package com.numerario.apicarga.entities.composite_keys;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TiposOperacaoId implements Serializable {
    private int idGrupoCaixa;
    private int idOperacaoCaixa;
    private int historico;
}
