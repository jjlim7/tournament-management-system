package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.Rank;
import com.example.elorankingservice.entity.RankThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;

@NoRepositoryBean
public interface RankThresholdRepository extends JpaRepository<RankThreshold, Long> {
    // Create

    // Read
    RankThreshold getRankThresholdById(Long id);
    RankThreshold getRankThresholdByRank(Rank rank);
    List<RankThreshold> getRankThresholds();

    // Update
    RankThreshold update(RankThreshold rankThreshold);

}
