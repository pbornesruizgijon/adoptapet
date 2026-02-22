package com.example.adoptapet.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase base abstracta que define la estructura y el comportamiento común para
 * todas las mascotas en el sistema AdoptaPet. Utiliza la estrategia de persistencia SINGLE_TABLE 
 * para agrupar toda la jerarquía en una única tabla de base de datos, optimizando el
 * rendimiento de las consultas.
 */
@Entity
@Table(name = "mascota")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_animal", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class MascotaEntity {

    /**
     * Identificador único autoincremental de la mascota.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la mascota.
     */
    private String nombre;

    /**
     * Edad de la mascota expresada en años.
     */
    private int edad;

    /**
     * Raza o linaje específico del animal.
     */
    private String raza;

    /**
     * Género del animal (ej. Macho, Hembra).
     */
    private String sexo;

    /**
     * Descripción detallada sobre la personalidad y necesidades de la mascota.
     */
    private String descripcion;

    /**
     * Estado lógico que indica si el proceso de adopción ha finalizado con
     * éxito.
     */
    private boolean adoptada;

    /**
     * URL segura del recurso multimedia (fotografía) almacenado en Cloudinary.
     */
    private String imagen;

    /**
     * * Relación de asociación muchos-a-uno con el adoptante. Vincula la
     * mascota con el usuario responsable una vez procesada la adopción.
     */
    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Adopter adopter;

    public String getTipoMascota() {
        return this.getClass().getSimpleName().toLowerCase();
    }
}
