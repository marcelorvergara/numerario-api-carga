package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Integer> {
}
