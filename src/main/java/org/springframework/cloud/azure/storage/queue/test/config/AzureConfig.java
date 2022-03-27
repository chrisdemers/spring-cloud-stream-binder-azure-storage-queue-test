package org.springframework.cloud.azure.storage.queue.test.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    @Value("${azure.storage.connection}")
    private String azureConnection;

    @Bean
    public BlobServiceClient getBlobServiceClient() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureConnection)
                .buildClient();
        return blobServiceClient;
    }
}
