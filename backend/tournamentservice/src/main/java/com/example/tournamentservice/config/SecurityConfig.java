import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF protection if necessary (be cautious)
            .authorizeRequests()
                .requestMatchers("/public/**").permitAll() // Allow access to public endpoints
                .anyRequest().authenticated() // Require authentication for all other requests
                .and()
            .formLogin().disable(); // Disable form login if you want to bypass it

        return http.build();
    }
}
