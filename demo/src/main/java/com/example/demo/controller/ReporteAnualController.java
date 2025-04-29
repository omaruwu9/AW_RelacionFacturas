package com.example.demo.controller;

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
        List<Object[]> resultados = llenadoRepository.obtenerTotalesPorFamiliaYAnio(anio);

        // Mapea ID -> Nombre de familia
        Map<Integer, String> idToNombre = familiaRepository.findAll().stream()
                .collect(Collectors.toMap(f -> f.getId_familia(), f -> f.getFamilia()));

        Map<String, Double> resultado = new HashMap<>();
        for (Object[] fila : resultados) {
            Integer idFamilia = (Integer) fila[0];
            Double total = ((Number) fila[1]).doubleValue();
            String nombreFamilia = idToNombre.get(idFamilia);
            if (nombreFamilia != null) {
                resultado.put(nombreFamilia, total);
            }
        }
        return resultado;
    }


}

