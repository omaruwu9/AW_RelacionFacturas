package com.example.demo.controller;

import com.example.demo.entity.FamiliaGrafica;
import com.example.demo.repository.FamiliaRepository;
import com.example.demo.repository.LlenadoRepository;
import com.example.demo.service.ReporteAnualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reporteAnual")
public class ReporteAnualController {
    @Autowired
    private LlenadoRepository llenadoRepository;

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private ReporteAnualService reporteAnualService;

    //maneja los totales de las familias para realizar la insersión en el reporte anual que se encuentra cargado en resources
    @GetMapping("/anual")
    public ResponseEntity<byte[]> generarReporteAnual(@RequestParam int anio) {
        try {
            Map<String, Double> totalesActual = traerTotalesPorFamilia(anio);
            Map<String, Double> totalesAnterior = traerTotalesPorFamilia(anio - 1);

            byte[] excel = reporteAnualService.generarReporteAnual(anio, totalesActual, totalesAnterior);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Anual_" + anio + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    private Map<String, Double> traerTotalesPorFamilia(int anio) {
        List<FamiliaGrafica> resultados = llenadoRepository.obtenerTotalesPorFamiliaYAnio(anio);

        Map<String, Double> resultado = new HashMap<>();
        for (FamiliaGrafica fila : resultados) {
            resultado.put(fila.getFamilia(), fila.getTotal().doubleValue()); // convierto BigDecimal a double
        }
        return resultado;
    }




}

