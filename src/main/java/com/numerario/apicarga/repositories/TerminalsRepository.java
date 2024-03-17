package com.numerario.apicarga.repositories;

import com.numerario.apicarga.entities.TerminaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TerminalsRepository extends JpaRepository<TerminaisEntity, Integer> {
}
