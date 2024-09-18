package com.example.elorankingservice.service;


import com.example.elorankingservice.entity.RankThreshold;
import com.example.elorankingservice.repository.RankThresholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {

    private static final double INITIAL_MIN_RATING = 0;      // Starting rating for the first rank (IRON)
    private static final double INITIAL_RANGE = 10;          // Initial range for the first rank
    private static final double GROWTH_RATE = 1.5;           // Growth rate of 50% per rank

    private final RankThresholdRepository rankThresholdRepository;

    @Autowired
    public RankService(RankThresholdRepository rankThresholdRepository) {
        this.rankThresholdRepository = rankThresholdRepository;
    }

    // Seed data with increasing ranges for each rank
    private static final List<RankThreshold> SEED_RANK_THRESHOLDS = createRankThresholds();

    private static List<RankThreshold> createRankThresholds() {
        double minRating = INITIAL_MIN_RATING;
        double range = INITIAL_RANGE;

        return List.of(
                new RankThreshold(RankThreshold.Rank.IRON, minRating, minRating += range),
                new RankThreshold(RankThreshold.Rank.BRONZE, minRating, minRating += (range *= GROWTH_RATE)),
                new RankThreshold(RankThreshold.Rank.SILVER, minRating, minRating += (range *= GROWTH_RATE)),
                new RankThreshold(RankThreshold.Rank.GOLD, minRating, minRating += (range *= GROWTH_RATE)),
                new RankThreshold(RankThreshold.Rank.PLATINUM, minRating, minRating += (range *= GROWTH_RATE)),
                new RankThreshold(RankThreshold.Rank.DIAMOND, minRating, minRating += (range *= GROWTH_RATE)),
                new RankThreshold(RankThreshold.Rank.MASTER, minRating, minRating += (range *= GROWTH_RATE)),
                new RankThreshold(RankThreshold.Rank.GRANDMASTER, minRating, minRating += (range *= GROWTH_RATE)),
                new RankThreshold(RankThreshold.Rank.CHALLENGER, minRating, minRating + range * GROWTH_RATE)
        );
    }

    public void seedRankThresholds() {
        // Check if rank thresholds are already seeded
        if (rankThresholdRepository.count() == 0) {
            // Seed the rank thresholds in the repository
            rankThresholdRepository.saveAll(SEED_RANK_THRESHOLDS);
        }
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
