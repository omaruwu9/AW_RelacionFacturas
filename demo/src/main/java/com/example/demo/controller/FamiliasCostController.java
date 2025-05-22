package com.example.demo.controller;

import com.example.demo.entity.FamiliaGrafica;
import com.example.demo.repository.LlenadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FamiliasCostController {
    @Autowired
    private LlenadoRepository llenadoRepository;

    //es con lo que se alimentan los totales de el modulo de reportes (la parte de la grafica y la tabla de Familias)
    @GetMapping("/api/llenado/totales-por-familia")
    public List<FamiliaGrafica> obtenerTotalesPorFamilia(
            @RequestParam int anio,
            @RequestParam(required = false) Integer mes
    ) {
        if (mes != null && mes >= 1 && mes <= 12) {
            return llenadoRepository.obtenerTotalesPorFamiliaYMes(anio, mes);
        } else {
            return llenadoRepository.obtenerTotalesPorFamilia(); // O puedes hacer: llenadoRepository.obtenerTotalesPorFamiliaYAnio(anio)
        }
    }
}
