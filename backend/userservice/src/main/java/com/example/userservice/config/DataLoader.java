package com.example.userservice.config;

import com.example.userservice.entity.Clan;
import com.example.userservice.entity.ClanUser;
import com.example.userservice.entity.User;
import com.example.userservice.service.ClanService;
import com.example.userservice.service.ClanUserService;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@Lazy
public class DataLoader implements CommandLineRunner {
    private final UserService userService;
    private final ClanService clanService;
    private final ClanUserService clanUserService;
    private final Random random = new Random();


    @Autowired
    public DataLoader(UserService userService, ClanService clanService, ClanUserService clanUserService) {
        this.userService = userService;
        this.clanService = clanService;
        this.clanUserService = clanUserService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {

        if (!userService.listAllUsers().isEmpty() || !clanService.listAllClans().isEmpty()) {
            System.out.println("Existing data in database, skipping seed!");
            return;
        }

        // seed user data
        List<User> users = IntStream.range(0, 100).mapToObj(i -> {
            User user = new User();
            user.setName("User_" + i);
            user.setEmail("user" + i + "@user.com");
            user.setPassword("password" + i);
            user.setRole("ROLE_PLAYER");
            return userService.addUser(user);
        }).toList();

        // seed clan data
        List<Clan> clans = IntStream.range(0, 10).mapToObj(i -> {
            Clan clan = new Clan();
            clan.setClanName("Clan_" + i);
            return clanService.addClan(clan);
        }).toList();

        // add user to clan, and the
        int usersPerClan = users.size() / clans.size(); // 10 users per clan
        for (int i = 0; i < clans.size(); i++) {
            Clan clan = clans.get(i);
            for (int j = 0; j < usersPerClan; j++) {
                int userIndex = i * usersPerClan + j;
                User user = users.get(userIndex);

                boolean isClanLeader = (j == 0); // The first user in each clan is the leader
                String position = isClanLeader ? "Leader" : "Member";

                ClanUser clanUser = new ClanUser(user, clan, isClanLeader, position);
                clanUserService.addClanUser(clanUser);
            }
        }

        System.out.println("Seeded User & Clan data!");
    }
}
