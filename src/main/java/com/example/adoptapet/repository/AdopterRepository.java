package com.example.adoptapet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.adoptapet.model.Adopter;

/**
 * Interfaz de persistencia para la entidad {@link Adopter}.
 * Extiende de {@link JpaRepository} para proporcionar operaciones CRUD estándar
 * y define métodos de consulta personalizados para la gestión de identidades.
 *
 */
@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    /**
     * Recupera un adoptante basado en su nombre de usuario local.
     * Utilizado principalmente en el flujo de autenticación tradicional y registro.
     *
     * * @param nombre Nombre del adoptante a buscar.
     * @return Un {@link Optional} que contiene el adoptante si existe.
     */
    Optional<Adopter> findByNombre(String nombre); 

    /**
     * Recupera un adoptante basado en su identificador único de GitHub.
     * Esencial para la resolución de identidades en el flujo de autenticación OAuth2.
     *
     * * @param githubId Identificador de usuario proporcionado por GitHub.
     * @return Un {@link Optional} que contiene el adoptante vinculado a la cuenta de GitHub.
     */
    Optional<Adopter> findByGithubId(String githubId);
}