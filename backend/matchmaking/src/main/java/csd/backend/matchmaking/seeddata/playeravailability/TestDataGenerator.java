package csd.backend.matchmaking.seeddata.playeravailability;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TestDataGenerator {

    private static final int NUMBER_OF_PLAYERS = 100; // Number of unique players
    private static final int MIN_AVAILABILITIES_PER_PLAYER = 5; // Maximum availability records per player
    private static final String CSV_FILE_PATH = "src/main/resources/seed_data/player_availability.csv";

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

        long tournamentId = 1;

        // Generate a random date within the next 30 days
        OffsetDateTime date = OffsetDateTime.now(ZoneOffset.UTC).plusDays((long) (Math.random() * 30));

        for (long playerId = 1; playerId <= NUMBER_OF_PLAYERS; playerId++) {
            // Randomly select the player's availability window
            int availabilityStartHour = MIN_START_HOUR + random.nextInt(MAX_END_HOUR - MIN_START_HOUR - 1);
            int availabilityEndHour = availabilityStartHour + 2 + random.nextInt((MAX_END_HOUR - availabilityStartHour) / 2 + 1);

            int availabilityCount = 0;

            // Generate full range of availability windows for the player
            for (int hour = MIN_START_HOUR; hour < MAX_END_HOUR; hour += 2) {
                OffsetDateTime startTime = date.withHour(hour).withMinute(0).withSecond(0).withNano(0);
                OffsetDateTime endTime = startTime.plusHours(2);

                // Check if this time slot is within the player's availability window
                boolean isAvailable = (hour >= availabilityStartHour && hour < availabilityEndHour);

                // Ensure the player has at least the minimum number of availability records
                if (availabilityCount < MIN_AVAILABILITIES_PER_PLAYER) {
                    isAvailable = true; // Force availability to true if below minimum
                }

                if (isAvailable) {
                    availabilityCount++;
                }

                writer.write(String.format("%d,%d,%s,%s,%b\n", playerId, tournamentId, startTime.format(formatter), endTime.format(formatter), isAvailable));
            }
        }

        writer.close();
        System.out.println("Test data generated and saved to " + CSV_FILE_PATH);
    }
}
