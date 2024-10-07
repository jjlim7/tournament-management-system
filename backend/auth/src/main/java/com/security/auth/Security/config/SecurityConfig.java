package com.security.auth.Security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.security.auth.Security.filter.JwtAuthenticationFilter;
import com.security.auth.Security.service.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsServiceImp;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomLogoutHandler logoutHandler;

    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomLogoutHandler logoutHandler) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        //change permitAll() to hasAuthority("ROLE_ADMIN") for authorization : ROLE_ADMIN , ROLE_PLAYER
                        req->req //customer
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/hello")).hasAuthority("ROLE_PLAYER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/customer/createTicket")).hasAuthority("ROLE_PLAYER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/customer/viewAccount/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/customer/cancelTicket")).hasAuthority("ROLE_CUSTOMER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/customer/getTicketHistoryByEmail/*")).hasAuthority("ROLE_CUSTOMER")

                                //account
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/account/**")).hasAuthority("ROLE_CUSTOMER")

                                //TicketOffice
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/ticketOfficer/**")).hasAuthority("ROLE_TICKETOFFICER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/ticketOfficer/**")).hasAuthority("ROLE_TICKETOFFICER")

                                //event
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/event/getAllEvents")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/event/getEventById/*")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/event/generateSalesSatistics/**")).hasAuthority("ROLE_EVENTMANAGER")

                                //event manager
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/event/createEvent/*")).hasAuthority("ROLE_EVENTMANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/event/updateEvent/*")).hasAuthority("ROLE_EVENTMANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/event/cancelEvent/*")).hasAuthority("ROLE_EVENTMANAGER")
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/user/addTicketingOfficer/*")).hasAuthority("ROLE_EVENTMANAGER")

                                //auth
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/register")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/login")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/logout")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/person/updatePerson/*")).hasAnyAuthority("ROLE_EVENTMANAGER","ROLE_CUSTOMER","ROLE_TICKETOFFICER")


                                .anyRequest()
                                .authenticated()
                ).userDetailsService(userDetailsServiceImp)
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        e -> e.accessDeniedHandler((request, response, accessDeniedException) -> {
                                    // Print or log the access denied exception
                                    System.out.println("Access Denied Exception: " + accessDeniedException.getMessage());
//                                    accessDeniedException.printStackTrace(); // This will print the full stack trace to the console
                                    response.setStatus(403);
                                })
                                .authenticationEntryPoint((request, response, authException) -> {
                                    // Print or log the authentication exception
                                    System.out.println("Authentication Exception: " + authException.getMessage());
//                                    authException.printStackTrace(); // This will print the full stack trace to the console
                                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                })
                )
                .logout(l->l
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
                        ))
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
