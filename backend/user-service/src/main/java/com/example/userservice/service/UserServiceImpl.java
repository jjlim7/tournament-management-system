package com.example.userservice.service;

import java.util.*;

import com.example.userservice.controller.UserController;
import com.example.userservice.feignclient.EloRankingClient;
import com.example.userservice.feigndto.ClanEloRank;
import com.example.userservice.feigndto.GameMode;
import com.example.userservice.feigndto.PlayerEloRank;
import com.example.userservice.feigndto.PlayerStats;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service; // to be able to use the @Service annotation

import com.example.userservice.entity.*;
import com.example.userservice.repository.*;

import jakarta.transaction.Transactional;



@Service 
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userDB;

    @Autowired
    private EloRankingClient eloRankingClient;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public UserServiceImpl(UserRepository userDB, EloRankingClient eloRankingClient, ObjectMapper objectMapper) {
        this.userDB = userDB;
        this.eloRankingClient = eloRankingClient;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public List<User> listAllUsers() {
        return userDB.findAll();
    }

    @Override
    @Transactional
    public User addUser(User user) {

        List<User> sameUsersList = userDB.findByEmail(user.getEmail());

        if(sameUsersList.isEmpty()) { // size = 0 means unique user, add him to the DB
            return userDB.save(user);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public User getUser(Long userId) {
        /* findById() is of type optional */
        return userDB.findById(userId).orElse(null);

    }

    @Override
    @Transactional
    public User updateUser(Long userId, User newUser) {
        Optional<User> optionalUser = userDB.findById(userId);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setName(newUser.getName());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            user.setRole(newUser.getRole());
            
            return userDB.save(user); // need to save the updates in the DB

        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> optionalUser = userDB.findById(userId);

        if(optionalUser.isPresent()) {
            userDB.deleteById(userId);
        }

    }

    @Override
    @Transactional
    public PlayerEloRank getLatestPlayerRank(Long playerId) {
        Optional<User> optionalUser = userDB.findById(playerId);
        if (optionalUser.isEmpty()) {
            return null;
        }

        ResponseEntity<Map<String, Object>> resp = eloRankingClient.getPlayerLatestRank(playerId);
        log.info("getLatestPlayerRank response: {}", resp.getBody());

        if (!resp.getStatusCode().is2xxSuccessful()) {
            return null;
        }

        // Extract and convert the "data" field to a PlayerEloRank object
        Map<String, Object> data = (Map<String, Object>) resp.getBody().get("data");
        return objectMapper.convertValue(data, PlayerEloRank.class);
    }

    @Override
    @Transactional
    public PlayerOverallStats getPlayerOverallForLatestTournament(Long playerId){
        Optional<User> optionalUser = userDB.findById(playerId);
        if(optionalUser.isEmpty()) {
            return null;
        }
        ResponseEntity<Map<String, Object>> resp1 = eloRankingClient.getPlayerLatestRank(playerId);
        log.info("getLatestPlayerRank response: {}", resp1.getBody());
        if (!resp1.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        Map<String, Object> data = (Map<String, Object>) resp1.getBody().get("data");
        // rank struct
        PlayerEloRank rank = objectMapper.convertValue(data, PlayerEloRank.class);

        // playerStats struct
        ResponseEntity<Map<String, Object>> resp2 = eloRankingClient.getPlayerStatistics(playerId, rank.getTournamentId(), GameMode.BATTLE_ROYALE);
        log.info("getPlayerOverallForLatestTournament response: {}", resp2.getBody());
        if (!resp2.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        Map<String, Object> data1= (Map<String, Object>) resp2.getBody().get("playerStats");

        // player stats struct
        PlayerStats stats = objectMapper.convertValue(data1, PlayerStats.class);

        // clan user
        PlayerOverallStats res = new PlayerOverallStats();
        res.setEloRank(rank);
        res.setStats(stats);

        return res;
    }

 }
