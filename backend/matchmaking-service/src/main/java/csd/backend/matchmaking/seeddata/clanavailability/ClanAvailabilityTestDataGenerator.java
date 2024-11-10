package csd.backend.matchmaking.seeddata.clanavailability;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ClanAvailabilityTestDataGenerator {

    private static final int NUMBER_OF_CLANS = 100; // Number of unique clans
    private static final int NUMBER_OF_TOURNAMENTS = 5; // Number of tournaments
    private static final int MIN_AVAILABILITIES_PER_CLAN = 10; // Minimum availability records per clan
    private static final String CSV_FILE_PATH = "matchmaking-service/src/main/resources/seed_data/clan_availability.csv";

    private static final int MIN_START_HOUR = 8; // Minimum start hour (e.g., 8 AM)
    private static final int MAX_END_HOUR = 22; // Maximum end hour (e.g., 10 PM)

    public static void main(String[] args) {
        try {
            System.out.println("Starting clan data generation...");
            generateClanTestData(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error occurred during clan data generation.");
        }
    }

    private static void generateClanTestData(boolean skip) throws IOException {
        if (skip) {
            System.out.println("Skipping clan availability data load.");
            return;
        }

        // Print the file path for verification
        System.out.println("Writing to file: " + new java.io.File(CSV_FILE_PATH).getAbsolutePath());

        // Open the BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, false))) {
            System.out.println("File opened successfully.");

            // Write CSV header
            writer.write("clanId,tournamentId,startTime,endTime,isAvailable\n");
            System.out.println("Header written to file.");

            Random random = new Random();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

            // Generate clan data
            for (long clanId = 1; clanId <= NUMBER_OF_CLANS; clanId++) {
                for (long tournamentId = 1; tournamentId <= NUMBER_OF_TOURNAMENTS; tournamentId++) {
                    OffsetDateTime baseDate = OffsetDateTime.now(ZoneOffset.UTC).plusDays(random.nextInt(30));
                    Set<Integer> usedHours = new HashSet<>();
                    int availabilityCount = 0;

                    // Ensure a minimum of 5 availability records with random hours
                    while (availabilityCount < MIN_AVAILABILITIES_PER_CLAN) {
                        int randomHour = MIN_START_HOUR + random.nextInt(MAX_END_HOUR - MIN_START_HOUR);

                        // Avoid duplicate time slots for the same clan and tournament
                        if (usedHours.contains(randomHour)) {
                            continue;
                        }

                        usedHours.add(randomHour);
                        OffsetDateTime startTime = baseDate.withHour(randomHour).withMinute(0).withSecond(0).withNano(0);
                        OffsetDateTime endTime = startTime.plusHours(1);

                        // Mark as available and increment count
                        writer.write(String.format("%d,%d,%s,%s,%b\n",
                                clanId, tournamentId, startTime.format(formatter), endTime.format(formatter), true));
                        availabilityCount++;
                    }
                }
            }

            writer.flush();
            System.out.println("Clan data generation complete, file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error occurred while writing the file.");
        }
    }
}
