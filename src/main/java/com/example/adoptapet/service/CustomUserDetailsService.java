package com.example.adoptapet.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.CustomUserDetails;
import com.example.adoptapet.repository.AdopterRepository;

/**
 * Servicio de autenticación personalizado que implementa la interfaz {@link UserDetailsService}.
 * Se encarga de la recuperación de perfiles de usuario desde la persistencia local para el
 * proceso de login de Spring Security.
 * * Implementa una estrategia de búsqueda dual para soportar tanto identificadores de GitHub 
 * como nombres de usuario registrados localmente.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /** Repositorio de adoptantes para el acceso a datos de identidad. */
    private final AdopterRepository adopterRepository;

    /**
     * Constructor para la inyección de dependencias del repositorio.
     * @param adopterRepository Repositorio encargado de la persistencia de usuarios.
     */
    public CustomUserDetailsService(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    /**
     * Localiza al usuario en el sistema basándose en su nombre de usuario o identificador.
     * El método intenta primero una búsqueda por GitHub ID y, en caso de no éxito, 
     * procede a buscar por el nombre de usuario local.
     *
     * @param username El identificador introducido en el formulario de login.
     * @return Una instancia de {@link UserDetails} (encapsulada en {@link CustomUserDetails}) 
     * lista para la validación de credenciales.
     * @throws UsernameNotFoundException Si el identificador no coincide con ningún registro en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Estrategia de búsqueda 1: Identificador de proveedor externo (GitHub)
        return adopterRepository.findByGithubId(username)
                .map(adopter -> new CustomUserDetails(adopter))
                .orElseGet(() -> {
                    // Estrategia de búsqueda 2: Identificador de registro local
                    Adopter adopter = adopterRepository.findByNombre(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
                    return new CustomUserDetails(adopter);
                });
    }
}