package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Clan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clanId;
    private String clanName;
    
    // public Clan(Long clanId, String clanName) {
    //     this.clanId = clanId;
    //     this.clanName = clanName;
    // }

    // public Long getClanId() {
    //     return clanId;
    // }

    // public void setClanId(Long clanId) {
    //     this.clanId = clanId;
    // }

    // public String getClanName() {
    //     return clanName;
    // }

    // public void setClanName(String clanName) {
    //     this.clanName = clanName;
    // }
 
}
