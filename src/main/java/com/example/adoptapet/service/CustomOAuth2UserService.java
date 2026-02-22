package com.example.adoptapet.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.Role;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.repository.RoleRepository;

/**
 * Servicio personalizado para la gestión de usuarios autenticados mediante el protocolo OAuth2 (GitHub).
 * Esta clase extiende la funcionalidad por defecto para sincronizar los datos proporcionados
 * por el proveedor externo con la base de datos local de la aplicación.
 * * Implementa una estrategia de "Just-in-Time Provisioning", creando o actualizando el 
 * perfil del adoptante en el momento del inicio de sesión.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    /** Delegado de Spring Security para realizar la carga estándar del usuario OAuth2. */
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultService = new DefaultOAuth2UserService();

    /** Repositorio para la persistencia de los datos del adoptante sincronizado. */
    @Autowired
    AdopterRepository adopterRepo;

    /** Repositorio para la asignación de roles de seguridad. */
    @Autowired
    private RoleRepository roleRepo;

    /**
     * Procesa la identidad del usuario tras una autenticación exitosa en GitHub.
     * Recupera los atributos del perfil externo (login, email, nombre) y asegura su 
     * existencia en la base de datos local vinculándolos mediante el identificador de GitHub.
     *
     * @param request La solicitud de usuario que contiene el token de acceso y parámetros del cliente.
     * @return El objeto {@link OAuth2User} con los atributos cargados para la sesión de Spring Security.
     * @throws RuntimeException Si el rol de usuario estándar (USER) no está preconfigurado en el sistema.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        // Ejecución de la lógica estándar de recuperación de usuario
        OAuth2User oAuth2User = defaultService.loadUser(request);

        // Extracción del identificador único de GitHub (username)
        String githubId = oAuth2User.getAttribute("login");
       
        /**
         * Lógica de Sincronización Local:
         * Intenta localizar al adoptante por su identificador de GitHub.
         * Si no existe, instancia un nuevo objeto {@link Adopter} para su registro.
         */
        Adopter adopter = adopterRepo.findByGithubId(githubId)
                .orElse(new Adopter());

        // Mapeo dinámico de atributos externos al modelo de datos interno
        adopter.setGithubId(githubId);
        adopter.setEmail(oAuth2User.getAttribute("email"));
        adopter.setNombre(oAuth2User.getAttribute("name"));

        // Asignación de privilegios automáticos mediante el rol USER
        Role userRole = roleRepo.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Role USER no encontrado"));
        adopter.setRoles(Set.of(userRole));

        // Persistencia o actualización del perfil sincronizado
        adopterRepo.save(adopter);

        return oAuth2User;
    }
}