package com.example.adoptapet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import com.example.adoptapet.service.CustomOAuth2UserService;
import com.example.adoptapet.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Define la cadena de filtros de seguridad de Spring
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                // Rutas ESTÁTICAS públicas (CSS, IMG)
                .requestMatchers("/base.css", "/index.css", "/lista.css", "/detalle.css", "/img/**").permitAll()
                // Rutas PÚBLICAS (visibles sin login)
                .requestMatchers("/", "/lista", "/detalle/**").permitAll()
                // POST adoptar: CUALQUIER AUTENTICADO (local + GitHub)
                .requestMatchers("/adoptar").authenticated()
                // ADMIN: agregar mascotas
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Resto: requiere login
                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .oauth2Login((oauth2) -> oauth2
                .loginPage("/")
                .defaultSuccessUrl("/lista", true)
                )
                // Configuración del login
                .formLogin((form) -> form
                // Página personalizada de login
                .loginPage("/")
                // URL que procesa el formulario de login
                .loginProcessingUrl("/login")
                // Me redirige tras login correcto
                .defaultSuccessUrl("/lista", true)
                .permitAll()
                )
                // Configuración del logout
                .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") // Me redirige al hacer logout
                // Permite acceso al logout a cualquiera
                .permitAll()
                );

        // Construye y devuelve la configuración de seguridad
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}
