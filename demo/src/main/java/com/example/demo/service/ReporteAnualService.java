package com.example.demo.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@Service
public class ReporteAnualService {

    private static final Map<String, String> CELDAS_REPORTE = crearMapaCeldas();


    //se mapea las columnas en donde se hará la insersión de datos
    private static Map<String, String> crearMapaCeldas() {
        Map<String, String> mapa = new java.util.HashMap<>();
        mapa.put("2.1 Labor Cost Outside", "I71");
        mapa.put("3.1 Software", "I95");
        mapa.put("3.2 Renovation Software", "I104");
        mapa.put("3.3 Servers", "I114");
        mapa.put("3.3 Laptops", "I118");
        mapa.put("3.3 Escaner", "I21");
        mapa.put("3.3 Printers", "I24");
        mapa.put("3.3 Celulares y Other", "I127");
        mapa.put("3.4 Hardware Maintenance fee", "I137");
        mapa.put("4.1 Hosting", "I145");
        mapa.put("4.2 Gastos de Telefonía", "I149");
        mapa.put("4.3 Cost Implementation", "I153");
        mapa.put("4.4", "I158"); // todas las familias 4.4
        return mapa;
    }


    //se genera el reporte anual, en classpathresourse debe estar el documento como corresponde o como sea que se llame
    //en la parte de resources
    public byte[] generarReporteAnual(int anio, Map<String, Double> totalesAnioActual, Map<String, Double> totalesAnioAnterior) throws Exception {
        try (
                InputStream input = new ClassPathResource("Reporte_japon_copia.xlsx").getInputStream();
                Workbook workbook = new XSSFWorkbook(input);
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Map.Entry<String, String> entry : CELDAS_REPORTE.entrySet()) {
                String familiaKey = entry.getKey();
                String celda = entry.getValue();

                // Si es clave "4.4", suma todas las que inician con eso
                double valorActual = 0;
                double valorAnterior = 0;

                if (familiaKey.equals("4.4")) {
                    for (String familia : totalesAnioActual.keySet()) {
                        if (familia.startsWith("4.4")) {
                            valorActual += totalesAnioActual.getOrDefault(familia, 0.0);
                            valorAnterior += totalesAnioAnterior.getOrDefault(familia, 0.0);
                        }
                    }
                } else if (familiaKey.equals("3.3 Celulares y Other")) {
                    valorActual += totalesAnioActual.getOrDefault("3.3 Celulares", 0.0);
                    valorActual += totalesAnioActual.getOrDefault("3.3 Other", 0.0);
                    valorAnterior += totalesAnioAnterior.getOrDefault("3.3 Celulares", 0.0);
                    valorAnterior += totalesAnioAnterior.getOrDefault("3.3 Other", 0.0);
                } else {
                    valorActual = totalesAnioActual.getOrDefault(familiaKey, 0.0);
                    valorAnterior = totalesAnioAnterior.getOrDefault(familiaKey, 0.0);
                }

                int[] posicion = obtenerFilaColumna(celda);
                int fila = posicion[0];
                int col = posicion[1];

                // Escribimos total año actual
                Row rowActual = obtenerFila(sheet, fila);
                Cell cellActual = obtenerCelda(rowActual, col);
                cellActual.setCellValue(valorActual);

                // Año anterior en fila siguiente
                Row rowAnterior = obtenerFila(sheet, fila + 1);
                Cell cellAnterior = obtenerCelda(rowAnterior, col);
                if (valorAnterior > 0) {
                    cellAnterior.setCellValue(valorAnterior);
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private int[] obtenerFilaColumna(String celda) {
        int col = celda.charAt(0) - 'A';
        int fila = Integer.parseInt(celda.substring(1)) - 1; // índice base 0
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
