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
    - use placement as the outcome - range of 0 to 1, `placement = 1.0 - ((Position - 1) / (Total Players - 1) * 0.1)
      `.
  
- α (alpha): A scaling factor that determines how much the player's performance (RPS) affects the outcome. This allows us to fine-tune the impact of the player’s role-specific performance on the final score based on relative rating of the opponents.
  - for Battle Royale:
    - `K constant * (average rating of all players / current rating of player)`, `K constant` to be decided.
  - for Clan War:
    - `K constant * (average rating of both clans / current rating of clan)`, `K constant` to be decided.
  
- Normalized RPS: The Role Performance Score (RPS) for player i, normalized to a range (e.g., between -1 and 1), ensuring that all performance metrics are comparable across different roles and matches.
#### Normalization Formula

For each player `i`, the formula is:

```
Normalized RPS_i = (RPS_i - μ_RPS) / (R_max - R_min)
```

Where:
- `RPS_i` is the Role Performance Score of player `i`.
- `μ_RPS` is the mean **RPS** of all players / teams in the match.
- `R_max` is the maximum **RPS** among all players.
- `R_min` is the minimum **RPS** among all players.

This formula ensures that the highest performer gets a normalized score close to 1, the lowest performer gets a score close to -1, and other players are distributed proportionally in between.
---

### Example for Clan war / Battle royale

Let’s assume we have the following **RPS** values for players:

| Player   | RPS  |
|----------|------|
| Player 1 | 5.96 |
| Player 2 | 4.08 |
| Player 3 | 10.88|
| Player 4 | 8.82 |
| Player 5 | 2.32 |

1. **Mean `μ_RPS`**:
    ``` 
    μ_RPS = (5.96 + 4.08 + 10.88 + 8.82 + 2.32) / 5 = 6.41
    ```

2. **Max `R_max`**: 10.88
3. **Min `R_min`**: 2.32

Now, to normalize Player 1’s score:

```
Normalized RPS_1 = (5.96 - 6.41) / (10.88 - 2.32) 
                 = (-0.45) / 8.56 
                 ≈ -0.053
```

Similar calculations would be applied to the other players to normalize their **RPS**.


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

#### 8. Contextual Factors and Multi-Factor Adjustments
- The system supports adjustments for contextual factors like:
    - Opponent strength: Matches against significantly stronger opponents may result in smaller μ reductions for losses and larger μ gains for wins, which is the α (alpha).

---
### Process Flow (Clan War and Battle Royale)

---

### **5v5 Clan War Scenario**

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
During the match, specific metrics are gathered based on each player's role.

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

#### **Step 4: Normalize RPS and Adjust Performance-Based Outcomes (PBO)**

1. **Normalization**:
  - **Mean RPS for Team A**:  
    `(8,100 + 10,400 + 20,000 + 18,000 + 15,000) / 5 = 14,300`
  - **Mean RPS for Team B**:  
    `(7,200 + 8,400 + 18,000 + 17,000 + 16,000) / 5 = 13,320`
  - Normalization Formula:
   ```
   Normalized RPS_i = (RPS_i - μ_RPS) / (R_max - R_min)
   ```

2. **PBO Calculation**:  
   Use the formula:  
   `PBO_i = Match Outcome * (1 + α * Normalized RPS_i)`  
   Assume **α = 0.1**.

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

Adjust **μ** and **σ** before passing values to TrueSkill.

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
Finally, we pass the updated μ and σ values into the **TrueSkill algorithm** for the final update.

---



### **Battle Royale Scenario**

#### **Step 1: Gather Pre-Match TrueSkill Ratings**

| Player | μ  | σ   |
|--------|----|-----|
| P1     | 28 | 7.5 |
| P2     | 25 | 8.0 |
| P3     | 30 | 6.5 |
| P4     | 27 | 7.0 |
| P5     | 26 | 7.2 |

#### **Step 2: Gather Role-Specific Performance Metrics**

| Player | Kills | Damage Dealt | Survival Time (seconds) | Placement |
|--------|-------|--------------|-------------------------|-----------|
| P1     | 5     | 8,000        | 1,200                   | 3rd       |
| P2     | 2     | 5,000        | 900                     | 6th       |
| P3     | 10    | 12,000       | 1,400                   | 1st       |
| P4     | 7     | 9,000        | 1,300                   | 2nd       |
| P5     | 1     | 4,000        | 600                     | 10th      |

#### **Step 3: Calculate Role Performance Scores (RPS)**

| Player | RPS Calculation                                          | RPS   |
|--------|----------------------------------------------------------|-------|
| P1     | (0.4 * 5) + (0.4 * 8,000/1,000) + (0.2 * 1,200/1,000)    | 5.96  |
| P2     | (0.4 * 2) + (0.4 * 5,000/1,000) + (0.2 * 900/1,000)      | 4.08  |
| P3     | (0.4 * 10) + (0.4 * 12,000/1,000) + (0.2 * 1,400/1,000)  | 10.88 |
| P4     | (0.4 * 7) + (0.4 * 9,000/1,000) + (0.2 * 1,300/1,000)    | 8.82  |
| P5     | (0.4 * 1) + (0.4 * 4,000/1,000) + (0.2 * 600/1,000)      | 2.32  |

#### **Step 4: Normalize RPS and Adjust PBO**

1. **Match Outcome Calculation**:

   The match outcome is calculated as:

   ```plaintext
   Match Outcome = 1.0 - ((Position - 1) / (Total Players - 1) * 0.1)
   ```

   For the example with 10 players, the outcomes would be:

   | Player | Placement | Match Outcome |
      |--------|-----------|---------------|
   | P1     | 3rd       | 0.8           |
   | P2     | 6th       | 0.5           |
   | P3     | 1st       | 1.0           |
   | P4     | 2nd       | 0.9           |
   | P5     | 10th      | 0.1           |

2. **Normalization**:

   To normalize the Role Performance Score (RPS), we use the following formula:

   ```plaintext
   Normalized RPS_i = (RPS_i - μ_RPS) / (R_max - R_min)
   ```

   Where:
    - **RPS_i** is the Role Performance Score for player *i*.
    - **μ_RPS** is the average RPS for all players.
    - **R_max** is the maximum RPS among all players.
    - **R_min** is the minimum RPS among all players.

   For this example:

    - **Mean RPS**:

      ```
      μ_RPS = (5.96 + 4.08 + 10.88 + 8.82 + 2.32) / 5
            = 6.41
      ```

    - **Max RPS**: `10.88`
    - **Min RPS**: `2.32`

   Now, let's normalize the RPS for Player 1:

   ```plaintext
   Normalized RPS_1 = (5.96 - 6.41) / (10.88 - 2.32)
                    = (-0.45) / 8.56
                    ≈ -0.053
   ```

   This calculation shows that Player 1's performance, relative to others, is slightly below average, with a normalized RPS of approximately `-0.053`.


3. **PBO Calculation**:

   The **Performance-Based Outcome (PBO)** is calculated using the formula:

   ```plaintext
   PBO_i = Match Outcome * (1 + α * Normalized RPS_i)
   ```

   Assuming **α = 0.1**, the PBO values are:

   | Player | Normalized RPS | Placement Outcome | PBO   |
      |--------|----------------|-------------------|-------|
   | P1     | -0.053         | 0.8               | 0.796 |
   | P2     | -0.273         | 0.5               | 0.486 |
   | P3     | 1.0            | 1.0               | 1.1   |
   | P4     | 0.71           | 0.9               | 0.971 |
   | P5     | -0.478         | 0.1               | 0.052 |

#### **Step 5: Modify μ and σ Based on PBO**

After calculating the **PBO** for each player, use it to adjust the **μ (mean skill)** and **σ (uncertainty)** values for each player:

| Player | Pre-Match μ | Pre-Match σ | Δμ    | Δσ    | New μ | New σ |
|--------|-------------|-------------|-------|-------|-------|-------|
| P1     | 28          | 7.5         | +0.4  | -0.2  | 28.4  | 7.3   |
| P2     | 25          | 8.0         | -0.3  | +0.3  | 24.7  | 8.3   |
| P3     | 30          | 6.5         | +1.0  | -0.4  | 31.0  | 6.1   |
| P4     | 27          | 7.0         | +0.8  | -0.3  | 27.8  | 6.7   |
| P5     | 26          | 7.2         | -0.5  | +0.3  | 25.5  | 7.5   |

#### **Step 6: Update μ and σ with TrueSkill**

The adjusted **μ** and **σ** values are fed into TrueSkill for the final Bayesian update.

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