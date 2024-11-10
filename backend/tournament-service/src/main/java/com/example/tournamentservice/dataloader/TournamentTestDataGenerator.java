package com.example.tournamentservice.dataloader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TournamentTestDataGenerator {

    private static final String CSV_FILE_PATH = "backend/tournament-service/src/main/resources/seed_data/tournaments.csv";
    private static final String CSV_HEADER = "name,game_mode,status,start_date,end_date,player_capacity,admin_id,playlist,gamelist";
    private static final String[] GAME_MODES = {"BATTLE_ROYALE", "CLAN_WAR"};
    private static final int NUMBER_OF_TOURNAMENTS = 50;
    private static final int MAX_PLAYER_CAPACITY = 110;
    private static final int MIN_PLAYER_CAPACITY = 10;
    private static final int MAX_ADMIN_ID = 5;
    private static final int MAX_DAYS_OFFSET = 30; // Max days offset for start date
    private static final int MAX_HOURS_OFFSET = 12; // Limit start hour
    private static final int MIN_HOURS_OFFSET = 8; // Start no earlier than 8 AM
    private static final Random random = new Random();
    
    private static final OffsetDateTime REFERENCE_DATE = OffsetDateTime.parse("2024-11-01T00:00:00+08:00");

    public void generateData() {
        try {
            generateTournamentData();
            System.out.println("tournaments.csv generated successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        TournamentTestDataGenerator generator = new TournamentTestDataGenerator();
        generator.generateData();
    }

    private void generateTournamentData() throws IOException {
        Files.createDirectories(Paths.get(CSV_FILE_PATH).getParent());
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write(CSV_HEADER + "\n");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime currentDate = OffsetDateTime.now();
    
            for (int i = 1; i <= NUMBER_OF_TOURNAMENTS; i++) {
                String name = "Tournament " + i;
                String gameMode = GAME_MODES[random.nextInt(GAME_MODES.length)];
    
                OffsetDateTime startDate = generateRandomStartDate();
                OffsetDateTime endDate = generateRandomEndDate(startDate);
    
                String status = determineStatus(startDate, endDate, currentDate);
    
                // Player capacity logic: Ensure it's valid for "CLAN_WAR" mode
                int playerCapacity = generateValidPlayerCapacity(gameMode);
    
                long adminId = random.nextInt(MAX_ADMIN_ID) + 1;
    
                writer.write(String.format("%s,%s,%s,%s,%s,%d,%d,null,null\n",
                        name, gameMode, status,
                        startDate.format(formatter),
                        endDate.format(formatter),
                        playerCapacity,
                        adminId));
            }
        }
    }
    
    // Ensure that the player capacity for CLAN_WAR is more than 10 and divisible by 10
    private int generateValidPlayerCapacity(String gameMode) {
        int playerCapacity;
    
        if ("CLAN_WAR".equals(gameMode)) {
            // For "CLAN_WAR", the player capacity must be > 10 and divisible by 10
            do {
                playerCapacity = random.nextInt(MAX_PLAYER_CAPACITY - MIN_PLAYER_CAPACITY + 1) + MIN_PLAYER_CAPACITY;
            } while (playerCapacity <= 10 || playerCapacity % 10 != 0);
        } else {
            // For other game modes, use a normal capacity within the range
            playerCapacity = random.nextInt(MAX_PLAYER_CAPACITY - MIN_PLAYER_CAPACITY + 1) + MIN_PLAYER_CAPACITY;
        }
    
        return playerCapacity;
    }
    
    

    private OffsetDateTime generateRandomStartDate() {
        int startDayOffset = random.nextInt(MAX_DAYS_OFFSET);
        int startHour = random.nextInt(MAX_HOURS_OFFSET - MIN_HOURS_OFFSET + 1) + MIN_HOURS_OFFSET;

        return REFERENCE_DATE
                .plusDays(startDayOffset)
                .withHour(startHour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    private OffsetDateTime generateRandomEndDate(OffsetDateTime startDate) {
        return startDate.plusDays(random.nextInt(10) + 1);
    }

    private String determineStatus(OffsetDateTime startDate, OffsetDateTime endDate, OffsetDateTime currentDate) {
        if (startDate.isBefore(REFERENCE_DATE)) {
            return "INACTIVE";
        } else if (!currentDate.isBefore(startDate) && currentDate.isBefore(endDate)) {
            return "ACTIVE";
        } else if (currentDate.isAfter(endDate)) {
            return "COMPLETED";
        } else {
            return "INACTIVE";
        }
    }
}
