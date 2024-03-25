package com.numerario.apicarga.repositories;


import com.numerario.apicarga.entities.ProcessedFilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedFilesRepository extends JpaRepository<ProcessedFilesEntity, Integer> {
    ProcessedFilesEntity findByFileName(String lancamentosFileName);
}
