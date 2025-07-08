package com.example.demo.controller;

import com.example.demo.repository.BitacoraRepository;
import com.example.demo.entity.Bitacora;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BitacoraController {

    private final BitacoraRepository bitacoraRepository;

    public BitacoraController(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    @GetMapping("/api/bitacora")
    @ResponseBody
    public List<Bitacora> obtenerRegistros() {
        return bitacoraRepository.findAll();
    }


    @GetMapping("/bitacora")
    public String mostrarBitacora(Model model) {
        List<Bitacora> registros = bitacoraRepository.findAll();
        model.addAttribute("registros", registros);
        return "mod_mantenimiento";
    }
}
