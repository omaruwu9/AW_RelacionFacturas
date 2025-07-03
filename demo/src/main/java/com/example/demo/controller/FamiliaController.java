package com.example.demo.controller;

import com.example.demo.entity.Familia;
import com.example.demo.entity.Rol;
import com.example.demo.repository.FamiliaRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/familia")
public class FamiliaController {
    private final FamiliaRepository familiaRepository;

    public FamiliaController(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    @GetMapping
    public List<Familia> getAll() {
        return (List<Familia>) familiaRepository.findAll();
    }

    @GetMapping("/mostrar/familias")
    public String cargarRolesModificacion(Model model) {
        List<Familia> familias = familiaRepository.findAll();  // Asume que tienes una entidad Rol
        model.addAttribute("familias", familias);
        return "usuarios"; // tu vista .html
    }
}
