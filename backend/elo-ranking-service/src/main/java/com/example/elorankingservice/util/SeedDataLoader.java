package com.example.elorankingservice.util;

import com.example.elorankingservice.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedDataLoader implements CommandLineRunner {

    private final RankService rankService;

    @Autowired
    public SeedDataLoader(RankService rankService) {
        this.rankService = rankService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {
        rankService.seedRankThresholds();
        System.out.println("Rank Thresholds seeded.");
    }
}
