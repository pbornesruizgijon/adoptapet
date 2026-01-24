package com.example.adoptapet.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.Role;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.repository.RoleRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(AdopterRepository adopterRepo, RoleRepository roleRepo, PasswordEncoder encoder) {

        return args -> {
            // Roles
            if (roleRepo.count() == 0) {
                roleRepo.save(new Role("USER"));
                roleRepo.save(new Role("ADMIN"));
            }

            // Usuario TEST
            if (adopterRepo.findByNombre("user").isEmpty()) {
                Role userRole = roleRepo.findByNombre("USER").orElseThrow();
                Adopter user = new Adopter("user", "user@email.com", "123456789", encoder.encode("1234"));
                user.getRoles().add(userRole);
                adopterRepo.save(user);
            }
            // Admin
            if (adopterRepo.findByNombre("admin").isEmpty()) {
                Role adminRole = roleRepo.findByNombre("ADMIN").orElseThrow();
                Role userRole = roleRepo.findByNombre("USER").orElseThrow();
                Adopter admin = new Adopter("admin", "admin@email.com", "987654321", encoder.encode("admin"));
                admin.getRoles().add(adminRole);
                admin.getRoles().add(userRole);
                adopterRepo.save(admin);
            }
        };
    }
}
