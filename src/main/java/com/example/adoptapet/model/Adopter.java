package com.example.adoptapet.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un adoptante en el sistema.
 * Esta clase gestiona la información personal, el historial de mascotas adoptadas 
 * y la integración con el sistema de seguridad de Spring mediante la interfaz UserDetails.
 */
@Entity
@Table(name = "adopter")
@Getter
@Setter
@NoArgsConstructor
public class Adopter implements org.springframework.security.core.userdetails.UserDetails {

    /** Identificador único autoincremental en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador único de GitHub para usuarios autenticados mediante OAuth2. */
    @Column(unique = true)
    private String githubId; 

    /** Nombre real o nombre de usuario local del adoptante. */
    private String nombre;

    /** Dirección de correo electrónico para notificaciones SMTP. */
    private String email;

    /** Número de teléfono de contacto validado. */
    private String telefono;

    /** Contraseña cifrada para acceso mediante login tradicional. */
    private String password;

    /** Relación uno a muchos: Lista de mascotas vinculadas a este adoptante. */
    @OneToMany(mappedBy = "adopter")
    private List<MascotaEntity> mascotas;

    /** * Relación muchos a muchos con los roles de seguridad. 
     * Carga de tipo EAGER para disponer de los permisos inmediatamente en la sesión.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "adopter_roles",
            joinColumns = @JoinColumn(name = "adopter_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor para inicialización de datos básicos de contacto.
     * @param nombre Nombre del adoptante.
     * @param email Correo electrónico.
     * @param telefono Teléfono de contacto.
     */
    public Adopter(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * Constructor completo para el registro de usuarios locales con credenciales.
     * @param nombre Nombre del adoptante.
     * @param email Correo electrónico.
     * @param telefono Teléfono de contacto.
     * @param password Contraseña cifrada.
     */
    public Adopter(String nombre, String email, String telefono, String password) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
    }

    // --- IMPLEMENTACIÓN DE USERDETAILS ---

    /**
     * Mapea los roles personalizados a autoridades entendibles por Spring Security.
     * Añade el prefijo "ROLE_" a cada autoridad para cumplir con el estándar.
     * @return Colección de autoridades concedidas al usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
                .collect(Collectors.toList());
    }

    /** @return La contraseña cifrada del usuario. */
    @Override
    public String getPassword() {
        return password;
    }

    /** * Determina el identificador de acceso. 
     * Prioriza el ID de GitHub si existe, garantizando soporte híbrido.
     * @return Nombre de usuario para el proceso de autenticación.
     */
    @Override
    public String getUsername() {
        return githubId != null ? githubId : nombre;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}