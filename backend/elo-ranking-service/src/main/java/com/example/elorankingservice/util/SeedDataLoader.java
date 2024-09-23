package com.example.elorankingservice.util;

import com.example.elorankingservice.service.EloRankingService;
import com.example.elorankingservice.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedDataLoader implements CommandLineRunner {

    private final RankService rankService;
    private final EloRankingService eloRankingService;

    @Autowired
    public SeedDataLoader(RankService rankService, EloRankingService eloRankingService) {
        this.rankService = rankService;
        this.eloRankingService = eloRankingService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {
        rankService.seedRankThresholds();
        eloRankingService.seedPlayerEloRanks();
        System.out.println("Rank Thresholds seeded.");
    }

}