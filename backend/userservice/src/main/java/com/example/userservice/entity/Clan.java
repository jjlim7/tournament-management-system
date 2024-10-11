package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clan_id")
    private Long clanId;

    @Column(name = "clan_name")
    private String clanName;

    @OneToMany(mappedBy = "clan", cascade =  CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClanUser> clanUsers;

}
