package com.example.userservice.dataloader;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.userservice.repository.*;
import com.example.userservice.entity.*;

@Component
public class ClanUserDataLoader implements CommandLineRunner {
    @Autowired
    private ClanUserRepository clanUserDB;

    @Autowired
    private ClanRepository clanDB;

    @Autowired
    private UserRepository userDB;

    @Override
    public void run(String... args) throws Exception {
        loadSeedData();
    }

    private void loadSeedData() {
        if(clanUserDB.count() == 0) {
            
            Random random = new Random();
            Boolean[] clanLeader = {true, false};
            String[] position = {"MAGE", "JUNGLER", "HEALER", "DAMAGE", "TANK"};

            List<User> users = userDB.findAll();
            List<Clan> clans = clanDB.findAll();

            for(int i = 0; i < 10; i++) {
                ClanUser clanUser = new ClanUser();

                clanUser.setUser(users.get(i)); // Hopefully this does not throw IndexOutOfBoundsException
                clanUser.setClan(clans.get(i));
                clanUser.setIsClanLeader(clanLeader[random.nextInt(clanLeader.length)]);
                clanUser.setPosition(position[random.nextInt(position.length)]);

                clanUserDB.save(clanUser);
            }
        
            System.out.println("Seed data for Clan Users loaded: 10 Clan Users");
        } else {
            System.out.println("Seed data for Clan Users already loaded");
        }
    }
}
