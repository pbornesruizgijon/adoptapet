package com.example.adoptapet.controller;

import java.util.List; // Asegúrate de importar tu clase principal

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.adoptapet.model.MascotaEntity;
import com.example.adoptapet.repository.MascotaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador de servicios Web RESTful para la entidad Mascota.
 * Expone puntos de acceso (endpoints) para la consulta de datos en formato JSON,
 * permitiendo la interoperabilidad con sistemas externos.
 * * Implementa las especificaciones de OpenAPI mediante Swagger para la documentación
 * interactiva de la API.
 */
@RestController
@RequestMapping("/api/mascotas")
@Tag(name = "API Mascotas", description = "Servicios REST para integración con terceros")
public class MascotaRestController {

    @Autowired
    private MascotaRepository mascotaRepository;

    /**
     * Recupera el catálogo completo de mascotas registradas en el sistema.
     * Gracias al polimorfismo de JPA, la lista incluye tanto instancias de 
     * Perro como de Gato con sus atributos específicos.
     * * @return Una lista de objetos {@link MascotaEntity} serializados en JSON.
     */
    @Operation(summary = "Listar catálogo", description = "Retorna todas las mascotas independientemente de su tipo")
    @GetMapping
    public List<MascotaEntity> listarTodas() {
        // Spring Boot se encarga de serializar la herencia correctamente en el JSON
        return mascotaRepository.findAll();
    }

    /**
     * Realiza una búsqueda selectiva de una mascota por su identificador único.
     * * @param id Identificador de la mascota a consultar.
     * @return El objeto {@link MascotaEntity} correspondiente, o null si no se encuentra.
     */
    @Operation(summary = "Buscar por ID", description = "Busca una mascota específica")
    @GetMapping("/{id}")
    public MascotaEntity obtenerPorId(@PathVariable Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }
}