package com.example.demo.controller;

import com.example.demo.entity.ReporteGeneralDTO;
import com.example.demo.service.ReporteExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;


@RestController
@RequestMapping("/api/reporte")
public class ReportesExcelController {

    @Autowired
    private ReporteExcelService service;

    @GetMapping("/descargar")
    public ResponseEntity<InputStreamResource> descargarExcel(@RequestParam int anio,
                                                              @RequestParam int mesInicio,
                                                              @RequestParam int mesFin) {
        try {
            List<ReporteGeneralDTO> datos = service.obtenerDatosPorPeriodo(anio, mesInicio, mesFin);
            ByteArrayInputStream excel = service.exportarExcel(datos);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Reporte.xls");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(excel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
