package com.example.demo.controller;

import com.example.demo.entity.OrdenCompra;
import com.example.demo.repository.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class LlenadoController {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

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

}
