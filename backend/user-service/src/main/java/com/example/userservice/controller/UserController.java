package com.example.userservice.controller;

import com.example.userservice.feigndto.PlayerEloRank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.entity.*;
import com.example.userservice.service.*;
import com.example.userservice.exceptions.*;

import jakarta.transaction.Transactional;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import io.swagger.v3.oas.annotations.Operation;



@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ClanUserService clanUserService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService, ClanUserService clanUserService) {
        this.userService = userService;
        this.clanUserService = clanUserService;
    }

    // Read
    @Operation(summary = "Get all users", description = "Returns a list of users")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.listAllUsers();
    }

    @Operation(summary = "Gets a specific user", description = "Returns a specific user with a specific user id")
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);

        if(user == null) {
            throw new UserNotFoundException(userId);
        } 

        return user;
    }

    // Create
    @Operation(summary = "Add a new user", description = "Returns a new user with the relevant details")
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 - success
    @PostMapping("/users") // post mapping to add the new user to the entire list of users
    @Transactional
    public User addUser(@RequestBody User user) {
        User savedUser = userService.addUser(user);

        if(savedUser == null) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        return savedUser;
    }

    // Update
    @Operation(summary = "Updates a user", description = "Returns the user with updated user details")
    @PutMapping("/users/{userId}") // PUT is to update
    public User updateUserDetails(@PathVariable Long userId, @RequestBody User newUser) {
        User updatedUser = userService.updateUser(userId, newUser);
        
        if(updatedUser == null) {
            throw new UserNotFoundException(userId);
        }

        return updatedUser;
    }

    // Delete
    @Operation(summary = "Deletes a specific user", description = "Deletes users based on their user id")
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        
        try {
            userService.deleteUser(userId);

        } catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(userId);
        }
    }

    // complex ms fn that calls elo-ranking-service
    @Operation(summary = "Get elo rank for specific user", description = "Get latest/current elo ranking of player")
    @GetMapping("/users/{userId}/latest-rank")
    public ResponseEntity<?> getUserEloRank(@PathVariable Long userId) {
        try {
            PlayerEloRank rank = userService.getLatestPlayerRank(userId);
            return ResponseEntity.ok(rank);
        } catch (Exception e) {
            log.error("Failed to retrieve Elo rank for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not retrieve Elo rank at this time.");
        }
    }

    @Operation(summary = "get full user details", description = "")
    @GetMapping("/users/{userId}/overall")
    public ResponseEntity<?> getUserOverall(@PathVariable Long userId) {
        try {
            ClanUser associatedClanDetails = clanUserService.getClanUserByUserId(userId);
            PlayerOverallStats partialStatsNoClanData = userService.getPlayerOverallForLatestTournament(userId);

            partialStatsNoClanData.setClanUser(associatedClanDetails);
            return ResponseEntity.ok(partialStatsNoClanData);
        } catch (Exception e) {
            log.error("Failed to retrieve player details for player: {}", userId, e);

            try {
                // Attempt to retrieve basic user information as a fallback
                User basicUserDetails = userService.getUser(userId);

                System.out.println("fall back," + basicUserDetails.toString());

                Map<String, Object> defaultUser = new HashMap<>();
                defaultUser.put("id", basicUserDetails.getUserId());
                defaultUser.put("name", basicUserDetails.getName() != null ? basicUserDetails.getName() : "");
                defaultUser.put("rank", "UNRANKED");
                defaultUser.put("eloLowerlimit", 0);
                defaultUser.put("currentElo", 0);
                defaultUser.put("eloUpperlimit", 0);
                defaultUser.put("totalWins", 0);
                defaultUser.put("winRatio", 0);
                System.out.println("????");
                defaultUser.put("clanRole", "ROLE_PLAYER");
                defaultUser.put("role", "ROLE_PLAYER");
                defaultUser.put("user", basicUserDetails);

                Map<String, Object> stats = new HashMap<>();
                stats.put("playerId", basicUserDetails.getUserId());
                stats.put("tournamentId", null);
                stats.put("totalMatches", 0);
                stats.put("gameMode", null);
                stats.put("totalKills", 0);
                stats.put("totalDeaths", 0);
                stats.put("avgKillDeathRatio", 0.0);
                stats.put("avgAccuracy", 0.0);
                stats.put("avgHeadshotAccuracy", 0.0);
                stats.put("avgHealingDonePerSecond", 0.0);
                stats.put("avgDamageDonePerSecond", 0.0);
                stats.put("avgDamageTanked", 0.0);
                stats.put("totalAssists", 0);
                stats.put("totalRevives", 0);
                stats.put("totalShotsFired", 0);
                stats.put("totalShotsHit", 0);
                defaultUser.put("stats", stats);

                Map<String, Object> eloRank = new HashMap<>();
                eloRank.put("id", null);
                eloRank.put("rankThresholdId", null);
                eloRank.put("meanSkillEstimate", 0.0);
                eloRank.put("uncertainty", 0.0);
                eloRank.put("tournamentId", null);
                eloRank.put("playerId", basicUserDetails.getUserId());

                Map<String, Object> rankThreshold = new HashMap<>();
                rankThreshold.put("minRating", 0.0);
                rankThreshold.put("maxRating", 0.0);
                rankThreshold.put("rank", null);
                eloRank.put("rankThreshold", rankThreshold);

                defaultUser.put("eloRank", eloRank);

                return ResponseEntity.status(HttpStatus.OK).body(defaultUser);

            } catch (Exception fallbackException) {
                log.error("Failed to retrieve fallback user details for player: {}", userId, fallbackException);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not retrieve user details for player.");
            }
        }
    }

}
