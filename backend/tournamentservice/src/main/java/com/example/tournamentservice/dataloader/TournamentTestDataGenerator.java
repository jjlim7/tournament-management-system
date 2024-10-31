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

    private static final String CSV_FILE_PATH = "backend/tournamentservice/src/main/resources/seed_data/tournaments.csv";
    private static final String CSV_HEADER = "name,game_mode,status,start_date,end_date,player_capacity,admin_id,playlist,gamelist";
    private static final String[] GAME_MODES = {"BATTLE_ROYALE", "CLANWAR"};
    private static final int NUMBER_OF_TOURNAMENTS = 20; // Number of tournaments to generate
    private static final int MAX_PLAYER_CAPACITY = 110;
    private static final int MIN_PLAYER_CAPACITY = 10;
    private static final int MAX_ADMIN_ID = 5;
    private static final int MAX_DAYS_OFFSET = 30; // Maximum days from now for start date
    private static final int MAX_HOURS_OFFSET = 12; // Limit hour of day for start time
    private static final int MIN_HOURS_OFFSET = 8; // Start no earlier than 8 AM
    private static final Random random = new Random();

    public static void main(String[] args) {
        try {
            generateTournamentData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateTournamentData() throws IOException {
        // Ensure the directories exist
        Files.createDirectories(Paths.get(CSV_FILE_PATH).getParent());
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write CSV header with fields
            writer.write(CSV_HEADER + "\n");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    
            for (int i = 1; i <= NUMBER_OF_TOURNAMENTS; i++) {
                String name = "Tournament " + i;
                String gameMode = GAME_MODES[random.nextInt(GAME_MODES.length)];
        
                // Generate start and end dates
                OffsetDateTime startDate = generateRandomStartDate();
                OffsetDateTime endDate = startDate.plusDays(random.nextInt(10) + 1);
        
                // Determine status based on start and end dates
                String status;
                if (startDate.isAfter(OffsetDateTime.now())) {
                    status = "INACTIVE";
                } else if (endDate.isBefore(OffsetDateTime.now()) && !startDate.isAfter(OffsetDateTime.now())) {
                    status = "COMPLETED";
                } else {
                    status = "ACTIVE";
                }
        
                // Random player capacity between 10 and 110
                int playerCapacity = random.nextInt(MAX_PLAYER_CAPACITY - MIN_PLAYER_CAPACITY + 1) + MIN_PLAYER_CAPACITY;
        
                // Random admin ID between 1 and 5
                long adminId = random.nextInt(MAX_ADMIN_ID) + 1;
        
                // Write the generated tournament data to CSV, with null placeholders for playlist and games
                writer.write(String.format("%s,%s,%s,%s,%s,%d,%d,null,null\n",
                        name, gameMode, status,
                        startDate.format(formatter),
                        endDate.format(formatter),
                        playerCapacity,
                        adminId));
            }
        }

        System.out.println("tournaments.csv generated successfully!");
    }
    
    // Generate a random start date within the next 30 days
    private static OffsetDateTime generateRandomStartDate() {
        int startDayOffset = random.nextInt(MAX_DAYS_OFFSET); // Within the next 30 days
        int startHour = random.nextInt(MAX_HOURS_OFFSET - MIN_HOURS_OFFSET + 1) + MIN_HOURS_OFFSET; // Between 8 AM and 8 PM

        // Start date offset from the current date
        return OffsetDateTime.now()
                .plusDays(startDayOffset)
                .withHour(startHour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
