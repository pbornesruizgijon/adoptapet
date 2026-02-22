package com.example.adoptapet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // <-- IMPORTANTE
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

/**
 * Configuración central de seguridad de la aplicación.
 * Define las políticas de autenticación, autorización, gestión de sesiones
 * y la integración híbrida entre el sistema de usuarios local y el proveedor OAuth2 (GitHub).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Define el algoritmo de hash para la protección de credenciales.
     * Implementa BCrypt, un algoritmo de derivación de claves robusto que incluye "sal" 
     * aleatoria para mitigar ataques de diccionario y tablas arcoíris.
     * * @return Una instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de la cadena de filtros de seguridad (Security Filter Chain).
     * Establece las reglas de acceso por URL, define los puntos de entrada de login 
     * y las políticas de cierre de sesión.
     *
     * @param http Objeto para configurar la seguridad basada en web para peticiones HTTP.
     * @return La cadena de filtros configurada.
     * @throws Exception Si ocurre un error durante la configuración de la seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Gestión de recursos estáticos: Acceso total para garantizar la carga de estilos y medios.
                .requestMatchers("/base.css", "/index.css", "/lista.css", "/detalle.css", "/img/**").permitAll()
                
                // Rutas públicas: Navegación permitida para usuarios no autenticados.
                .requestMatchers("/", "/lista", "/detalle/**", "/registro").permitAll()
                
                // Control de Acceso basado en Roles (RBAC): Privilegios administrativos.
                // Se validan autoridades tanto con prefijo ROLE_ como sin él para asegurar compatibilidad.
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                
                // Acceso protegido: Requiere autenticación previa para realizar operaciones de adopción.
                .requestMatchers("/adoptar").authenticated()
                
                // Seguridad por defecto: Cualquier ruta no especificada requiere estar logueado.
                .anyRequest().authenticated()
            )
            // Integración del servicio de carga de usuarios desde la persistencia JPA.
            .userDetailsService(userDetailsService)
            
            // Orquestación de Autenticación Social (OAuth2): Integración con GitHub.
            .oauth2Login((oauth2) -> oauth2
                .loginPage("/") 
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
                .defaultSuccessUrl("/lista", true)
            )
            
            // Configuración del Formulario de Acceso local.
            .formLogin((form) -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/lista", true)
                .permitAll()
            )
            
            // Política de Cierre de Sesión: Invalida la sesión del servidor y elimina cookies del cliente.
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    /**
     * Define el servicio encargado de procesar y mapear los atributos de usuario de GitHub.
     * Permite adaptar la respuesta de la API de GitHub al modelo de datos interno.
     * * @return Una instancia de {@link CustomOAuth2UserService}.
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}