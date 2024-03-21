package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.UsuariosEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.UsuariosRepository;
import com.numerario.apicarga.utils.UsuariosExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosService {

    private static final String USERS = "constantes/Base de Usuários e Terminais Atuailzada.xlsx";
    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public List<UsuariosEntity> executeUsers() {
        UsuariosExcelUtils usuariosExcelUtils = new UsuariosExcelUtils();

        byte[] content = getBytes();

        int[] desiredUsersColumns = {3, 5, 6};
        List<UsuariosEntity> usersExcelData = usuariosExcelUtils.readExcelUsersSheet(content, 0, desiredUsersColumns);

        return this.usuariosRepository.saveAll(usersExcelData);
    }

    private byte[] getBytes() {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new BucketNotFoundException("Bucket not found");
        }
        Blob blob = bucket.get(USERS);
        if (blob == null) {
            throw new BucketNotFoundException("File:" + USERS.split("/")[1] +  " not found");
        }

        byte[] content = blob.getContent();
        return content;
    }
}
