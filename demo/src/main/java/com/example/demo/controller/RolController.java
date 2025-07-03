package com.example.demo.controller;

import com.example.demo.entity.Rol;
import com.example.demo.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RolController {

    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/mostrar/roles")
    public String cargarRolesModificacion(Model model) {
        List<Rol> roles = rolRepository.findAll();  // Asume que tienes una entidad Rol
        model.addAttribute("roles", roles);
        return "usuarios"; // tu vista .html
    }

}
