package com.example.demo.controller;

import com.example.demo.entity.Rol;
import com.example.demo.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    @ResponseBody
    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    @GetMapping("/mostrar/roles")
    public String cargarRolesModificacion(Model model) {
        List<Rol> roles = rolRepository.findAll();
        model.addAttribute("roles", roles);
        return "usuarios";
    }

    @PostMapping
    public Rol create(@RequestBody Rol rol) {
        return rolRepository.save(rol);
    }

    @PutMapping("/{id}")
    public Rol update(@PathVariable Integer id, @RequestBody Rol nuevo) {
        return rolRepository.findById(id).map(r -> {
            r.setRol(nuevo.getRol());
            return rolRepository.save(r);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        rolRepository.deleteById(id);
    }

}
