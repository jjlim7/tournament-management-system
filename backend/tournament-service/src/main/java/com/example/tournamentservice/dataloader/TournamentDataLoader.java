package com.example.tournamentservice.dataloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.entity.Tournament.GameMode;
import com.example.tournamentservice.entity.Tournament.Status;
import com.example.tournamentservice.repository.TournamentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TournamentDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(TournamentDataLoader.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Transactional
    public void loadTournamentsFromCsv() {
        Resource resource = resourceLoader.getResource("classpath:seed_data/tournaments.csv");

        if (!resource.exists()) {
            logger.error("CSV file not found: {}", resource.getFilename());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); // Skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                
                if (fields.length < 7) {
                    logger.warn("Skipping line due to incorrect number of fields: {}", line);
                    continue;
                }

                try {
                    Tournament tournament = new Tournament();
                    tournament.setName(fields[0]);
                    tournament.setGameMode(GameMode.valueOf(fields[1].toUpperCase()));
                    tournament.setStatus(Status.valueOf(fields[2].toUpperCase()));
                    tournament.setStartDate(OffsetDateTime.parse(fields[3], DATE_FORMATTER));
                    tournament.setEndDate(OffsetDateTime.parse(fields[4], DATE_FORMATTER));
                    tournament.setPlayerCapacity(Integer.parseInt(fields[5]));
                    tournament.setAdminId(Long.parseLong(fields[6]));
                    
                    // Additional attributes (if nullable, no need to set)
                    tournament.setPlayerIds(null);
                    tournament.setGameList(null);

                    // Check for duplicates based on tournament name
                    if (!tournamentRepository.existsByNameIgnoreCase(tournament.getName())) {
                        tournamentRepository.save(tournament);
                        logger.info("Saved tournament: {}", tournament.getName());
                    } else {
                        logger.info("Tournament already exists: {}", tournament.getName());
                    }

                } catch (IllegalArgumentException | DateTimeParseException e) {
                    logger.error("Error parsing line: {}. Skipping line.", line, e);
                }
            }
        } catch (Exception e) {
            logger.error("Error reading CSV file", e);
        }
    }
}
