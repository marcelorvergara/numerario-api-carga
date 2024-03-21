package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.TipoTerminalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TiposTerminalRepository extends JpaRepository<TipoTerminalEntity, Integer> {
    Optional<TipoTerminalEntity> findByCodigo(int finalTerminalTypeId);
}
