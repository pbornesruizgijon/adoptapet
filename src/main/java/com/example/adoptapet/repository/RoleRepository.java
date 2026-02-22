package com.example.adoptapet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.adoptapet.model.Role;

/**
 * Interfaz de persistencia para la entidad {@link Role}.
 * Proporciona los métodos necesarios para gestionar los roles de seguridad 
 * del sistema, permitiendo la asignación de permisos a los usuarios.
 * */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Verifica la existencia de un rol específico en la base de datos por su nombre.
     * Utilizado para validar si los roles maestros ya han sido creados.
     * * * @param nombre Etiqueta del rol a verificar (ej. "ADMIN").
     * @return true si el rol ya existe; false en caso contrario.
     */
    boolean existsByNombre(String nombre);  

    /**
     * Recupera la entidad de un rol basado en su nombre identificador.
     * Esencial para la asignación de permisos durante el registro de nuevos usuarios.
     * * * @param nombre Nombre del privilegio a buscar.
     * @return Un {@link Optional} con el objeto {@link Role} si se encuentra en la base de datos.
     */
    Optional<Role> findByNombre(String nombre);
}