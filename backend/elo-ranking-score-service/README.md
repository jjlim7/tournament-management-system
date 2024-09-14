# elo score ranking service


### Enhanced Elo Algorithm with TrueSkill and Role-Specific Performance Metrics

This algorithm integrates Microsoft's **TrueSkill** system with **role-specific performance metrics** and **multi-factor adjustments** to create a sophisticated and adaptive ranking system, suited for both team-based games (e.g., Clan Wars) and Free-for-All games (e.g., Battle Royale). The system ensures that players are ranked not just based on wins or losses but also on their individual contributions within the match, accounting for different roles and playstyles.

#### **1. Initial TrueSkill Rating**
- Every player starts with an initial rating consisting of:
    - **μ (mean skill estimate)**: A measure of the player’s perceived skill level (e.g., 25).
    - **σ (uncertainty)**: The system’s confidence in the player's skill (e.g., 8.333). A higher σ means the system is less certain about the player's skill and will adjust it more significantly in the early matches.

  These values are used to calculate the probability of a player’s or team’s win before each match.

#### **2. Role-Specific Performance Metrics**
- During the match, performance metrics relevant to the player's role are tracked. These metrics vary depending on the role and game mode:
    - **For Team Games (e.g., Clan Wars)**:
        - **Tanks**: Damage mitigated, time spent holding objectives.
        - **Healers**: Total healing done, revives, debuffs removed.
        - **Damage Dealers**: Total damage dealt, kill/death ratio.
    - **For Free-for-All Games (e.g., Battle Royale)**:
        - Metrics might include damage dealt, kills, survival time, and placement.

  Each metric is given a weight that reflects its importance to the role. The combined weighted metrics produce a **Role Performance Score (RPS)** for each player, representing their individual contribution to the match.

To make the formula readable in Markdown (which doesn't support LaTeX), you can convert it to plain text or use basic Markdown formatting. Here’s how you can present it in a Markdown-friendly format:

---

#### **3. Performance-Based Outcome (PBO)**

To account for individual player performance during a match, we use the **Performance-Based Outcome (PBO)** formula. This formula adjusts the match outcome based on each player's contribution, measured by their **Role Performance Score (RPS)**.

**PBO Formula**:

```
PBO_i = Match Outcome * (1 + α * Normalized RPS_i)
```

- **PBO**: Performance-Based Outcome for player `i`.
- **Match Outcome**: The raw result of the match:
  - `1` if the player’s team won.
  - `0` if the player’s team lost.
- **α (alpha)**: A scaling factor that determines how much the player's performance (RPS) affects the outcome. This allows us to fine-tune the impact of the player’s role-specific performance on the final score.
- **Normalized RPS**: The **Role Performance Score (RPS)** for player `i`, normalized to a range (e.g., between -1 and 1), ensuring that all performance metrics are comparable across different roles and matches.

### Example:
If Player A's team wins, and their normalized RPS is `0.5`, with an α value of `0.1`, the PBO would be calculated as:

```
PBO_A = 1 * (1 + 0.1 * 0.5) = 1.05
```

This means Player A’s contribution is factored into the match outcome, rewarding them slightly more for their higher performance.

---

#### **4. Adjusting Player Ratings (μ and σ)**
- Once the PBO is calculated for each player, their **μ (mean skill estimate)** and **σ (uncertainty)** are adjusted to reflect their performance:
    - Players with high PBO scores (due to good performance) will see their **μ** increase and **σ** decrease (indicating a more confident skill estimate).
    - Players with low PBO scores will experience a decrease in μ, and their σ may increase if their performance was unexpected (e.g., a top-ranked player performing poorly).

  These adjustments are made **before** passing the final values into TrueSkill for Bayesian updates.

#### **5. TrueSkill Bayesian Update**
- After the match, TrueSkill’s core algorithm is used to update each player's μ and σ based on the adjusted performance scores. TrueSkill accounts for:
    - **The probability of the match outcome** based on pre-match μ and σ values.
    - **Surprising outcomes**: If a low-ranked player defeats a high-ranked player or if a team with lower combined skill wins, TrueSkill will adjust ratings more drastically.

  The Bayesian update further refines each player’s skill distribution, gradually reducing **σ** as more matches are played, making the system more confident in the player’s rating over time.

#### **6. Composite Skill Rating for Matchmaking**
- In team-based games, a **composite team skill rating** is calculated by combining the **μ** values of all players on the team. The TrueSkill system also combines the uncertainties (σ values) to calculate the overall team uncertainty.

  This composite rating ensures that players are matched with teams of similar skill levels, while accounting for the variability in performance (uncertainty). As σ decreases over time, players’ ratings stabilize, leading to more accurate matchmaking.

#### **7. Handling Free-for-All Game Modes**
- In Free-for-All (FFA) games like Battle Royale, TrueSkill handles multiple players by evaluating each player’s **placement** in the match. Players who survive longer or place higher are ranked better than those who are eliminated early.
    - **FFA-specific adjustments**: Performance metrics (e.g., kills, damage dealt, survival time) are factored into the ranking to account for individual contributions beyond just final placement.

#### **8. Contextual Factors and Multi-Factor Adjustments**
- The system supports adjustments for **contextual factors** like:
    - **Map difficulty**: Harder maps may increase the uncertainty (σ) adjustment.
    - **Opponent strength**: Matches against significantly stronger opponents may result in smaller μ reductions for losses and larger μ gains for wins.
    - **Environmental factors**: Random events (e.g., weather conditions, game-specific modifiers) can also affect the rating adjustments, allowing the system to reflect the difficulty of different match conditions.

#### **9. Dynamic Matchmaking and Leaderboards**
- **Matchmaking**: Players with similar μ and σ values are matched together to ensure balanced games. The system can also use **role-based matchmaking** to create teams with complementary skills (e.g., pairing strong damage dealers with strong healers).

- **Leaderboards**: Players are ranked based on their **μ** value. The system can support **role-specific leaderboards**, allowing players to see how they rank within their specific role (e.g., top Tanks, top Healers, etc.).

- **Rating Decay**: For players who become inactive, their **σ** may gradually increase, reflecting growing uncertainty about their current skill level. This allows the system to recalibrate their rating when they return to active play.

---

### Key Benefits of This Algorithm
1. **Fairer Ratings**: Players are rewarded based on both match outcomes and their individual role-based performance, leading to more accurate skill estimates.
2. **Adaptability**: The system can handle various game types, including team-based and Free-for-All modes, with flexibility for different performance metrics.
3. **Accurate Matchmaking**: TrueSkill’s Bayesian approach, combined with performance-based adjustments, ensures players are matched with similarly skilled opponents.
4. **Contextual Sensitivity**: The system can factor in contextual elements such as map difficulty and opponent strength, providing a nuanced view of player performance.
5. **Player Engagement**: By integrating role-specific performance, players receive feedback that’s directly tied to how well they played their role, rather than just whether they won or lost.

---

This advanced algorithm provides a comprehensive approach to player skill evaluation and matchmaking, creating a fair, flexible, and adaptive system that accurately reflects both team and individual contributions in competitive games.