package csd.backend.matchmaking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankThreshold {
    double minRating;
    double maxRating;
    Rank rank;

    public RankThreshold(Rank rank, long minRating, long maxRating) {
        this.rank = rank;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    public enum Rank {
        GOLD, SILVER, PLATINUM
    }
}
