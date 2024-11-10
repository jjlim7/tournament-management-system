package csd.backend.matchmaking.seeddata.playeravailability;

import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import csd.backend.matchmaking.services.PlayerAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PlayerAvailabilityService playerAvailabilityService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadPlayerAvailabilities(false);
    }

    private void loadPlayerAvailabilities(boolean skip) throws Exception {
        if (skip) {
            System.out.println("Skipping player availability data load.");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resourceLoader.getResource("classpath:seed_data/player_availability.csv").getInputStream(), StandardCharsets.UTF_8));

        // Define interval in hours (adjust if necessary)
        long intervalInHours = 1;

        // Read CSV lines, skip header, and save data to the repository
        reader.lines().skip(1).forEach(line -> {
            try {
                String[] fields = line.split(",");
                long playerId = Long.parseLong(fields[0]);
                long tournamentId = Long.parseLong(fields[1]);
                OffsetDateTime startTime = OffsetDateTime.parse(fields[2], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                OffsetDateTime endTime = OffsetDateTime.parse(fields[3], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                boolean isAvailable = Boolean.parseBoolean(fields[4]);

                // Create player availability records based on intervals within the range
                OffsetDateTime currentStartTime = startTime;
                while (currentStartTime.isBefore(endTime)) {
                    OffsetDateTime intervalEndTime = currentStartTime.plusHours(intervalInHours);
                    if (intervalEndTime.isAfter(endTime)) {
                        intervalEndTime = endTime;
                    }

                    PlayerAvailability availability = new PlayerAvailability(
                            playerId, tournamentId, currentStartTime, intervalEndTime, isAvailable
                    );
                    playerAvailabilityService.createAvailability(availability);

                    currentStartTime = intervalEndTime;
                }

            } catch (Exception e) {
                System.err.println("Error processing line: " + line + ". Error: " + e.getMessage());
            }
        });

        reader.close();
    }
}
