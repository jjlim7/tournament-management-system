package com.example.userservice.dataloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.userservice.entity.*;
import com.example.userservice.repository.*;

import java.util.Random;

@Component
public class UserDataLoader implements CommandLineRunner{

    @Autowired
    private UserRepository userDB;

    @Override
    public void run(String... args) throws Exception {
        loadSeedData();
    }

    private void loadSeedData() {
        if(userDB.count() == 0) {
            Random random = new Random();

            String[] role = {"ROLE_ADMIN", "ROLE_PLAYER"};
            // String[] rank = {"UNRANKED", "IRON", "SILVER", "GOLD", "PLATINIUM", "EMERALD", "DIAMOND", "MASTER", "GRANDMASTER", "CHALLENGER"};

            for(int i = 0; i < 10; i++) {
                User user = new User();

                user.setName("User " + i);
                user.setEmail("user" + i + "@hottestmail.com");
                user.setPassword("abcd" + i);
                user.setRole(role[random.nextInt(role.length)]);
                user.setCountry("Singapore");

                long eloRating = 0 + i*1000;
                user.setElo_rating((Long) eloRating);

                Long currentEloRating = user.getElo_rating();
                user.setRank(assignRankToRating(currentEloRating));

                userDB.save(user);
            }
            System.out.println("Seed data for User loaded: 10 Users");

        } else {
            System.out.println("Seed data for User already loaded");
        }
    }

    public String assignRankToRating(Long eloRating) {
        // String[] rank = {"UNRANKED", "IRON", "SILVER", "GOLD", "PLATINIUM", "EMERALD", "DIAMOND", "MASTER", "GRANDMASTER", "CHALLENGER"};
        
        if(eloRating == 0) {
            return "UNRANKED";
        } else if (eloRating < 1000) {
            return "IRON";
        } else if (eloRating < 2000) {
            return "SILVER";
        } else if (eloRating < 3000) {
            return "GOLD";
        } else if (eloRating < 4000) {
            return "PLATINUM";
        } else if (eloRating < 5000) {
            return "EMERALD";
        } else if (eloRating < 6000) {
            return "DIAMOND";
        } else if (eloRating < 7000) {
            return "MASTER";
        } else if (eloRating < 8500) {
            return "GRANDMASTER";
        } else {
            return "CHALLENGER";
        }

    }
}
