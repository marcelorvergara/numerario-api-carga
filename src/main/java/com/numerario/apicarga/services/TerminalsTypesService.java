package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.TipoTerminalEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.TerminalsTypesRepository;
import com.numerario.apicarga.utils.TerminalsTypesExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalsTypesService {
    private static final String TERMINALSTYPES = "constantes/TIPOS_TERMINAL.xlsx";

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    private TerminalsTypesExcelUtils terminalsTypesExcelUtils;

    @Autowired
    private TerminalsTypesRepository terminalsTypesRepository;

    public List<TipoTerminalEntity> executeTypeOfTerminals() {

        byte[] content = getBytes();

        int[] desiredUsersColumns = {0,1,2,3,4};
        List<TipoTerminalEntity> terminalsTypesServiceResult = terminalsTypesExcelUtils.readExcelTerminalTypesSheet(content, 0, desiredUsersColumns);

        return this.terminalsTypesRepository.saveAll(terminalsTypesServiceResult);
    }

    private byte[] getBytes() {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new BucketNotFoundException("Bucket not found");
        }
        Blob blob = bucket.get(TERMINALSTYPES);
        if (blob == null) {
            throw new BucketNotFoundException("File:" + TERMINALSTYPES.split("/")[1] + " not found");
        }

        byte[] content = blob.getContent();
        return content;
    }
}
