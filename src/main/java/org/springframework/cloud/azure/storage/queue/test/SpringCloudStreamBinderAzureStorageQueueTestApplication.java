package org.springframework.cloud.azure.storage.queue.test;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobUrlParts;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.azure.storage.queue.test.model.PublishRecord;
import org.springframework.cloud.stream.binder.azure.storagequeue.schema.StorageQueueMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

@SpringBootApplication
public class SpringCloudStreamBinderAzureStorageQueueTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamBinderAzureStorageQueueTestApplication.class, args);
    }


    @Bean
    public Consumer<Message<StorageQueueMessage>> handle(BlobServiceClient blobServiceClient) {
        return msg -> {
            System.out.println(msg.getPayload().getEventType());
            if (msg.getPayload().getEventType().equals("Microsoft.Storage.BlobCreated")) {

                BlobUrlParts blobUrlParts = BlobUrlParts.parse(msg.getPayload().getData().getUrl());

                BlobContainerClient blobContainerClient =
                        blobServiceClient.getBlobContainerClient(blobUrlParts.getBlobContainerName());

                try (ByteArrayOutputStream ostream = new ByteArrayOutputStream(msg.getPayload().getData().getContentLength())) {

                    blobContainerClient.getBlobClient(blobUrlParts.getBlobName()).downloadStream(ostream);

                    PublishRecord record = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(ostream.toString(), PublishRecord.class);
                    System.out.println("Record : " + record);

                    // We have the record, don't need anymore the blob, can be deleted
                    blobContainerClient.getBlobClient(blobUrlParts.getBlobName()).delete();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read blob", e);
                }
            }
        };
    }

}
