package csd.backend.matchmaking.seeddata.playeravailability;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestDataGenerator {

    private static final int NUMBER_OF_TOURNAMENTS = 5;
    private static final int NUMBER_OF_PLAYERS = 100; // Number of unique players
    private static final int MIN_AVAILABILITIES_PER_PLAYER = 5; // Maximum availability records per player
    private static final String CSV_FILE_PATH = "matchmaking-service/src/main/resources/seed_data/player_availability.csv";

    private static final int MIN_START_HOUR = 8; // Minimum start hour (e.g., 8 AM)
    private static final int MAX_END_HOUR = 22; // Maximum start hour (e.g., 10 PM)

    public static void main(String[] args) {
        try {
            generateTestData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateTestData() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH));
        writer.write("playerId,tournamentId,startTime,endTime,isAvailable\n"); // CSV Header

        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        for (long playerId = 1; playerId <= NUMBER_OF_PLAYERS; playerId++) {
            for (long tournamentId = 1; tournamentId <= NUMBER_OF_TOURNAMENTS; tournamentId++) {
                OffsetDateTime baseDate = OffsetDateTime.now(ZoneOffset.UTC).plusDays(random.nextInt(30));
                Set<Integer> usedHours = new HashSet<>();
                int availabilityCount = 0;

                // Ensure a minimum of 5 availability records with random hours
                while (availabilityCount < MIN_AVAILABILITIES_PER_PLAYER) {
                    int randomHour = MIN_START_HOUR + random.nextInt(MAX_END_HOUR - MIN_START_HOUR);

                    // Avoid duplicate time slots for the same player and tournament
                    if (usedHours.contains(randomHour)) {
                        continue;
                    }

                    usedHours.add(randomHour);
                    OffsetDateTime startTime = baseDate.withHour(randomHour).withMinute(0).withSecond(0).withNano(0);
                    OffsetDateTime endTime = startTime.plusHours(1);

                    // Mark as available and increment count
                    writer.write(String.format("%d,%d,%s,%s,%b\n",
                            playerId, tournamentId, startTime.format(formatter), endTime.format(formatter), true));
                    availabilityCount++;
                }
            }
        }

        writer.close();
        System.out.println("Test data generated and saved to " + CSV_FILE_PATH);
    }
}
