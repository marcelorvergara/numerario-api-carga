package com.numerario.apicarga.config;

import com.numerario.apicarga.entities.enums.FileStatusEnum;

public interface ProcessedFileInterface {
    boolean processFileFreshness(String lancamentosFileName);
    void updateFileProcessStatus(String lancamentosFileName, FileStatusEnum fileStatusEnum);
}