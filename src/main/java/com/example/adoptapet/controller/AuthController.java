package com.example.adoptapet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.Role;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.repository.RoleRepository;

/**
 * Controlador encargado de la gestión de autenticación y registro de nuevos usuarios.
 * Proporciona los puntos de entrada para el alta de adoptantes locales, aplicando
 * validaciones de integridad y cifrado de credenciales.
 */
@Controller
public class AuthController {

    @Autowired
    private AdopterRepository adopterRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra el formulario de registro de usuario.
     * @param model Modelo para la transferencia de datos a la vista Thymeleaf.
     * @return El nombre de la plantilla de registro.
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Adopter());
        return "registro";
    }

    /**
     * Procesa la solicitud de registro de un nuevo adoptante.
     * Incluye validación de robustez de contraseña mediante expresiones regulares.
     * @param adopter Objeto capturado del formulario.
     * @param model Modelo para mensajes de error.
     * @return Redirección a home o retorno al formulario.
     */
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Adopter adopter, Model model) {
        
        // 1. Validación de integridad de datos (Campos obligatorios)
        if (adopter.getNombre() == null || adopter.getNombre().isBlank() ||
            adopter.getPassword() == null || adopter.getPassword().isBlank() ||
            adopter.getEmail() == null || adopter.getEmail().isBlank() ||
            adopter.getTelefono() == null || adopter.getTelefono().isBlank()) {
            
            model.addAttribute("error", "Todos los campos son obligatorios.");
            model.addAttribute("usuario", adopter);
            return "registro";
        }

        // 2. Validación de formato de teléfono (9 dígitos)
        if (!adopter.getTelefono().matches("\\d{9}")) {
            model.addAttribute("error", "El teléfono debe tener exactamente 9 números.");
            model.addAttribute("usuario", adopter);
            return "registro";
        }

        // 2.5 VALIDACIÓN DE SEGURIDAD (PASSWORD)
        // Requisitos: Mínimo 8 caracteres, una mayúscula y un número.
        if (!adopter.getPassword().matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            model.addAttribute("error", "La contraseña no cumple los requisitos: debe tener al menos 8 caracteres, una mayúscula y un número.");
            model.addAttribute("usuario", adopter);
            return "registro";
        }

        // 3. Control de unicidad de nombre de usuario
        if (adopterRepo.findByNombre(adopter.getNombre()).isPresent()) {
            model.addAttribute("error", "El nombre de usuario '" + adopter.getNombre() + "' ya está en uso.");
            model.addAttribute("usuario", adopter);
            return "registro";
        }

        // 4. Cifrado de credenciales (BCrypt)
        adopter.setPassword(passwordEncoder.encode(adopter.getPassword()));

        // 5. Asignación de rol USER por defecto
        Role userRole = roleRepo.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Error: El rol USER no existe en la base de datos."));
        adopter.getRoles().add(userRole);

        // 6. Guardar en la base de datos
        adopterRepo.save(adopter);

        return "redirect:/?registroExitoso=true"; 
    }
}