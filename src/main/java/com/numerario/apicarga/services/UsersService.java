package com.numerario.apicarga.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.entities.UsuariosEntity;
import com.numerario.apicarga.exceptions.BucketNotFoundException;
import com.numerario.apicarga.repositories.TerminalsRepository;
import com.numerario.apicarga.repositories.UsersRepository;
import com.numerario.apicarga.utils.TerminalsExcelUtils;
import com.numerario.apicarga.utils.UsersExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private static final String USERS = "constantes/Base de Usuários e Terminais Atuailzada.xlsx";
    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Autowired
    private UsersRepository usersRepository;

    public List<UsuariosEntity> executeUsers() {
        UsersExcelUtils usersExcelUtils = new UsersExcelUtils();

        byte[] content = getBytes();

        int[] desiredUsersColumns = {3, 5, 6};
        List<UsuariosEntity> usersExcelData = usersExcelUtils.readExcelUsersSheet(content, 0, desiredUsersColumns);

        return this.usersRepository.saveAll(usersExcelData);
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
