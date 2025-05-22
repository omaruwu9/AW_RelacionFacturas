// Servicio de reporte anual con mapeo de familias a celdas específicas
package com.example.demo.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

@Service
public class ReporteAnualService {

    private static final Map<String, String> CELDAS_REPORTE = crearMapaCeldas();

    private static Map<String, String> crearMapaCeldas() {
        Map<String, String> mapa = new HashMap<>();
        mapa.put("2.1 Labor Cost Outside", "I71");
        mapa.put("3.1 Software", "I95");
        mapa.put("3.2 Renovation Software", "I104");
        mapa.put("3.3 Celulares", "I127");
        mapa.put("3.3 Escaner", "I121");
        mapa.put("3.3 Impresora Label", "I124");
        mapa.put("3.3 Laptops", "I118");
        mapa.put("3.3 Other", "I132");
        mapa.put("3.3 Print Rent", "I124");
        mapa.put("3.3 Printers", "I124");
        mapa.put("3.3 Servers", "I114");
        mapa.put("3.3 Network equipment", "I111");
        mapa.put("3.4 Hardware Maintenance", "I137");
        mapa.put("3.4 Hardware Maintenance fee", "I137");
        mapa.put("4.1 Hosting", "I145");
        mapa.put("4.2 Gastos de Telefonia", "I149");
        mapa.put("4.3 Cost Implementation", "I153");
        mapa.put("4.4 Other Indirect Cost Papel", "I158");
        mapa.put("4.4 Other Indirect Cost Toner", "I158");
        mapa.put("4.4 Other Indirect Cost Clics", "I158");
        mapa.put("4.4 Other Indirect Cost Label", "I158");
        mapa.put("MANTENIMIENTO", "I137");
        mapa.put("CAPACITACION", "I71");
        return mapa;
    }

    public byte[] generarReporteAnual(int anio, Map<String, Double> totalesAnioActual, Map<String, Double> totalesAnioAnterior) throws Exception {
        try (
                InputStream input = new ClassPathResource("Reporte_japon_copia.xlsx").getInputStream();
                Workbook workbook = new XSSFWorkbook(input);
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Double> acumuladoActual = new HashMap<>();
            Map<String, Double> acumuladoAnterior = new HashMap<>();

            for (Map.Entry<String, String> entry : CELDAS_REPORTE.entrySet()) {
                String familiaKey = entry.getKey();
                String celda = entry.getValue();

                double valorActual = totalesAnioActual.getOrDefault(familiaKey, 0.0);
                double valorAnterior = totalesAnioAnterior.getOrDefault(familiaKey, 0.0);

                acumuladoActual.merge(celda, valorActual, Double::sum);
                acumuladoAnterior.merge(celda, valorAnterior, Double::sum);
            }

            for (Map.Entry<String, Double> acumulado : acumuladoActual.entrySet()) {
                String celda = acumulado.getKey();
                int[] posicion = obtenerFilaColumna(celda);
                int fila = posicion[0];
                int col = posicion[1];

                Row rowActual = obtenerFila(sheet, fila);
                Cell cellActual = obtenerCelda(rowActual, col);
                cellActual.setCellValue(acumulado.getValue());

                double anterior = acumuladoAnterior.getOrDefault(celda, 0.0);
                if (anterior > 0) {
                    Row rowAnterior = obtenerFila(sheet, fila + 1);
                    Cell cellAnterior = obtenerCelda(rowAnterior, col);
                    cellAnterior.setCellValue(anterior);
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private int[] obtenerFilaColumna(String celda) {
        int col = celda.charAt(0) - 'A';
        int fila = Integer.parseInt(celda.substring(1)) - 1;
        return new int[]{fila, col};
    }

    private Row obtenerFila(Sheet sheet, int filaIndex) {
        Row row = sheet.getRow(filaIndex);
        return row != null ? row : sheet.createRow(filaIndex);
    }

    private Cell obtenerCelda(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        return cell != null ? cell : row.createCell(colIndex);
    }
}
