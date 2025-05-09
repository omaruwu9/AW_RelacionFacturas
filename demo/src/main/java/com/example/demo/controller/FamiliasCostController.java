package com.example.demo.controller;

import com.example.demo.entity.FamiliaGrafica;
import com.example.demo.repository.LlenadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FamiliasCostController {
    @Autowired
    private LlenadoRepository llenadoRepository;

    @GetMapping("/api/llenado/totales-por-familia")
    public List<FamiliaGrafica> obtenerTotales() {
        return llenadoRepository.obtenerTotalesPorFamilia();

    }
}
