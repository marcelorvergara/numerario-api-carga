package com.numerario.apicarga.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;

@Configuration
public class GoogleCloudStorageConfig {

    @Autowired
    private ResourceLoader resourceLoader;
    @Value("${gcp.service.account}")
    private String serviceAccount;

    @Bean
    public Storage storage() throws IOException {
        var resource = resourceLoader.getResource("classpath:" + serviceAccount);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}
