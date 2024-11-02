package com.example.tournamentservice.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EntityResponseDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntityIdResponse {
        private String entityType;
        private List<Long> ids;
    }
}
