package com.example.adoptapet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa los privilegios o niveles de acceso dentro del sistema.
 * Define las autoridades que pueden ser asignadas a un {@link Adopter} para 
 * segmentar las funcionalidades de la aplicación (ej. USER, ADMIN).
 * * Implementa el modelo de persistencia para la gestión de seguridad basada en roles (RBAC).
 */
@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class Role {

    /** Identificador único autoincremental del rol en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** * Nombre identificador del rol (ej. "USER" o "ADMIN").
     * Se garantiza su unicidad y obligatoriedad para evitar inconsistencias en la seguridad.
     */
    @Column(unique = true, nullable = false)
    private String nombre;  // "USER", "ADMIN"
    
    /**
     * Constructor para la creación de nuevos roles con un nombre específico.
     * @param nombre Etiqueta del rol que se registrará en el sistema.
     */
    public Role(String nombre) {
        this.nombre = nombre;
    }
    
}