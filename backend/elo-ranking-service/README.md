

# **Elo Ranking Service: Advanced Elo Algorithm with TrueSkill Integration**

## **Abstract**

This document presents an advanced **Elo Ranking Service** algorithm that integrates **Microsoft’s TrueSkill system** with **role-specific performance metrics** and dynamic adjustments based on player contributions. The algorithm is designed to accommodate both **team-based games** (e.g., Clan Wars) and **Free-for-All games** (e.g., Battle Royale). The system provides a fair and accurate ranking mechanism by considering not only match outcomes but also individual player performance relative to their role and match context. This paper outlines the algorithm's methodology, its performance score computation, and the subsequent updates to player ratings.

---

## **Introduction**

Traditional Elo rating systems are based solely on the win/loss outcomes of matches, assuming that each player contributes equally to the final result. However, in team-based and Free-for-All games, this assumption is often inaccurate. Players assume different roles and perform specific tasks that may not directly impact the match's final outcome but are crucial to the overall team's success. To address this, we have integrated role-specific performance metrics into the TrueSkill algorithm, allowing us to evaluate each player's contribution to the match more accurately.

This paper explains the various components of the algorithm, from the role-specific performance metrics to the calculation of **Performance-Based Outcomes (PBO)** and adjustments to player ratings. The algorithm aims to improve upon traditional Elo systems by accounting for nuanced individual contributions, player roles, and the uncertainty in skill estimates.

---

## **TrueSkill and Initial Ratings**

Each player in the system starts with an initial TrueSkill rating consisting of:

- **μ (Mean Skill Estimate):** Represents the player’s perceived skill level.
- **σ (Uncertainty):** Indicates the system’s confidence in the player’s skill level. A high σ reflects greater uncertainty, which is typical for new players or players with few matches.

These initial ratings form the foundation for the system's updates after each match, with σ gradually decreasing as the system becomes more confident in a player’s skill through more match data.

---

## **Role-Specific Performance Metrics (RPS)**

The system accounts for the player's role within the game and uses **Role-Specific Performance (RPS)** to gauge individual contributions to the match. For example:

- **Damage Dealers:** Metrics include Kill/Death Ratio (KDR), Accuracy, Damage Per Second (DPS), and Headshot Accuracy.
- **Tanks:** Metrics include Damage Mitigated, Healing Done, and Assists.
- **Healers:** Metrics include Total Healing, Revives, and Assists.

Each role has a predefined weight assigned to its metrics, and the **Role Performance Score (RPS)** is calculated based on these weights. This RPS is a crucial factor in determining how much a player's performance impacts their rating adjustment after a match.

### **Example of Role-Based Weights:**
| Role          | Metric         | Weight |
|---------------|----------------|--------|
| Damage Dealer | KDR            | 0.4    |
| Damage Dealer | Accuracy        | 0.2    |
| Tank          | Damage Mitigated| 0.5    |
| Healer        | Healing Done    | 0.6    |

---

## **Performance-Based Outcome (PBO)**

The algorithm uses the **Performance-Based Outcome (PBO)** to adjust the player's rating based on their individual contributions within a match. The PBO formula integrates the match result (win/loss/placement) with the normalized RPS of each player, ensuring that players are rewarded for their specific role-based performance.

### **PBO Formula:**
```
PBO_i = Match Outcome × (1 + α × Normalized RPS_i)
```
- **Match Outcome:** For team-based games, this is 1 for a win and 0 for a loss. For Battle Royale, it is a placement-based score ranging from 0 to 1.
- **α (Alpha):** A scaling factor that adjusts the influence of RPS, capped at a defined maximum to prevent over-amplification of performance differences.
- **Normalized RPS:** The RPS of the player normalized using Z-score normalization.

### **Z-Score Normalization Formula:**
```
Z_RPS_i = (RPS_i - μ_RPS) / σ_RPS
```
- **RPS_i:** The Role Performance Score of player _i_.
- **μ_RPS:** The average RPS of all players in the match.
- **σ_RPS:** The standard deviation of the RPS values in the match.

This normalization ensures that performance metrics are comparable across different matches and roles, distributing player performance around a mean of 0.

---

## **Updating Player Ratings: Adjusting μ and σ**

Once the PBO is calculated, the player's ratings are updated as follows:

- **Mean Skill Estimate (μ):** Adjusted based on the difference between the **Performance-Based Outcome (PBO)** and the player's **Expected Performance (E_i)**, which is calculated using the player's pre-match rating.
  ```
  New μ = Old μ + K × (PBO - E_i)
  ```
  - **K:** A constant that controls the sensitivity of the updates.
  - **E_i:** The player's expected performance, calculated using their pre-match rating and the ratings of their opponents.

- **Uncertainty (σ):** Adjusted based on the surprise factor of the player's performance. If the player performed unexpectedly well or poorly, the uncertainty is adjusted to reflect this deviation from expectations.
  ```
  New σ = sqrt(1 / (σ^2 + (1 / v_i))) × (1 - λ)
  ```
  - **λ (Lambda):** A dynamic factor that adjusts uncertainty based on the performance surprise factor.
  - **v_i:** Variance based on expected performance.

These updates ensure that the system adapts to each player’s performance dynamically while gradually reducing the uncertainty (σ) as more matches are played.

---

## **TrueSkill Bayesian Updates**

The system applies **TrueSkill's Bayesian update** to further refine the μ and σ values for each player. TrueSkill’s core algorithm ensures that ratings are updated based on both the match outcome and the pre-match rating distribution. The algorithm also handles **surprise outcomes**, adjusting ratings more significantly when players perform well against highly ranked opponents or vice versa.

---

## **Conclusion**

This enhanced Elo Ranking Service provides a **fairer** and **more accurate** method of ranking players by integrating **role-specific performance metrics**, **TrueSkill’s Bayesian updates**, and **multi-factor adjustments**. By rewarding players based on individual contributions and incorporating performance-based outcome calculations, this system delivers a more nuanced approach to skill evaluation, ensuring better matchmaking and an engaging player experience across different game modes.

This algorithm is well-suited for both **team-based games** and **Free-for-All games**, offering flexibility for different roles and gameplay scenarios. The inclusion of **Z-score normalization** and **uncertainty management** further enhances the system’s accuracy and adaptability, making it a powerful tool for competitive multiplayer gaming environments.

--- 
