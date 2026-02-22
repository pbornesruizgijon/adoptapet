package com.example.adoptapet.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para modificar datos

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.MascotaEntity;
import com.example.adoptapet.model.exceptions.AlreadyAdoptedException;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.repository.MascotaRepository;

/**
 * Servicio de lógica de negocio para la gestión integral de mascotas. Esta
 * clase orquestar las operaciones de persistencia, reglas de adopción y
 * generación de estadísticas, actuando como intermediario entre los
 * controladores y los repositorios.
 *
 */
@Service
public class PetService {

    private final MascotaRepository mascotaRepository;
    private final AdopterRepository adopterRepository;

    /**
     * Constructor para la inyección de dependencias de los repositorios
     * necesarios.
     *
     * @param mascotaRepository Repositorio de datos para mascotas.
     * @param adopterRepository Repositorio de datos para adoptantes.
     */
    public PetService(MascotaRepository mascotaRepository,
            AdopterRepository adopterRepository) {
        this.mascotaRepository = mascotaRepository;
        this.adopterRepository = adopterRepository;
    }

    // --- MÉTODOS DE GESTIÓN ---
    /**
     * Persiste o actualiza una mascota en la base de datos.
     *
     * @param mascota Entidad mascota a guardar.
     * @return La mascota guardada con su ID generado.
     */
    public MascotaEntity save(MascotaEntity mascota) {
        return mascotaRepository.save(mascota);
    }

    /**
     * Recupera el listado completo de mascotas registradas.
     *
     * @return Lista polimórfica de mascotas.
     */
    public List<MascotaEntity> todas() {
        return mascotaRepository.findAll();
    }

    /**
     * Busca una mascota por su identificador único.
     *
     * @param id Identificador de la mascota.
     * @return Un {@link Optional} con la mascota encontrada.
     */
    public Optional<MascotaEntity> findById(Long id) {
        return mascotaRepository.findById(id);
    }

    /**
     * Elimina una mascota del sistema tras verificar su existencia.
     *
     * @param id ID de la mascota a eliminar.
     * @throws NoSuchElementException Si la mascota no existe en la base de
     * datos.
     */
    public void eliminar(Long id) {
        if (!mascotaRepository.existsById(id)) {
            throw new NoSuchElementException("No se puede eliminar: Mascota no encontrada");
        }
        mascotaRepository.deleteById(id);
    }

    // --- MÉTODOS DE ADOPCIÓN ---
    /**
     * Procesa la adopción de una mascota bajo una gestión transaccional.
     * Vincula la mascota con un adoptante existente o crea uno nuevo si es
     * necesario, garantizando la integridad de los datos.
     *
     * @param id ID de la mascota a adoptar.
     * @param adopterName Nombre del adoptante.
     * @throws AlreadyAdoptedException Si la mascota ya tiene un proceso de
     * adopción finalizado.
     * @throws NoSuchElementException Si el ID de la mascota no es válido.
     */
    @Transactional
    public void adoptar(Long id, String adopterName) throws AlreadyAdoptedException {
        MascotaEntity mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mascota no encontrada"));

        if (mascota.isAdoptada()) {
            throw new AlreadyAdoptedException("La mascota ya fue adoptada");
        }

        Optional<Adopter> opt = adopterRepository.findByNombre(adopterName);
        Adopter adopter;
        if (opt.isPresent()) {
            adopter = opt.get();
        } else {
            adopter = adopterRepository.save(new Adopter(adopterName, "", ""));
        }

        mascota.setAdopter(adopter);
        mascota.setAdoptada(true);
        mascotaRepository.save(mascota);
    }

    /**
     * Revierte el estado de adopción de una mascota, dejándola disponible nuevamente.
     * Libera la relación con el adoptante previo.
     * @param id ID de la mascota a liberar.
     */
    @Transactional
    public void anularAdopcion(Long id) {
        MascotaEntity mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mascota no encontrada"));

        mascota.setAdoptada(false);
        mascota.setAdopter(null); // Liberamos la mascota
        mascotaRepository.save(mascota);
    }

    /**
     * Recupera las mascotas adoptadas por un usuario específico.
     * @param username Nombre del adoptante.
     * @return Lista de mascotas vinculadas al usuario.
     */
    public List<MascotaEntity> findByAdopterName(String username) {
        return mascotaRepository.findByAdopterNombre(username);
    }

    // --- ESTADÍSTICAS Y FILTROS ---
    /**
     * Calcula el número total de mascotas que han sido adoptadas exitosamente.
     * Utiliza Stream API para filtrar la colección completa.
     * @return Cantidad total de adopciones.
     */
    public int contarAdoptadas() {
        return (int) mascotaRepository.findAll()
                .stream()
                .filter(MascotaEntity::isAdoptada)
                .count();
    }

    /**
     * Retorna la lista de mascotas ordenada según el criterio especificado.
     * @param campo Campo de ordenación (ej. "edad" o "nombre").
     * @return Lista ordenada de mascotas.
     */
    public List<MascotaEntity> ordenarPorCampo(String campo) {
        if ("edad".equalsIgnoreCase(campo)) {
            return mascotaRepository.findAll(Sort.by("edad"));
        }
        return mascotaRepository.findAll(Sort.by("nombre"));
    }
}
