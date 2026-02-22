package com.example.adoptapet.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.Role;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.repository.RoleRepository;

/**
 * Componente de configuración encargado de la precarga de datos (Seeding).
 * Se ejecuta automáticamente al iniciar la aplicación para garantizar que existan
 * los roles y usuarios administrativos necesarios en la base de datos.
 */
@Configuration
public class DataLoader {

    /**
     * Inicializa los datos maestros de la aplicación si la base de datos está vacía.
     * Crea los roles de seguridad y las cuentas de prueba para el administrador y usuarios estándar.
     *
     * @param adopterRepo Repositorio para la persistencia de adoptantes.
     * @param roleRepo Repositorio para la gestión de roles de seguridad.
     * @param encoder Componente para el cifrado de contraseñas mediante BCrypt.
     * @return Un {@link CommandLineRunner} que ejecuta la lógica de inserción inicial.
     */
    @Bean
    CommandLineRunner initData(AdopterRepository adopterRepo, RoleRepository roleRepo, PasswordEncoder encoder) {

        return args -> {
            // Inicialización de Roles de Seguridad
            if (roleRepo.count() == 0) {
                roleRepo.save(new Role("USER"));
                roleRepo.save(new Role("ADMIN"));
            }

            // Registro del Usuario de Pruebas (USER)
            if (adopterRepo.findByNombre("user").isEmpty()) {
                Role userRole = roleRepo.findByNombre("USER").orElseThrow();
                Adopter user = new Adopter("user", "user@email.com", "123456789", encoder.encode("1234"));
                user.getRoles().add(userRole);
                adopterRepo.save(user);
            }
            
            // Registro del Administrador del Sistema (ADMIN)
            if (adopterRepo.findByNombre("admin").isEmpty()) {
                Role userRole = roleRepo.findByNombre("USER").orElseThrow();
                Role adminRole = roleRepo.findByNombre("ADMIN").orElseThrow();
                
                Adopter admin = new Adopter("admin", "admin@email.com", "987654321", encoder.encode("admin"));
                
                // Asignación de jerarquía de roles: el admin posee privilegios de ambos niveles
                admin.getRoles().add(userRole);
                admin.getRoles().add(adminRole);
                
                adopterRepo.save(admin);
            }
        };
    }
}