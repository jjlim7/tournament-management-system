package com.example.elorankingservice.entity;


import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GameScore {
    @Id
    private Long id;
    private Long gameId;
}
