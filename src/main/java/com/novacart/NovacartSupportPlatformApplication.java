package com.novacart;

import com.novacart.user.User;
import com.novacart.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class NovacartSupportPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovacartSupportPlatformApplication.class, args);
    }

    // Yeh code application start hote hi database mein ek default user bana dega
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("test@novacart.com").isEmpty()) {
                User user = new User();
                user.setName("Test Admin");
                user.setEmail("test@novacart.com");
                // Password encrypt ho kar save hoga: password123
                user.setPassword(passwordEncoder.encode("password123"));
                user.setRole("ADMIN");
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());
                
                userRepository.save(user);
                System.out.println(">>> Default Test User Created: test@novacart.com / password123 <<<");
            }
        };
    }
}