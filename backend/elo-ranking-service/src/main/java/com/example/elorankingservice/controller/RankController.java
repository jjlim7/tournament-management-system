package com.example.elorankingservice.controller;


import com.example.elorankingservice.entity.RankThreshold;
import com.example.elorankingservice.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
public class RankController {
    private final RankService rankService;

    @Autowired
    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping("/ranks")
    public ResponseEntity<List<RankThreshold>> getRankThresholds() {
        List<RankThreshold> ranks = rankService.retrieveRankThresholds();
        return ResponseEntity.ok(ranks);
    }

    @GetMapping("/{rankThresholdId}")
    public ResponseEntity<RankThreshold> getRankThresholdById(@PathVariable Long rankThresholdId) {
        RankThreshold rankThreshold = rankService.retrieveRankThresholdById(rankThresholdId);
        if (rankThreshold == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rankThreshold);
    }

}
