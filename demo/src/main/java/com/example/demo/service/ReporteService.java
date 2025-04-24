package com.example.demo.service;

import com.example.demo.repository.ReporteRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public ByteArrayInputStream generarExcelPorMes(int mes) throws IOException {
        List<Object[]> datos = reporteRepository.obtenerReportePorMes(mes);

        String[] columnas = {
                "Solicitud_OC", "Fecha_requerida", "descripcion", "orden_compra", "fecha_orden", "fecha_emision",
                "centro_costos", "cuenta_contable", "descripcion_cuenta_contable", "proveedor",
                "fecha_factura", "factura", "folio_fiscal", "cantidad", "poliza_garantia",
                "familia", "responsable", "ext_presupuesto", "moneda", "PU", "Subtotal", "IVA%", "Total",
                "tipo_cambio", "pesos_totales", "estatus"
        };

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");

        Row header = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
        }

        int rowIdx = 1;
        for (Object[] fila : datos) {
            Row row = sheet.createRow(rowIdx++);
            for (int i = 0; i < fila.length; i++) {
                Cell cell = row.createCell(i);
                if (fila[i] != null) {
                    if (fila[i] instanceof Number) {
                        cell.setCellValue(((Number) fila[i]).doubleValue());
                    } else {
                        cell.setCellValue(fila[i].toString());
                    }
                }
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
