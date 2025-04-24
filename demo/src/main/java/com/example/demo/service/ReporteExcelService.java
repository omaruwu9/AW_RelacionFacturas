package com.example.demo.service;

import com.example.demo.entity.ReporteGeneralDTO;
import com.example.demo.repository.ReporteExcelRepository;
import com.example.demo.repository.ReporteRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Month;
import java.util.List;

@Service
public class ReporteExcelService {

    @Autowired
    private ReporteExcelRepository repository;

    public List<ReporteGeneralDTO> obtenerDatosPorPeriodo(int anio, int mesInicio, int mesFin) {
        List<Object[]> resultados = repository.obtenerReportePorPeriodo(anio, mesInicio, mesFin);
        return resultados.stream().map(obj -> new ReporteGeneralDTO(obj)).toList();
    }

    public ByteArrayInputStream exportarExcel(List<ReporteGeneralDTO> datos) throws IOException {
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Reporte");

            Row header = sheet.createRow(0);
            String[] columnas = {
                    "Solicitud_OC", "Fecha_requerida", "Descripcion", "Orden_Compra", "Fecha_Orden", "Fecha_Emision",
                    "Centro_Costos", "Cuenta_Contable", "Descripcion_Cuenta_Contable", "Proveedor", "Fecha_Factura",
                    "Factura", "Folio_Fiscal", "Cantidad", "Poliza_Garantia", "Familia", "Responsable", "Ext_Presupuesto",
                    "Moneda", "PU", "Subtotal", "IVA%", "Total", "Tipo_Cambio", "Pesos_Totales", "Estatus"
            };

            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            int rowIdx = 1;
            for (ReporteGeneralDTO dto : datos) {
                Row row = sheet.createRow(rowIdx++);
                dto.llenarRow(row);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
