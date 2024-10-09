package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.userservice.repository.*;
import com.example.userservice.entity.*;
import com.example.userservice.service.*;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		// ApplicationContext ctx = SpringApplication.run(UserServiceApplication.class, args);
		SpringApplication.run(UserServiceApplication.class, args);

		// // Initializing UserRepository
        // UserRepository userDB = ctx.getBean(UserRepository.class);

		// Long testUserId1 = (long) 1;
		// Long testUserRating1 = (long) 100;
		// User testUser1 = new User(testUserId1, "Bob", "bob@gmail.com", "abc123", "ROLE_PLAYER", "Pakistan", testUserRating1, "UNRANKED");
        
		// // Long testUserId2 = (long) 2;
		// // Long testUserRating2 = (long) 200;
		// // User testUser2 = new User(testUserId2, "Joe", "joe@gmail.com", "def123", "ROLE_PLAYER", "Kazakhstan", testUserRating2, "IRON");
		
		// // Long testUserId3 = (long) 3;
		// // Long testUserRating3 = (long) 300;
		// // User testUser3 = new User(testUserId3, "Sue", "sue@gmail.com", "ghi123", "ROLE_ADMIN", "Dubai", testUserRating3, "SILVER");
		
		// // System.out.println(userDB.findById(1L));
		// System.out.println("[Add user]: " + userDB.save(testUser1));
		// System.out.println("[Add user]: " + userDB.save(testUser2).getName());
		// System.out.println("[Add user]: " + userDB.save(testUser3).getName());


	}

}
