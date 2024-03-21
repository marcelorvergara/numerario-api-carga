package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PontosAtendimentoRepository extends JpaRepository<PontosAtendimentoEntity, Integer> {
    Optional<PontosAtendimentoEntity> findByIdUnidadeInst(int pa);
}
