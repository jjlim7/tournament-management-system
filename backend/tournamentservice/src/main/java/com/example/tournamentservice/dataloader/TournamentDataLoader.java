package com.example.tournamentservice.dataloader;

import java.time.OffsetDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.entity.Tournament.GameMode;
import com.example.tournamentservice.repository.TournamentRepository;

@Component
public class TournamentDataLoader implements CommandLineRunner {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public void run(String... args) throws Exception {
        loadSeedData();
    }

    private void loadSeedData() {
        if (tournamentRepository.count() == 0) {
            Random random = new Random();
            String[] gameModes = {"BATTLE_ROYALE", "CLAN_WARS"};
    
            for (int i = 1; i <= 10; i++) {  // Creating 10 randomized tournaments
                Tournament tournament = new Tournament();
                tournament.setName("Tournament " + i);
                tournament.setGameMode(GameMode.valueOf(gameModes[random.nextInt(gameModes.length)]));
                // Set start date
                OffsetDateTime startDate = OffsetDateTime.now().plusDays(random.nextInt(30));
                tournament.setStartDate(startDate);
                // Set end date (must be after start date)
                OffsetDateTime endDate = startDate.plusDays(random.nextInt(10) + 1);
                tournament.setEndDate(endDate);
                // Determine status based on start and end dates
                if (startDate.isAfter(OffsetDateTime.now())) {
                    tournament.setStatus(Tournament.Status.INACTIVE);
                } else if (endDate.isBefore(OffsetDateTime.now())) {
                    tournament.setStatus(Tournament.Status.COMPLETED);
                } else {
                    tournament.setStatus(Tournament.Status.ACTIVE);
                }
                tournament.setPlayerCapacity(random.nextInt(100) + 10); // Random capacity between 10 and 110
                tournament.setAdminId(random.nextLong(1, 5));  // Random admin ID between 1 and 5
                tournamentRepository.save(tournament);
            }
    
            System.out.println("Seed data loaded: 10 Tournaments");
        } else {
            System.out.println("Seed data already loaded");
        }
    }    

}