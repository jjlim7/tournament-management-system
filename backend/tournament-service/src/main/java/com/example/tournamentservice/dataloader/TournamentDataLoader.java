package com.example.tournamentservice.dataloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.entity.Tournament.GameMode;
import com.example.tournamentservice.entity.Tournament.Status;
import com.example.tournamentservice.repository.TournamentRepository;

@Service
public class TournamentDataLoader implements CommandLineRunner{

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ResourceLoader resourceLoader;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // loadTournamentsFromCsv();
    }

    @Transactional
    public void loadTournamentsFromCsv() throws Exception {
    // Check if the resource can be found
    Resource resource = resourceLoader.getResource("classpath:seed_data/tournaments.csv");
    if (!resource.exists()) {
        System.err.println("CSV file not found: " + resource.getFilename());
        throw new Exception("CSV file not found: " + resource.getFilename());
    }
    
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            resource.getInputStream(), StandardCharsets.UTF_8))) {

        // Skip the header
        reader.readLine();

        // Read each line of the CSV file
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");

            // Create a new Tournament entity from the CSV data
            Tournament tournament = new Tournament();
            tournament.setName(fields[0]);
            tournament.setGameMode(GameMode.valueOf(fields[1])); // Convert to GameMode enum
            tournament.setStatus(Status.valueOf(fields[2]));
            tournament.setStartDate(OffsetDateTime.parse(fields[3], DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            tournament.setEndDate(OffsetDateTime.parse(fields[4], DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            tournament.setPlayerCapacity(Integer.parseInt(fields[5]));
            tournament.setAdminId(Long.parseLong(fields[6]));

            tournament.setPlayerIds(null);
//            tournament.setGameList(null);

            // Save the tournament to the database
            tournamentRepository.save(tournament);
        }
    }
}

}

