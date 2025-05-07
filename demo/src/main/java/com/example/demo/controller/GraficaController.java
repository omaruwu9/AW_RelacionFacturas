package com.example.demo.controller;

import com.example.demo.repository.LlenadoRepository;
import com.example.demo.entity.FamiliaGrafica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grafica")
public class GraficaController {

    @Autowired
    private LlenadoRepository llenadoRepository;

    //es la gráfica
    @GetMapping("/polar-area/anual")
    public List<FamiliaGrafica> obtenerDatosGrafica() {
        return llenadoRepository.obtenerTotalesPorFamilia();
    }

    @GetMapping("/polar-area")
    public List<FamiliaGrafica> obtenerDatoGraficaPorMes(
            @RequestParam int anio,
            @RequestParam int mes) {
        return llenadoRepository.obtenerTotalesPorFamiliaYMes(anio, mes);
    }
}

