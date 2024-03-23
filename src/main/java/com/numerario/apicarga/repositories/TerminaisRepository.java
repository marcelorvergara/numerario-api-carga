package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.TerminaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TerminaisRepository extends JpaRepository<TerminaisEntity, Integer> {
    TerminaisEntity findByNumTerminalAndPontosAtendimentoEntity_IdUnidadeInst(int terminal, int idUnidadeInst);
}
