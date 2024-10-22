# Tournament Management System

## Overview

The **Tournament Management System (TMS)** is a backend application designed to manage and organize tournaments for multiplayer games. The system supports key features such as player and clan availability tracking, game scheduling, matchmaking, and score tracking. Built using **Java Spring Boot** with a **microservice architecture**, this system is scalable, flexible, and easy to maintain.

The project is designed to handle multiple game modes, matchmaking, dynamic tournament management, and player availability. It integrates JPA for persistence, MockMVC for unit testing, and follows best practices for clean code architecture.

## Key Features

- **Tournament Management**: Administrators can create, update, and delete tournaments.
- **Match Scheduling**: Automatically schedule matches based on player availability and game mode.
- **Player and Clan Availability**: Players and clans can indicate their availability, which is used for optimal matchmaking.
- **Multiple Game Modes**: The system supports multiple game modes like Free-for-All, Team Deathmatch, etc.
- **Real-time Notifications**: Notify players when matches are about to begin and update them on match results.
- **Dynamic Matchmaking**: Matches players based on their Elo rating, availability, and chosen game mode.
- **Security**: Secured with JWT-based authentication for all API endpoints.

## Technologies Used

- **Java**: The primary language for the backend.
- **Spring Boot**: For building the microservice-based architecture.
- **Spring Data JPA**: For ORM and database interaction.
- **MySQL / PostgreSQL**: As the relational database for storing persistent data.
- **JWT**: For secure authentication and authorization.
- **WebSockets / LiveKit**: For real-time notifications to the frontend.
- **Spring Cloud / Kubernetes**: For managing microservices, load balancing, and service discovery.
- **Mockito**: For unit testing services.
- **Docker**: Containerization for consistent development and deployment environments.

## Microservices

1. **Tournament Service**: Handles all tournament-related operations such as creation, scheduling, and tracking.
2. **Player Service**: Manages player information such as profiles, rankings, and availability.
3. **Clan Service**: Manages clans, including clan creation and availability tracking.
4. **Matchmaking Service**: Responsible for creating matches based on availability and player statistics.
5. **Game Service**: Tracks game details such as scores, results, and outcomes.
6. **Notification Service**: Sends real-time notifications to users about match statuses and other updates.

## Prerequisites

- **Java 17** or higher
- **Maven** 3.x
- **MySQL** or **PostgreSQL** for database
- **Docker** (optional for containerization)
- **Postman** (optional for testing API endpoints)

## Running the Application

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/example/tournament-management-system.git
   cd tournament-management-system
   ```

2. **Set up the Database**:
   Create a MySQL or PostgreSQL database named `tournament_management`.

3. **Configure Application Properties**:
   Modify the `src/main/resources/application.properties` file to configure your database connection:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tournament_management
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Build the Project**:
   Run the following Maven command to build the project:
   ```bash
   mvn clean install
   ```

5. **Run the Application**:
   Use Maven to run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

6. **Access the API**:
   The API will be available at `http://localhost:8080`.

## API Endpoints

### **Player Availability**
- `POST /api/players/availability`: Create player availability
- `GET /api/players/availability/{playerId}`: Get availability for a specific player

### **Clan Availability**
- `POST /api/clans/availability`: Create clan availability
- `GET /api/clans/availability/{clanId}`: Get availability for a specific clan

### **Tournament**
- `POST /api/tournaments`: Create a new tournament
- `GET /api/tournaments/{id}`: Get tournament details
- `DELETE /api/tournaments/{id}`: Delete a tournament

### **Matchmaking**
- `POST /api/matches`: Trigger matchmaking for a tournament
- `GET /api/matches/{id}`: Get details for a specific match

## Running Tests

To run the unit tests and ensure the functionality of the application:
```bash
mvn test
```

### Example Unit Test with MockMvc

```java
@Test
public void createAvailability_ShouldReturnCreated() throws Exception {
    PlayerAvailabilityDto availabilityDto = new PlayerAvailabilityDto();
    availabilityDto.setAvailableDate(LocalDate.of(2024, 9, 21));
    availabilityDto.setStartTime(LocalTime.of(10, 0));
    availabilityDto.setEndTime(LocalTime.of(12, 0));

    mockMvc.perform(post("/api/players/availability")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(availabilityDto)))
            .andExpect(status().isCreated());
}
```

## Docker Setup

To run the application in Docker, follow these steps:

1. **Build Docker Image**:
   ```bash
   docker build -t tournament-management-system .
   ```

2. **Run the Container**:
   ```bash
   docker run -p 8080:8080 tournament-management-system
   ```

3. Access the API at `http://localhost:8080`.

## Future Improvements

- Implement player statistics and rankings based on game outcomes.
- Integrate live streaming of games with real-time analytics.
- Add more advanced matchmaking algorithms using machine learning.
- Support multiple databases using Spring Profiles.
