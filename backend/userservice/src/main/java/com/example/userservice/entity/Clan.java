package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Clan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clanId;
    private String clanName;

    @OneToMany(mappedBy = "clan", cascade =  CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClanUser> clanUsers;

    
 
}
