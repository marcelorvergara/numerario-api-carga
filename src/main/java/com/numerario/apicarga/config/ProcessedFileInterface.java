package com.numerario.apicarga.config;

import com.numerario.apicarga.entities.FileStatus;

public interface ProcessedFileInterface {
    boolean processFileFreshness(String lancamentosFileName);
    void updateFileProcessStatus(String lancamentosFileName, FileStatus fileStatus);
}