package com.example.demo.controller;

import com.example.demo.entity.Llenado;
import com.example.demo.entity.OrdenCompra;
import com.example.demo.repository.OrdenCompraRepository;
import com.example.demo.service.LlenadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LlenadoController {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    private final LlenadoService llenadoService;

    @GetMapping("/api/ordenes-compra/{id}")
    public ResponseEntity<OrdenCompra> obtenerOrdenPorId(@PathVariable Integer id) {
        return ordenCompraRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/llenado/{id}")
    public String mostrarFormulario(@PathVariable("id") Integer id, Model model) {
        Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(id);
        if (ordenOpt.isPresent()) {
            model.addAttribute("orden", ordenOpt.get());
            return "formulario_llenado"; // nombre del HTML (sin .html)
        } else {
            return "redirect:/error"; // o puedes mostrar una vista de error personalizada
        }
    }

    @GetMapping("/volver")
    public String volverAOrdenes() {
        return "mod_llenado";
    }

    public LlenadoController(LlenadoService llenadoService) {
        this.llenadoService = llenadoService;
    }

    @PostMapping("/llenadoG")
    public ResponseEntity<?> guardarLlenado(@RequestBody Llenado llenado) {
        try {
            Llenado guardado = llenadoService.guardar(llenado);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el registro");
        }
    }


}
