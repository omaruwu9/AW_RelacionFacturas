package com.example.demo.controller;

import com.example.demo.entity.Familia;
import com.example.demo.entity.Rol;
import com.example.demo.repository.FamiliaRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/familias")
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
        List<Familia> familias = familiaRepository.findAll();
        model.addAttribute("familias", familias);
        return "formulario_llenado";
    }

    @PostMapping
    public Familia create(@RequestBody Familia familia) {
        return familiaRepository.save(familia);
    }

    @PutMapping("/{id}")
    public Familia update(@PathVariable Integer id, @RequestBody Familia nueva) {
        return familiaRepository.findById(id).map(f -> {
            f.setFamilia(nueva.getFamilia());
            return familiaRepository.save(f);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        familiaRepository.deleteById(id);
    }
}
