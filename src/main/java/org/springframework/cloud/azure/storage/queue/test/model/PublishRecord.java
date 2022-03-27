package org.springframework.cloud.azure.storage.queue.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishRecord {
    private String entity;
    private String id;
    private LocalDateTime timestamp;
    private String action;
    private List<Collaborateur> entities = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Collaborateur {
        private String id;
        private String firstName;
        private String lastName;
    }
}
