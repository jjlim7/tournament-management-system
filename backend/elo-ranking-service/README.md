# Elo Ranking Service


### Enhanced Elo Algorithm with TrueSkill and Role-Specific Performance Metrics

This algorithm integrates Microsoft's TrueSkill system with role-specific performance metrics and multi-factor adjustments to create a sophisticated and adaptive ranking system, suited for both team-based games (e.g., Clan Wars) and Free-for-All games (e.g., Battle Royale). The system ensures that players are ranked not just based on wins or losses but also on their individual contributions within the match, accounting for different roles and playstyles.

#### 1. Initial TrueSkill Rating
- Every player starts with an initial rating consisting of:
    - μ (mean skill estimate): A measure of the player’s perceived skill level (e.g., 25).
    - σ (uncertainty): The system’s confidence in the player's skill (e.g., 8.333). A higher σ means the system is less certain about the player's skill and will adjust it more significantly in the early matches.

  These values are used to calculate the probability of a player’s or team’s win before each match.

#### 2. Role-Specific Performance Metrics
- During the match, performance metrics relevant to the player's role are tracked. These metrics vary depending on the role and game mode:
    - For Team Games (e.g., Clan Wars):
        - Tanks: Damage mitigated, time spent holding objectives.
        - Healers: Total healing done, revives, debuffs removed.
        - Damage Dealers: Total damage dealt, kill/death ratio.
    - For Free-for-All Games (e.g., Battle Royale):
        - Metrics might include damage dealt, kills, survival time, and placement.

  Each metric is given a weight that reflects its importance to the role. The combined weighted metrics produce a Role Performance Score (RPS) for each player, representing their individual contribution to the match.

---

#### 3. Performance-Based Outcome (PBO)

To account for individual player performance during a match, we use the Performance-Based Outcome (PBO) formula. This formula adjusts the match outcome based on each player's contribution, measured by their Role Performance Score (RPS).

PBO Formula:

`PBO_i = Match Outcome * (1 + α * Normalized RPS_i)`

- PBO: Performance-Based Outcome for player i.
- Match Outcome: The raw result of the match:
    - 1 if the player’s team won.
    - 0 if the player’s team lost.
- α (alpha): A scaling factor that determines how much the player's performance (RPS) affects the outcome. This allows us to fine-tune the impact of the player’s role-specific performance on the final score.
- Normalized RPS: The Role Performance Score (RPS) for player i, normalized to a range (e.g., between -1 and 1), ensuring that all performance metrics are comparable across different roles and matches.

### Example:
If Player A's team wins, and their normalized RPS is 0.5, with an α value of 0.1, the PBO would be calculated as:

`PBO_A = 1 * (1 + 0.1 * 0.5) = 1.05`

This means Player A’s contribution is factored into the match outcome, rewarding them slightly more for their higher performance.

---

#### 4. Adjusting Player Ratings (μ and σ)
- Once the PBO is calculated for each player, their μ (mean skill estimate) and σ (uncertainty) are adjusted to reflect their performance:
    - Players with high PBO scores (due to good performance) will see their μ increase and σ decrease (indicating a more confident skill estimate).
    - Players with low PBO scores will experience a decrease in μ, and their σ may increase if their performance was unexpected (e.g., a top-ranked player performing poorly).

  These adjustments are made before passing the final values into TrueSkill for Bayesian updates.

#### 5. TrueSkill Bayesian Update
- After the match, TrueSkill’s core algorithm is used to update each player's μ and σ based on the adjusted performance scores. TrueSkill accounts for:
    - The probability of the match outcome based on pre-match μ and σ values.
    - Surprising outcomes: If a low-ranked player defeats a high-ranked player or if a team with lower combined skill wins, TrueSkill will adjust ratings more drastically.

  The Bayesian update further refines each player’s skill distribution, gradually reducing σ as more matches are played, making the system more confident in the player’s rating over time.

#### 6. Composite Skill Rating for Matchmaking
- In team-based games, a composite team skill rating is calculated by combining the μ values of all players on the team. The TrueSkill system also combines the uncertainties (σ values) to calculate the overall team uncertainty.

  This composite rating ensures that players are matched with teams of similar skill levels, while accounting for the variability in performance (uncertainty). As σ decreases over time, players’ ratings stabilize, leading to more accurate matchmaking.

#### 7. Handling Free-for-All Game Modes
- In Free-for-All (FFA) games like Battle Royale, TrueSkill handles multiple players by evaluating each player’s placement in the match. Players who survive longer or place higher are ranked better than those who are eliminated early.
    - FFA-specific adjustments: Performance metrics (e.g., kills, damage dealt, survival time) are factored into the ranking to account for individual contributions beyond just final placement.

#### 8. Contextual Factors and Multi-Factor Adjustments
- The system supports adjustments for contextual factors like:
    - Map difficulty: Harder maps may increase the uncertainty (σ) adjustment.
    - Opponent strength: Matches against significantly stronger opponents may result in smaller μ reductions for losses and larger μ gains for wins.
    - Environmental factors: Random events (e.g., weather conditions, game-specific modifiers) can also affect the rating adjustments, allowing the system to reflect the difficulty of different match conditions.

#### 9. Dynamic Matchmaking and Leaderboards
- Matchmaking: Players with similar μ and σ values are matched together to ensure balanced games. The system can also use role-based matchmaking to create teams with complementary skills (e.g., pairing strong damage dealers with strong healers).

- Leaderboards: Players are ranked based on their μ value. The system can support role-specific leaderboards, allowing players to see how they rank within their specific role (e.g., top Tanks, top Healers, etc.).

- Rating Decay: For players who become inactive, their σ may gradually increase, reflecting growing uncertainty about their current skill level. This allows the system to recalibrate their rating when they return to active play.

---

### Process Flow
- Integrating role-specific performance metrics and TrueSkill. Using a **5v5 Clan War scenario** where each team has players with distinct roles (e.g., Tank, Healer, Damage Dealers).
- **Team A vs. Team B**: A 5v5 match.
- **Roles**:
  - **Team A**: Player A1 (Tank), A2 (Healer), A3-A5 (Damage Dealers).
  - **Team B**: Similar role distribution.
- **Match Outcome**: Team A wins the match.
- **Role-Specific Metrics**: Damage mitigated, healing done, and damage dealt are recorded for each player.

---

#### **Step 1: Gather Pre-Match TrueSkill Ratings**
Each player starts with **μ (mean skill)** and **σ (skill uncertainty)** from previous matches.

| Player | Role           | μ  | σ   |
|--------|----------------|----|-----|
| A1     | Tank           | 28 | 7.5 |
| A2     | Healer         | 25 | 8.0 |
| A3     | Damage Dealer  | 30 | 6.5 |
| A4     | Damage Dealer  | 27 | 7.0 |
| A5     | Damage Dealer  | 26 | 7.2 |
| B1     | Tank           | 27 | 7.4 |
| B2     | Healer         | 24 | 8.1 |
| B3     | Damage Dealer  | 29 | 6.8 |
| B4     | Damage Dealer  | 26 | 7.3 |
| B5     | Damage Dealer  | 27 | 7.0 |

#### **Step 2: Gather Role-Specific Performance Metrics**
During the match, specific metrics are gathered based on each player's role:

| Player | Role           | Damage Mitigated | Healing Done | Damage Dealt |
|--------|----------------|------------------|--------------|--------------|
| A1     | Tank           | 10,000           | N/A          | 3,000        |
| A2     | Healer         | N/A              | 12,000       | 2,000        |
| A3     | Damage Dealer  | N/A              | N/A          | 20,000       |
| A4     | Damage Dealer  | N/A              | N/A          | 18,000       |
| A5     | Damage Dealer  | N/A              | N/A          | 15,000       |
| B1     | Tank           | 8,000            | N/A          | 4,000        |
| B2     | Healer         | N/A              | 9,000        | 3,000        |
| B3     | Damage Dealer  | N/A              | N/A          | 18,000       |
| B4     | Damage Dealer  | N/A              | N/A          | 17,000       |
| B5     | Damage Dealer  | N/A              | N/A          | 16,000       |

#### **Step 3: Calculate Role Performance Scores (RPS)**
Using predefined weights for each metric, calculate the **Role Performance Score (RPS)** for each player.

- **Weights**:
  - For **Tanks**: 70% damage mitigated, 30% damage dealt.
  - For **Healers**: 80% healing done, 20% damage dealt.
  - For **Damage Dealers**: 100% damage dealt.

| Player | Role           | RPS Calculation                                                 | RPS   |
|--------|----------------|-----------------------------------------------------------------|-------|
| A1     | Tank           | (0.7 * 10,000) + (0.3 * 3,000)                                 | 8,100 |
| A2     | Healer         | (0.8 * 12,000) + (0.2 * 2,000)                                 | 10,400|
| A3     | Damage Dealer  | (1.0 * 20,000)                                                  | 20,000|
| A4     | Damage Dealer  | (1.0 * 18,000)                                                  | 18,000|
| A5     | Damage Dealer  | (1.0 * 15,000)                                                  | 15,000|
| B1     | Tank           | (0.7 * 8,000) + (0.3 * 4,000)                                  | 7,200 |
| B2     | Healer         | (0.8 * 9,000) + (0.2 * 3,000)                                  | 8,400 |
| B3     | Damage Dealer  | (1.0 * 18,000)                                                  | 18,000|
| B4     | Damage Dealer  | (1.0 * 17,000)                                                  | 17,000|
| B5     | Damage Dealer  | (1.0 * 16,000)                                                  | 16,000|

#### **Step 4: Adjust Performance-Based Outcomes (PBO)**
We adjust the **match outcome** for each player using their RPS. Assume that the match outcome for Team A is a **win (1)** and for Team B a **loss (0)**.

- Use the formula:  
  `PBO_i = Match Outcome * (1 + α * Normalized RPS_i)`
- Assume **α = 0.1** and that RPS is normalized between -1 and 1 (subtract the mean and divide by the range).

| Player | Role           | Normalized RPS (approx.) | Match Outcome | PBO   |
|--------|----------------|--------------------------|---------------|-------|
| A1     | Tank           | 0.2                      | 1             | 1.02  |
| A2     | Healer         | 0.5                      | 1             | 1.05  |
| A3     | Damage Dealer  | 1.0                      | 1             | 1.10  |
| A4     | Damage Dealer  | 0.8                      | 1             | 1.08  |
| A5     | Damage Dealer  | 0.5                      | 1             | 1.05  |
| B1     | Tank           | 0.1                      | 0             | 0.00  |
| B2     | Healer         | 0.2                      | 0             | 0.00  |
| B3     | Damage Dealer  | 0.6                      | 0             | 0.00  |
| B4     | Damage Dealer  | 0.4                      | 0             | 0.00  |
| B5     | Damage Dealer  | 0.3                      | 0             | 0.00  |

#### **Step 5: Modify μ and σ Based on PBO**
Before passing these values to TrueSkill for updating, we slightly adjust **μ** and **σ** based on the performance (PBO).

For Team A (winning team), each player’s μ is adjusted **upwards**, and their σ is slightly reduced because they performed well. For Team B (losing team), μ is adjusted **downwards**, with σ increasing for players with high uncertainty.

- Example adjustments:
  - **Player A3**: High PBO (1.10), strong performance → μ increases more significantly, σ decreases.
  - **Player B1**: Poor performance → μ decreases, σ increases.

| Player | Role           | Pre-Match μ | Pre-Match σ | Δμ    | Δσ    | New μ | New σ |
|--------|----------------|-------------|-------------|-------|-------|-------|-------|
| A1     | Tank           | 28          | 7.5         | +0.5  | -0.2  | 28.5  | 7.3   |
| A2     | Healer         | 25          | 8.0         | +0.8  | -0.3  | 25.8  | 7.7   |
| A3     | Damage Dealer  | 30          | 6.5         | +1.0  | -0.4  | 31.0  | 6.1   |
| A4     | Damage Dealer  | 27          | 7.0         | +0.9  | -0.3  | 27.9  | 6.7   |
| A5     | Damage Dealer  | 26          | 7.2         | +0.8  | -0.3  | 26.8  | 6.9   |
| B1     | Tank           | 27          | 7.4         | -0.4  | +0.3  | 26.6  | 7.7   |
| B2     | Healer         | 24          | 8.1         | -0.3  | +0.2  | 23.7  | 8.3   |
| B3     | Damage Dealer  | 29          | 6.8         | -0.6  | +0.3  | 28.4  | 7.1   |
| B4     | Damage Dealer  | 26          | 7.3         | -0.5  | +0.3  | 25.5  | 7.6   |
| B5     | Damage Dealer  | 27          | 7.0         | -0.4  | +0.2  | 26.6  | 7.2   |

#### **Step 6: Update μ and σ with TrueSkill**
Finally, we pass the updated μ and σ values into the **TrueSkill algorithm** to perform the final update. TrueSkill will further refine the ratings based on the match outcome (PBO) and the uncertainties (σ), providing the updated skill ratings for the next match.

---

### Summary
This process flow integrates role-specific performance metrics into TrueSkill by:
1. **Collecting role-specific data** during the match.
2. **Calculating performance scores** (RPS) based on that data.
3. **Adjusting match outcomes** (PBO) based on player performance.
4. **Modifying μ and σ** values before updating them with TrueSkill to reflect performance more accurately.

By following this process, players are rewarded or penalized based not only on whether their team won or lost but also on how well they contributed to their specific role in the match.

### Key Benefits of This Algorithm
1. Fairer Ratings: Players are rewarded based on both match outcomes and their individual role-based performance, leading to more accurate skill estimates.
2. Adaptability: The system can handle various game types, including team-based and Free-for-All modes, with flexibility for different performance metrics.
3. Accurate Matchmaking: TrueSkill’s Bayesian approach, combined with performance-based adjustments, ensures players are matched with similarly skilled opponents.
4. Contextual Sensitivity: The system can factor in contextual elements such as map difficulty and opponent strength, providing a nuanced view of player performance.
5. Player Engagement: By integrating role-specific performance, players receive feedback that’s directly tied to how well they played their role, rather than just whether they won or lost.

---

This advanced algorithm provides a comprehensive approach to player skill evaluation and matchmaking, creating a fair, flexible, and adaptive system that accurately reflects both team and individual contributions in competitive games.