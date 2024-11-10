package csd.backend.matchmaking.feigndto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClanUser {
    private Long clanUserId;
    private Long userId;
    private Long clanId;
    private Boolean isClanLeader;
    private String position;
}

