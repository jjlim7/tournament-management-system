package com.example.userservice.entity;

import com.example.userservice.feigndto.PlayerEloRank;
import com.example.userservice.feigndto.PlayerStats;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PlayerOverallStats {
    PlayerStats stats;
    PlayerEloRank eloRank;
    ClanUser clanUser;
}
