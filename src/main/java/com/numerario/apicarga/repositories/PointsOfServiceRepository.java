package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointsOfServiceRepository extends JpaRepository<PontosAtendimentoEntity, Integer> {
}
