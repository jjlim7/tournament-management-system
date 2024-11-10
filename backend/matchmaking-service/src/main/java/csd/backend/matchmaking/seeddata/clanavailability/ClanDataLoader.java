package csd.backend.matchmaking.seeddata.clanavailability;

import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.repository.ClanAvailabilityRepository;
import csd.backend.matchmaking.services.ClanAvailabilityService;
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

@Component
public class ClanDataLoader implements CommandLineRunner {

    @Autowired
    private ClanAvailabilityService clanAvailabilityService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadClanAvailabilities(true);
    }

    private void loadClanAvailabilities(boolean skip) throws Exception {
        if (skip) {
            System.out.println("Skipping clan availability data load.");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resourceLoader.getResource("classpath:seed_data/clan_availability.csv").getInputStream(), StandardCharsets.UTF_8));

        // Define interval in hours (adjust if necessary)
        long intervalInHours = 1;

        // Read CSV lines, skip header, and save data to the repository
        reader.lines().skip(1).forEach(line -> {
            try {
                String[] fields = line.split(",");
                long clanId = Long.parseLong(fields[0]);
                long playerId = Long.parseLong(fields[1]);  // Parse playerId from the CSV
                long tournamentId = Long.parseLong(fields[2]);
                OffsetDateTime startTime = OffsetDateTime.parse(fields[3], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                OffsetDateTime endTime = OffsetDateTime.parse(fields[4], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                boolean isAvailable = Boolean.parseBoolean(fields[5]);

                // Create clan availability records based on intervals within the range
                OffsetDateTime currentStartTime = startTime;
                while (currentStartTime.isBefore(endTime)) {
                    OffsetDateTime intervalEndTime = currentStartTime.plusHours(intervalInHours);
                    if (intervalEndTime.isAfter(endTime)) {
                        intervalEndTime = endTime;
                    }

                    // Create ClanAvailability with playerId included
                    ClanAvailability availability = new ClanAvailability(
                            clanId, playerId, tournamentId, currentStartTime, intervalEndTime, isAvailable
                    );
                    clanAvailabilityService.createClanAvailability(availability);

                    currentStartTime = intervalEndTime;
                }

            } catch (Exception e) {
                System.err.println("Error processing line: " + line + ". Error: " + e.getMessage());
            }
        });

        reader.close();
    }

}
