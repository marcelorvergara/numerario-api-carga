package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.TiposOperacaoEntity;
import com.numerario.apicarga.entities.composite_keys.TiposOperacaoId;
import com.numerario.apicarga.entities.enums.SensibilizacaoTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TiposOperacaoRepository extends JpaRepository<TiposOperacaoEntity, TiposOperacaoId> {
    TiposOperacaoEntity findByIdIdGrupoCaixaAndIdIdOperacaoCaixaAndIdHistorico(int idGrupoCaixa, int idOperacaoCaixa, int historico);
    List<TiposOperacaoEntity> findAllBySensibilizacaoNot(SensibilizacaoTypeEnum sensibilizacao);

}
