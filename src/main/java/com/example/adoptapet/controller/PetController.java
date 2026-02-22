package com.example.adoptapet.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.Gato;
import com.example.adoptapet.model.MascotaEntity;
import com.example.adoptapet.model.Perro;
import com.example.adoptapet.model.exceptions.AlreadyAdoptedException;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.service.CloudinaryService;
import com.example.adoptapet.service.EmailService;
import com.example.adoptapet.service.PetService;

/**
 * Controlador principal de la aplicación AdoptaPet.
 * Gestiona el ciclo de vida de las mascotas, la interacción con servicios cloud (Cloudinary),
 * el sistema de notificaciones SMTP y la lógica de adopción híbrida.
 */
@Controller
public class PetController {

    @Autowired
    private AdopterRepository adopterRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Inyectamos el servicio de correo para enviar notificaciones
    @Autowired
    private EmailService emailService;

    private final PetService petService;

    /**
     * Constructor para la inyección de dependencias del servicio de mascotas.
     * @param petService Servicio que gestiona la lógica de negocio de las mascotas.
     */
    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Gestiona el acceso a la página principal.
     * Recupera información del usuario autenticado y estadísticas globales de adopción.
     * @param model Modelo para la transferencia de datos a la vista.
     * @param principal Objeto que representa al usuario autenticado.
     * @return Nombre de la vista index.
     */
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            // Buscar nombre real
            String displayName = adopterRepo.findByGithubId(username)
                    .map(Adopter::getNombre)
                    .orElse(username);
            model.addAttribute("usuario", displayName);
            model.addAttribute("githubUsername", username);
        }
        model.addAttribute("totalAdoptadas", petService.contarAdoptadas());
        return "index";
    }

    /**
     * Muestra el formulario para dar de alta una nueva mascota.
     * Utiliza la clase concreta Perro para facilitar el binding de Thymeleaf.
     * @param model Modelo para la vista.
     * @return Nombre de la vista agregar.
     */
    @GetMapping("/admin/agregar")
    public String formAgregar(Model model) {
        model.addAttribute("mascota", new Perro());
        return "agregar";
    }

    /**
     * Procesa la creación o edición de una mascota.
     * Gestiona el polimorfismo entre Perros y Gatos y realiza la subida de imágenes a Cloudinary.
     * @param id ID opcional (solo presente en edición).
     * @param nombre Nombre de la mascota.
     * @param edad Edad de la mascota.
     * @param raza Raza de la mascota.
     * @param sexo Género de la mascota.
     * @param descripcion Detalles adicionales.
     * @param tipoAnimal Clasificación (PERRO/GATO).
     * @param file Archivo de imagen subido por el usuario.
     * @throws IOException Si ocurre un error durante el procesamiento del archivo.
     * @return Redirección a la lista de mascotas.
     */
    @PostMapping("/admin/agregar")
    public String agregar(@RequestParam(required = false) Long id,
            @RequestParam String nombre,
            @RequestParam int edad,
            @RequestParam String raza,
            @RequestParam String sexo,
            @RequestParam String descripcion,
            @RequestParam(required = false) String tipoAnimal,
            @RequestParam("file") MultipartFile file) throws IOException {

        MascotaEntity mascota;

        if (id != null) {
            // Modo Edición: Buscamos la existente para mantener su clase (Perro/Gato)
            mascota = petService.findById(id).orElseThrow();
        } else {
            // Modo Nuevo: Aplicamos Polimorfismo según la documentación técnica
            mascota = "GATO".equalsIgnoreCase(tipoAnimal) ? new Gato() : new Perro();
            mascota.setAdoptada(false);
        }

        // Mapeo manual de campos (evita errores de instanciación automática)
        mascota.setNombre(nombre);
        mascota.setEdad(edad);
        mascota.setRaza(raza);
        mascota.setSexo(sexo);
        mascota.setDescripcion(descripcion);

        // Lógica de Cloudinary
        if (file != null && !file.isEmpty()) {
            try {
                // Usamos secure_url como recomienda la opción 2 de tu doc
                String url = cloudinaryService.uploadFile(file);
                mascota.setImagen(url);
            } catch (Exception e) {
                System.err.println("Error subiendo a Cloudinary: " + e.getMessage());
                // Si falla la subida, le ponemos una por defecto para que no de 500
                if (mascota.getImagen() == null) {
                    mascota.setImagen("https://via.placeholder.com/300");
                }
            }
        }

        petService.save(mascota);
        return "redirect:/lista";
    }

    /**
     * Lista todas las mascotas disponibles y permite ordenarlas por campos específicos.
     * @param orden Campo por el cual ordenar (ej. nombre, edad).
     * @param principal Usuario autenticado.
     * @param model Modelo para la vista.
     * @return Vista con el listado de mascotas.
     */
    @GetMapping("/lista")
    public String lista(@RequestParam(required = false) String orden, Principal principal, Model model) {
        String campo = "nombre";
        if ("edad".equalsIgnoreCase(orden)) {
            campo = "edad";
        }
        List<MascotaEntity> mascotas = petService.ordenarPorCampo(campo);
        if (principal != null) {
            model.addAttribute("usuario", principal.getName());
        }
        model.addAttribute("mascotas", mascotas);
        return "lista";
    }

    /**
     * Muestra la información detallada de una mascota específica.
     * @param id ID de la mascota.
     * @param model Modelo para la vista.
     * @return Vista de detalle de la mascota.
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        MascotaEntity mascota = petService.findById(id).orElse(null);
        model.addAttribute("mascota", mascota);
        return "detalle";
    }

    /**
     * Procesa la solicitud de adopción de una mascota.
     * Implementa lógica de recuperación de contacto híbrida (OAuth2/JPA) y envía una notificación SMTP.
     * @param id ID de la mascota a adoptar.
     * @param principal Usuario autenticado que realiza la adopción.
     * @param model Modelo para gestionar mensajes de éxito o error.
     * @return Vista de detalle con el resultado del proceso.
     */
    @PostMapping("/adoptar")
    public String adoptar(@RequestParam Long id, java.security.Principal principal, Model model) {
        // 1. Verificamos que el usuario esté autenticado
        if (principal == null) {
            return "redirect:/login";
        }

        String usuarioNombre = principal.getName();
        String emailUsuario = null;

        // 2. Extraemos el email si es un usuario de GitHub
        if (principal instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken token) {
            var oauth2User = token.getPrincipal();
            usuarioNombre = oauth2User.getAttribute("login");
            emailUsuario = oauth2User.getAttribute("email");
        }

        // 3. ARREGLADO: Si no hay email (Usuario Local), buscamos por NOMBRE en la BD
        if (emailUsuario == null || emailUsuario.isEmpty()) {
            emailUsuario = adopterRepo.findByNombre(principal.getName())
                    .map(com.example.adoptapet.model.Adopter::getEmail)
                    .orElse(null);
        }

        // 4. Buscamos la mascota
        MascotaEntity mascota = petService.findById(id).orElse(null);
        if (mascota == null) {
            model.addAttribute("error", "Mascota no encontrada");
            return "detalle";
        }

        try {
            // 5. Procesamos la adopción en BD
            petService.adoptar(id, usuarioNombre);

            String mensajeExito = "¡Felicidades " + usuarioNombre + "! Adopción completada con éxito.";

            // 6. Envío de correo con foto de Cloudinary
            if (emailUsuario != null && !emailUsuario.isEmpty()) {
                try {
                    String urlFoto = (mascota.getImagen() != null) ? mascota.getImagen() : "Sin foto";
                    emailService.enviarCorreoAdopcion(emailUsuario, mascota.getNombre(), urlFoto);
                    mensajeExito += " Se ha enviado un comprobante a " + emailUsuario;
                } catch (Exception e) {
                    System.err.println("Error al enviar email: " + e.getMessage());
                    mensajeExito += " (No se pudo enviar el correo de confirmación).";
                }
            } else {
                mensajeExito += " (No se envió correo: no se encontró dirección de email asociada).";
            }

            model.addAttribute("mascota", petService.findById(id).orElse(mascota));
            model.addAttribute("mensaje", mensajeExito);
            return "detalle";

        } catch (AlreadyAdoptedException ex) {
            model.addAttribute("mascota", mascota);
            model.addAttribute("error", ex.getMessage());
            return "detalle";
        } catch (Exception ex) {
            model.addAttribute("mascota", mascota);
            model.addAttribute("error", "Error inesperado: " + ex.getMessage());
            return "detalle";
        }
    }

    /**
     * Prepara el formulario de edición para una mascota existente.
     * @param id ID de la mascota a editar.
     * @param model Modelo para la vista.
     * @return Vista de formulario (agregar).
     */
    @GetMapping("/admin/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        MascotaEntity mascota = petService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mascota no encontrada"));
        model.addAttribute("mascota", mascota);
        return "agregar"; // Reutilizamos el mismo formulario de agregar
    }

    /**
     * Elimina una mascota del sistema.
     * @param id ID de la mascota a eliminar.
     * @return Redirección a la lista de mascotas.
     */
    @PostMapping("/admin/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        petService.eliminar(id);
        return "redirect:/lista";
    }

    /**
     * Muestra las mascotas adoptadas por el usuario autenticado.
     * @param principal Usuario autenticado.
     * @param model Modelo para la vista.
     * @return Vista de adopciones personales.
     */
    @GetMapping("/mis-adopciones")
    public String misAdopciones(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        String username = principal.getName();
        List<MascotaEntity> misMascotas = petService.findByAdopterName(username);

        model.addAttribute("mascotas", misMascotas);
        model.addAttribute("usuario", username);
        return "mis-adopciones";
    }

    /**
     * Revierte el estado de una mascota a disponible y elimina la asociación con el adoptante.
     * @param id ID de la mascota.
     * @return Redirección al detalle de la mascota.
     */
    @PostMapping("/admin/anular-adopcion/{id}")
    public String anularAdopcion(@PathVariable Long id) {
        petService.anularAdopcion(id);
        return "redirect:/detalle/" + id;
    }

}
