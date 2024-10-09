package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClanUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clanUserId;

    // Many-to-one relationship with User
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // Many-to-one relationship with Clan
    @ManyToOne
    @JoinColumn(name = "clanId", nullable = false)
    private Clan clan;

    @Column(nullable = false)
    private Boolean isClanLeader;

    @Column
    private String position;
    
}
