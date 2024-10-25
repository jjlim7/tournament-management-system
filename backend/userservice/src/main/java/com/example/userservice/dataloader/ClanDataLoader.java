package com.example.userservice.dataloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.userservice.entity.Clan;
import com.example.userservice.repository.ClanRepository;

@Component
public class ClanDataLoader implements CommandLineRunner {
    
    @Autowired
    private ClanRepository clanDB;

    @Override
    public void run(String... args) throws Exception {
        loadSeedData();
    }

    private void loadSeedData() {
        if(clanDB.count() == 0) {
            
            
            for(int i = 0; i < 10; i++) {
                Clan clan = new Clan();
                
                clan.setClanName("Clan " + i);
                clanDB.save(clan);
            }

            System.out.println("Seed data for Clan loaded: 10 Clans");
        } else {
            System.out.println("Seed data for Clan already loaded");
        }
    }
}
