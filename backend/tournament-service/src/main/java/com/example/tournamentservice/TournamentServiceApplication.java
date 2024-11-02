package com.example.tournamentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.example.tournamentservice")
@EnableFeignClients(basePackages = "com.example.tournamentservice")
@EnableTransactionManagement
public class TournamentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentServiceApplication.class, args);
	}

}
