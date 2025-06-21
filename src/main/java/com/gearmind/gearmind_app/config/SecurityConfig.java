package com.gearmind.gearmind_app.config;

import com.gearmind.gearmind_app.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                .requestMatchers("/api/jobs/public", "/api/jobs/search").permitAll()
                .requestMatchers("/api/jobs/create").hasRole("ADMIN")
                .requestMatchers("/api/jobs/{id}/archive", "/api/jobs/{id}/unarchive").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/jobs/{id}/update-stage", "/api/jobs/{id}/update-stage-with-user").hasAnyRole("ADMIN", "MANAGER", "TECH", "PAINTER", "DETAILER")
                .requestMatchers("/api/jobs/{id}/update-notes", "/api/jobs/{id}/update-notes-with-user").hasAnyRole("ADMIN", "MANAGER", "TECH", "PAINTER", "DETAILER")
                .requestMatchers("/api/jobs/{id}/history").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/jobs/all", "/api/jobs/all-including-archived", "/api/jobs/archived").hasAnyRole("ADMIN", "MANAGER", "TECH", "PAINTER", "DETAILER")
                .requestMatchers("/api/jobs/{id}").hasAnyRole("ADMIN", "MANAGER", "TECH", "PAINTER", "DETAILER")
                .requestMatchers("/api/auth/users/tech").hasAnyRole("ADMIN", "MANAGER", "TECH", "PAINTER")
                .requestMatchers("/api/auth/users/painter").hasAnyRole("ADMIN", "MANAGER", "PAINTER", "TECH")
                .requestMatchers("/api/auth/users/detailer").hasAnyRole("ADMIN", "MANAGER", "DETAILER", "TECH", "PAINTER")
                .requestMatchers("/api/auth/users/all").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated()
            );

        http.addFilterBefore(
            jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
