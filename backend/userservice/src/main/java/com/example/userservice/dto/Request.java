package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

public class Request {
    
    @Getter
    @Setter
    public static class CreateClanUser {
        Long userId;
        Long clanId;
        String position;
        Boolean isClanLeader;
    }

    @Getter
    @Setter
    public static class UpdateClanUser {
        Long userId;
        Long clanId;
        String position;
        Boolean isClanLeader;
    }

    
}
