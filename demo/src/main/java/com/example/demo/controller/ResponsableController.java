package com.example.demo.controller;

import com.example.demo.entity.Responsable;
import com.example.demo.entity.Rol;
import com.example.demo.repository.ResponsableRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/responsable")
public class ResponsableController {
    private final ResponsableRepository responsableRepository;

    public ResponsableController(ResponsableRepository responsableRepository) {
        this.responsableRepository = responsableRepository;
    }

    @GetMapping
    public List<Responsable> getAll() {
        return (List<Responsable>) responsableRepository.findAll();
    }

    @GetMapping("/mostrar/responsables")
    public String cargarRolesModificacion(Model model) {
        List<Responsable> responsables = responsableRepository.findAll();  // Asume que tienes una entidad Rol
        model.addAttribute("responsables", responsables);
        return "usuarios"; // tu vista .html
    }
}
