package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.UnidadesNegocioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnidadesNegocioRepository extends JpaRepository<UnidadesNegocioEntity, Integer> {
    Optional<Object> findByCodTipoUnidade(int codTipoUnidade);
}
