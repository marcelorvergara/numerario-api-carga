package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.PointsOfServiceRepository;
import com.numerario.apicarga.utils.POServiceExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsOfService {

    private static final String POINTSOFSERVICE = "constantes/UNIDADEINSTITUICAO.xlsx";

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    private POServiceExcelUtils poServiceExcelUtils;

    @Autowired
    private PointsOfServiceRepository pointsOfServiceRepository;

    public List<PontosAtendimentoEntity> executePointsOfService() {

        byte[] content = getBytes();

        int[] desiredUsersColumns = {1, 3, 7};
        List<PontosAtendimentoEntity> pointsOfServiceResult = poServiceExcelUtils.readExcelPointsOfServiceSheet(content, 0, desiredUsersColumns);

        return this.pointsOfServiceRepository.saveAll(pointsOfServiceResult);
    }

    private byte[] getBytes() {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new BucketNotFoundException("Bucket not found");
        }
        Blob blob = bucket.get(POINTSOFSERVICE);
        if (blob == null) {
            throw new BucketNotFoundException("File:" + POINTSOFSERVICE.split("/")[1] +  " not found");
        }

        byte[] content = blob.getContent();
        return content;
    }
}
