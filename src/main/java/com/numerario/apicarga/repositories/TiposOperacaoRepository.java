package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.TiposOperacaoEntity;
import com.numerario.apicarga.entities.composite_keys.TiposOperacaoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TiposOperacaoRepository extends JpaRepository<TiposOperacaoEntity, TiposOperacaoId> {
    TiposOperacaoEntity findByIdIdGrupoCaixaAndIdIdOperacaoCaixaAndIdHistorico(int idGrupoCaixa, int idOperacaoCaixa, int historico);
}
