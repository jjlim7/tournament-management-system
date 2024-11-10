package csd.backend.matchmaking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class Response {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntityIdResponse {
        private String entityType;
        private List<Long> ids;
    }
}
