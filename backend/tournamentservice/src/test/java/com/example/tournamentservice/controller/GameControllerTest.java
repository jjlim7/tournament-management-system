// package com.example.tournamentservice.controller;

// import com.example.tournamentservice.controller.GameController;
// import com.example.tournamentservice.entity.Game;
// import com.example.tournamentservice.service.GameService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(GameController.class)
// @AutoConfigureMockMvc
// public class GameControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Mock
//     private GameService gameService;

//     private Game game;

//     @BeforeEach
//     public void setUp() {
//         game = new Game();
//         game.setName("Test Game");
//         game.setPlayerCapacity(10);
//         game.setPlayerIds(List.of(1L, 2L, 3L));
//         game.setStatus("Active");
//     }

//     @Test
//     public void testCreateGame() throws Exception {
//         when(gameService.createGame(any(Game.class))).thenReturn(game);

//         mockMvc.perform(post("/api/games")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"name\": \"Test Game\", \"playerCapacity\": 10, \"playerIds\": [1, 2, 3], \"status\": \"Active\"}"))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.name").value("Test Game"));
//     }

//     @Test
//     public void testGetAllGames() throws Exception {
//         when(gameService.getAllGames()).thenReturn(List.of(game));

//         mockMvc.perform(get("/api/games")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].name").value("Test Game"));
//     }

//     @Test
//     public void testGetGameById() throws Exception {
//         when(gameService.getGameById(anyLong())).thenReturn(game);

//         mockMvc.perform(get("/api/games/1")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("Test Game"));
//     }

//     @Test
//     public void testUpdateGame() throws Exception {
//         when(gameService.updateGame(anyLong(), any(Game.class))).thenReturn(game);

//         mockMvc.perform(put("/api/games/1")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"name\": \"Updated Game\", \"playerCapacity\": 15, \"playerIds\": [1, 4], \"status\": \"Inactive\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("Updated Game"));
//     }

//     @Test
//     public void testDeleteGame() throws Exception {
//         mockMvc.perform(delete("/api/games/1"))
//                 .andExpect(status().isNoContent());
//     }
// }
