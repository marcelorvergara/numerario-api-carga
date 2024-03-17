package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.TerminalsRepository;
import com.numerario.apicarga.utils.TerminalsExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class TerminalsService {

    private static final String TERMINALS = "constantes/TERMINAL.xlsx";

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    private TerminalsRepository terminalsRepository;

    public void executeTerminals() {
        TerminalsExcelUtils terminalsExcelUtils = new TerminalsExcelUtils();

        byte[] content = getBytes();

        int[] desiredTerminalsColumns = {2, 4, 5, 7};
        List<TerminaisEntity> terminalsExcelData = terminalsExcelUtils.readExcelTerminalsSheet(content, 0, desiredTerminalsColumns);

        for(var term: terminalsExcelData) {
            System.out.println(term);
        }
    }

    private byte[] getBytes() {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new BucketNotFoundException("Bucket not found");
        }
        Blob blob = bucket.get(TERMINALS);
        if (blob == null) {
            throw new BucketNotFoundException("File:" + TERMINALS +  " not found");
        }

        byte[] content = blob.getContent();
        return content;
    }
}
