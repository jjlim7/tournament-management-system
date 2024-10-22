package com.example.tournamentservice.dataloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.entity.Tournament.GameMode;
import com.example.tournamentservice.repository.TournamentRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

@Component
public class TournamentDataLoader implements CommandLineRunner {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public void run(String... args) throws Exception {
        loadSeedData();
    }

    private void loadSeedData(){
        if(tournamentRepository.count() == 0) {
            Random random = new Random();
            String[] gameModes = {"BATTLE ROYALE", "CLAN WARS"};
            String[] statuses = {"UPCOMING", "ONGOING", "COMPLETED"};

            for (int i = 1; i <= 10; i++) {  // Creating 10 randomized tournaments
                Tournament tournament = new Tournament();
                tournament.setName("Tournament " + i);
                tournament.setGameMode(GameMode.valueOf(gameModes[random.nextInt(gameModes.length)]));
                tournament.setStatus(Tournament.Status.valueOf(statuses[random.nextInt(statuses.length)]));
                tournament.setStartDate(OffsetDateTime.now().plusDays(random.nextInt(30)));
                tournament.setEndDate(tournament.getStartDate().plusDays(random.nextInt(10) + 1));
                tournament.setPlayerCapacity(random.nextInt(100) + 10); // Random capacity between 10 and 110
                tournament.setAdminId(random.nextLong(1, 5));  // Random admin ID between 1 and 5
                tournamentRepository.save(tournament);
            }

            System.out.println("Seed data loaded: 10 Tournaments");
        }
        else {
            System.out.println("Seed data already loaded");
        }
    }

}