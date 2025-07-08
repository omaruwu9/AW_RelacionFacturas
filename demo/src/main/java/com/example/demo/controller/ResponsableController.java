package com.example.demo.controller;

import com.example.demo.entity.Responsable;
import com.example.demo.entity.Rol;
import com.example.demo.repository.ResponsableRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<Responsable> responsables = responsableRepository.findAll();
        model.addAttribute("responsables", responsables);
        return "usuarios"; // tu vista .html
    }

    @PostMapping
    public Responsable create(@RequestBody Responsable responsable) {
        return responsableRepository.save(responsable);
    }

    @PutMapping("/{id}")
    public Responsable update(@PathVariable Integer id, @RequestBody Responsable nuevo) {
        return responsableRepository.findById(id).map(r -> {
            r.setResponsable(nuevo.getResponsable());
            return responsableRepository.save(r);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        responsableRepository.deleteById(id);
    }
}
