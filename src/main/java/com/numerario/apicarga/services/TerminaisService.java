package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.TerminaisRepository;
import com.numerario.apicarga.utils.TerminaisExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminaisService {

    private static final String TERMINALS = "constantes/TERMINAL.xlsx";

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    private TerminaisRepository terminaisRepository;

    @Autowired
    TerminaisExcelUtils terminaisExcelUtils;

    public List<TerminaisEntity> executeTerminals() {

        byte[] content = getBytes();

        int[] desiredTerminalsColumns = {2, 4, 5, 7, 26, 27};
        List<TerminaisEntity> terminalsExcelData = terminaisExcelUtils.readExcelTerminalsSheet(content, 0, desiredTerminalsColumns);

        return this.terminaisRepository.saveAll(terminalsExcelData);
    }

    private byte[] getBytes() {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new BucketNotFoundException("Bucket not found");
        }
        Blob blob = bucket.get(TERMINALS);
        if (blob == null) {
            throw new BucketNotFoundException("File:" + TERMINALS + " not found");
        }

        byte[] content = blob.getContent();
        return content;
    }
}
