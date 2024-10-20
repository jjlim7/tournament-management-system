package csd.backend.matchmaking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableFeignClients
public class MatchmakingApplication {

  public static void main(String[] args) {
    SpringApplication.run(MatchmakingApplication.class, args);
  }
}
