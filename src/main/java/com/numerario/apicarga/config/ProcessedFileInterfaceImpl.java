package com.numerario.apicarga.config;

import com.numerario.apicarga.entities.FileStatus;
import com.numerario.apicarga.entities.ProcessedFilesEntity;
import com.numerario.apicarga.repositories.ProcessedFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessedFileInterfaceImpl implements ProcessedFileInterface {
    @Autowired
    private ProcessedFilesRepository processedFilesRepository;

    @Override
    @Transactional
    public void updateFileProcessStatus(String lancamentosFileName, FileStatus fileStatus) {
        var fileInDB = this.processedFilesRepository.findByFileName(lancamentosFileName);
        fileInDB.setFileStatus(fileStatus);
        this.processedFilesRepository.save(fileInDB);
    }

    @Override
    @Transactional
    public boolean processFileFreshness(String lancamentosFileName) {
        ProcessedFilesEntity processedFilesEntity = this.processedFilesRepository.findByFileName(lancamentosFileName);
        if (processedFilesEntity == null) {
            var fileToInsertStatus = ProcessedFilesEntity.builder().fileName(lancamentosFileName).fileStatus(FileStatus.EM_PROGRESSO).build();
            this.processedFilesRepository.save(fileToInsertStatus);
            return true;
        } else {
            return false;
        }
    }
}
