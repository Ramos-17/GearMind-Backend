package com.gearmind.gearmind_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.gearmind.gearmind_app.model.User;
import com.gearmind.gearmind_app.model.Role;
import com.gearmind.gearmind_app.repository.UserRepository;
import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.gearmind.gearmind_app.repository")
@EntityScan(basePackages = "com.gearmind.gearmind_app.model")
public class GearmindAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(GearmindAppApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin user already exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin123")); // Change this password!
                adminUser.setRole(Role.ROLE_ADMIN);
                
                userRepository.save(adminUser);
                System.out.println(" Admin user created successfully!");
                System.out.println(" Username: admin");
                System.out.println(" Password: admin123");
                System.out.println(" IMPORTANT: Change the admin password after first login!");
            } else {
                System.out.println(" Admin user already exists, skipping creation.");
            }
        };
    }
}

