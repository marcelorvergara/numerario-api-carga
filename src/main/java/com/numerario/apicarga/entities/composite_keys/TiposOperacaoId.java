package com.numerario.apicarga.entities.composite_keys;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TiposOperacaoId implements Serializable {
    private int idGrupoCaixa;
    private int idOperacaoCaixa;
    private int historico;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiposOperacaoId that = (TiposOperacaoId) o;
        return idGrupoCaixa == that.idGrupoCaixa &&
                idOperacaoCaixa == that.idOperacaoCaixa &&
                historico == that.historico;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGrupoCaixa, idOperacaoCaixa, historico);
    }

    @Override
    public String toString() {
        return "TiposOperacaoId{" +
                "idGrupoCaixa=" + idGrupoCaixa +
                ", idOperacaoCaixa=" + idOperacaoCaixa +
                ", historico=" + historico +
                '}';
    }
}
