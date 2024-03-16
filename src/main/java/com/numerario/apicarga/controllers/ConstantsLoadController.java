package com.numerario.apicarga.controllers;


import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.numerario.apicarga.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/constants")
public class ConstantsLoadController {

    private static final String USERS_TERMINALS = "constantes/Base de Usuários e Terminais Atuailzada.xlsx";
    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @GetMapping("/load-users")
    public ResponseEntity<String> loadUsersFromFile() {

        ExcelUtils excelUtils = new ExcelUtils();

        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bucket not found");
        }
        Blob blob = bucket.get(USERS_TERMINALS);
        if (blob == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

        byte[] content = blob.getContent();

        String excelData = excelUtils.readExcelFile(content, 0);

        return ResponseEntity.status(HttpStatus.OK).body(excelData);
    }
}
