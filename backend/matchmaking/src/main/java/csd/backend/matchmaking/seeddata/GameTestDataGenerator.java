package csd.backend.matchmaking.seeddata;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GameTestDataGenerator {

    private static final String CSV_HEADER = "tournament_id,game_status,game_mode,start_time,end_time";
    private static final String[] GAME_STATUSES = {"SCHEDULED", "IN_PROGRESS", "COMPLETED"};
    private static final String[] GAME_MODES = {"BATTLE_ROYALE", "CLAN_WAR"};
    private static final int NUM_GAMES = 100;  // Number of games to generate
    private static final String CSV_FILE_PATH = "src/main/resources/seed_data/games.csv";
    private static final Random random = new Random();

    public static void main(String[] args) {
        generateCsvFile(CSV_FILE_PATH);
    }

    public static void generateCsvFile(String filePath) {
        try {
            // Create directories if not exists
            Files.createDirectories(Paths.get(filePath).getParent());

            long tournamentId = 1;

            // Create the CSV file and write the header
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.append(CSV_HEADER).append("\n");

                for (int i = 0; i < NUM_GAMES; i++) {
                    String gameStatus = GAME_STATUSES[random.nextInt(GAME_STATUSES.length)];
                    String gameMode = GAME_MODES[random.nextInt(GAME_MODES.length)];

                    // Generate random start and end times (intervals of 2 hours)
                    OffsetDateTime startTime = generateRandomStartTime();
                    OffsetDateTime endTime = startTime.plusHours(2);

                    // Write the generated game data to CSV
                    writer.append(String.valueOf(tournamentId))
                            .append(',')
                            .append(gameStatus)
                            .append(',')
                            .append(gameMode)
                            .append(',')
                            .append(startTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                            .append(',')
                            .append(endTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                            .append('\n');
                }
            }

            System.out.println("games.csv generated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate a random start time (within a realistic range)
    private static OffsetDateTime generateRandomStartTime() {
        // Fixed date range for games (between 2024-09-01 and 2024-09-30)
        int startDay = random.nextInt(30) + 1;
        int startHour = random.nextInt(12) + 8; // Random hour between 8 AM and 8 PM

        // Start time on a specific day in the range
        return OffsetDateTime.of(2024, 9, startDay, startHour, 0, 0, 0, ZoneOffset.ofHours(8));
    }
}

