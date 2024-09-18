package com.example.elorankingservice.service;


import com.example.elorankingservice.entity.RankThreshold;
import com.example.elorankingservice.repository.RankThresholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {
    // for rank threshold

    private final RankThresholdRepository rankThresholdRepository;

    @Autowired
    public RankService(RankThresholdRepository rtr) {
        this.rankThresholdRepository = rtr;
    }

    public List<RankThreshold> retrieveRankThresholds() {
        return rankThresholdRepository.findAll();
    }

    public RankThreshold retrieveRankThresholdById(Long id) {
        return rankThresholdRepository.findById(id).orElse(null);
    }

    public RankThreshold retrieveRankThresholdByRank(RankThreshold.Rank rank) {
        return rankThresholdRepository.findByRank(rank).orElse(null);
    }

    public RankThreshold retrieveRankThresholdByRating(double rating) throws Exception {
        List<RankThreshold> rankThresholds = retrieveRankThresholds();
        for (RankThreshold rankThreshold : rankThresholds) {
            if (rankThreshold.getMinRating() >= rating && rankThreshold.getMaxRating() <= rating) {
                return rankThreshold;
            }
        }
        throw new Exception("Rank threshold could not be found");
    }

    public RankThreshold createNewRankThreshold(RankThreshold rankThreshold) {
        return rankThresholdRepository.save(rankThreshold);
    }

    public RankThreshold updateRankRating(RankThreshold.Rank rank, double minRating, double maxRating) {
        return rankThresholdRepository.updateMinRatingAndMaxRatingByRank(rank, minRating, maxRating);
    }
}
