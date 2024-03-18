package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.TipoTerminalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalsTypesRepository extends JpaRepository<TipoTerminalEntity, Integer> {
}
