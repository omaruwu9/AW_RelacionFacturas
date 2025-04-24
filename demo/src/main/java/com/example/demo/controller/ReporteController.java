package com.example.demo.controller;

import com.example.demo.service.ReporteService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api/reporte")
public class ReporteController {

    @Autowired
    private ReporteService excelService;

    @GetMapping("/excel-mensual")
    public ResponseEntity<InputStreamResource> generarExcelMensual(@RequestParam("mes") int mes) throws IOException {
        ByteArrayInputStream in = excelService.generarExcelPorMes(mes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte_mensual.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }

}
