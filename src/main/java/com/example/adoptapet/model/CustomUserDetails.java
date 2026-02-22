package com.example.adoptapet.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase adaptadora que implementa {@link UserDetails} para integrar la entidad 
 * {@link Adopter} con el ecosistema de seguridad de Spring Security.
 * * Actúa como un envoltorio (wrapper) que traduce los datos del adoptante 
 * al formato requerido por los proveedores de autenticación del framework.
 */
public class CustomUserDetails implements UserDetails {
    
    /** Instancia de la entidad adoptante que contiene los datos de identidad. */
    private Adopter adopter;
    
    /**
     * Constructor que inicializa el adaptador con una instancia de adoptante.
     * @param adopter Objeto de tipo {@link Adopter} recuperado de la persistencia.
     */
    public CustomUserDetails(Adopter adopter) { 
        this.adopter = adopter; 
    }
    
    /**
     * Transforma los roles del adoptante en una colección de autoridades otorgadas.
     * Mapea cada objeto {@link Role} a un {@link SimpleGrantedAuthority} para 
     * su posterior validación en filtros de acceso.
     * * @return Colección de permisos/autoridades del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return adopter.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getNombre()))
            .collect(Collectors.toList());
    }
    
    /** * @return El hash de la contraseña del adoptante, o una cadena vacía 
     * si el usuario se autentica mediante proveedores externos (OAuth2). 
     */
    @Override 
    public String getPassword() { 
        return adopter.getPassword() != null ? adopter.getPassword() : ""; 
    }
    
    /** * Determina el identificador de acceso prioritario.
     * @return El ID de GitHub si existe, en caso contrario, el nombre de usuario local.
     */
    @Override 
    public String getUsername() { 
        return adopter.getGithubId() != null ? adopter.getGithubId() : adopter.getNombre(); 
    }
    
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}