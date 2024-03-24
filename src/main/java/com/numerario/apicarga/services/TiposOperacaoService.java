package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.TiposOperacaoEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.TiposOperacaoRepository;
import com.numerario.apicarga.utils.TiposOperacaoExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TiposOperacaoService {

    private static final String TIPOS_OPERACAO = "automatizados/Sensibilização de Saldos.xlsx";

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    TiposOperacaoExcelUtils tiposOperacaoExcelUtils;

    @Autowired
    TiposOperacaoRepository tiposOperacaoRepository;


    public List<TiposOperacaoEntity> executeTiposOperacaoService() {
        byte[] content = getBytes();

        int[] desiredTerminalsColumns = {0,1,2,3,4,5,6};
        List<TiposOperacaoEntity> tiposOperacaoServiceResult = tiposOperacaoExcelUtils.readExcelTiposOperacaoSheet(content, 0, desiredTerminalsColumns);

        this.tiposOperacaoRepository.deleteAll();
        return this.tiposOperacaoRepository.saveAll(tiposOperacaoServiceResult);
    }

    private byte[] getBytes() {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new BucketNotFoundException("Bucket not found");
        }
        Blob blob = bucket.get(TIPOS_OPERACAO);
        if (blob == null) {
            throw new BucketNotFoundException("File:" + TIPOS_OPERACAO + " not found");
        }

        byte[] content = blob.getContent();
        return content;
    }
}
