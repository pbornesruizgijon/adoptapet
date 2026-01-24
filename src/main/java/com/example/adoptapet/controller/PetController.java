package com.example.adoptapet.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.MascotaEntity;
import com.example.adoptapet.model.exceptions.AlreadyAdoptedException;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.service.PetService;

@Controller
public class PetController {

    @Autowired
    private AdopterRepository adopterRepo;
    
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Página principal
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

    @GetMapping("/admin/agregar")
    @PreAuthorize("hasRole('ADMIN')") //BLOQUEA si NO es ADMIN
    public String formAgregar(Model model) {
        model.addAttribute("mascota", new MascotaEntity()); //Objeto vacío para  Thymeleaf
        return "agregar";
    }

    @PostMapping("/admin/agregar")                                 // ← POST /admin/agregar (form submit)
    @PreAuthorize("hasRole('ADMIN')")                              // ← BLOQUEA si NO es ADMIN (403)
    public String agregar(@ModelAttribute MascotaEntity mascota, // ← Thymeleaf BIND campos form → objeto
            Model model) {                           // 
        petService.save(mascota);                                  // ← JPA .save() → H2 database
        return "redirect:/lista";                                  // ← PRG Pattern: evita re-submit
    }

    // Mostrar lista de mascotas
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

    // Detalle de una mascota
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        MascotaEntity mascota = petService.findById(id).orElse(null);
        model.addAttribute("mascota", mascota);
        return "detalle";
    }

    // Adopción protegida
    @PostMapping("/adoptar")
    public String adoptar(@RequestParam Long id, Principal principal, Model model) {
        // Spring Security garantiza que si llegamos aquí, 'principal' no es null
        // porque la ruta /adoptar está protegida en SecurityConfig.
        String usuario = principal.getName();

        MascotaEntity mascota = petService.findById(id).orElse(null);
        if (mascota == null) {
            model.addAttribute("error", "Mascota no encontrada");
            return "detalle";
        }

        try {
            petService.adoptar(id, usuario);
            MascotaEntity mascotaActualizada = petService.findById(id).orElse(null);
            model.addAttribute("mascota", mascotaActualizada);
            model.addAttribute("mensaje", "¡Felicidades " + usuario + ", has adoptado correctamente!");
            return "detalle";
        } catch (AlreadyAdoptedException ex) {
            model.addAttribute("mascota", mascota);
            model.addAttribute("error", ex.getMessage());
            return "detalle";
        }
    }
}
