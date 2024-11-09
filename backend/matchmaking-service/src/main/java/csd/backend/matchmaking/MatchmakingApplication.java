package csd.backend.matchmaking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableFeignClients
@EnableScheduling
public class MatchmakingApplication {

  public static void main(String[] args) {
    SpringApplication.run(MatchmakingApplication.class, args);
  }
}
