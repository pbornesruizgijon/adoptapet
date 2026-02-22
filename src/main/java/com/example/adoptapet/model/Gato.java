package com.example.adoptapet.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa a un espécimen de tipo Gato dentro del sistema.
 * Esta clase extiende la funcionalidad base de {@link MascotaEntity} mediante 
 * herencia, especializando el modelo con atributos propios de los felinos.
 * * Utiliza una estrategia de herencia Single Table, identificándose en la 
 * base de datos mediante el valor discriminador "GATO".
 */
@Entity
@DiscriminatorValue("GATO")
@Getter
@Setter
public class Gato extends MascotaEntity {

    /** * Indica el estado clínico de esterilización del animal. 
     * Atributo específico para la gestión sanitaria en el proceso de adopción.
     */
    private boolean esterilizado;
}