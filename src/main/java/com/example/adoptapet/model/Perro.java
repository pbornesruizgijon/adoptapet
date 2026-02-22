package com.example.adoptapet.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa a un espécimen de tipo Perro dentro del ecosistema AdoptaPet.
 * Esta clase especializa a {@link MascotaEntity} añadiendo atributos de comportamiento
 * y adiestramiento necesarios para el perfil del adoptante.
 * * Implementa la estrategia de herencia en base de datos mediante el valor 
 * discriminador "PERRO".
 */
@Entity
@DiscriminatorValue("PERRO")
@Getter
@Setter
public class Perro extends MascotaEntity {

    /** * Indica si la mascota ha recibido entrenamiento de obediencia o educación básica. 
     * Atributo relevante para filtrar mascotas según el nivel de experiencia del adoptante.
     */
    private boolean adiestrado;
}