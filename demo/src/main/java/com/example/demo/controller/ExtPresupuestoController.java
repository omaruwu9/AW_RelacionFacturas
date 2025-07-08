package com.example.demo.controller;

import com.example.demo.entity.ExtPresupuesto;
import com.example.demo.entity.Familia;
import com.example.demo.repository.ExtPresupuestoRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/extPresupuesto")
public class ExtPresupuestoController {
    private final ExtPresupuestoRepository extPresupuestoRepository;

    public ExtPresupuestoController(ExtPresupuestoRepository extPresupuestoRepository) {
        this.extPresupuestoRepository = extPresupuestoRepository;
    }

    @GetMapping
    public List<ExtPresupuesto> getAll() {
        return (List<ExtPresupuesto>) extPresupuestoRepository.findAll();
    }

    @GetMapping("/mostrar/extPres")
    public String cargarRolesModificacion(Model model) {
        List<ExtPresupuesto> ext = extPresupuestoRepository.findAll();
        model.addAttribute("ext", ext);
        return "formulario_llenado";
    }
}

