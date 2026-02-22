package com.example.adoptapet.model;

import com.example.adoptapet.model.exceptions.AlreadyAdoptedException;

/**
 * Interfaz que define el contrato de comportamiento para cualquier entidad 
 * que pueda ser sujeta a un proceso de adopción dentro del sistema.
 * * Establece los métodos necesarios para gestionar el cambio de estado de 
 * disponibilidad y la vinculación con un adoptante.
 */
public interface Adoptable {

    /**
     * Ejecuta la lógica necesaria para formalizar la adopción de la entidad.
     * * @param adopterName Nombre del adoptante que asume la responsabilidad de la mascota.
     * @throws AlreadyAdoptedException Si la entidad ya ha sido adoptada previamente, 
     * impidiendo una duplicidad en el proceso.
     */
    void adoptar(String adopterName) throws AlreadyAdoptedException;

    /**
     * Consulta el estado de disponibilidad actual de la entidad.
     * * @return true si la entidad ya ha sido adoptada; false si aún está disponible.
     */
    boolean isAdopted();
}