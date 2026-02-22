package com.example.adoptapet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.adoptapet.model.MascotaEntity;

/**
 * Interfaz de persistencia para la jerarquía de entidades {@link MascotaEntity}.
 * Proporciona acceso a la base de datos utilizando Spring Data JPA para gestionar 
 * tanto objetos de tipo Perro como Gato de forma polimórfica.
 *
 */
@Repository
public interface MascotaRepository extends JpaRepository<MascotaEntity, Long> {
    
    /**
     * Recupera una lista de mascotas asociadas a un adoptante específico mediante su nombre.
     * Utiliza la resolución de propiedades de Spring Data para navegar desde la 
     * entidad Mascota hacia el atributo 'nombre' de la entidad relacionada Adopter.
     *
     * * @param nombre Nombre del adoptante cuyos registros se desean recuperar.
     * @return Una lista de {@link MascotaEntity} que pertenecen al adoptante indicado.
     */
    List<MascotaEntity> findByAdopterNombre(String nombre);
}