package com.example.demo.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class CuentaContableService {

    private static final Map<String, String> CELDAS_REPORTE = crearMapaCeldas();

    private static Map<String, String> crearMapaCeldas() {
        Map<String, String> mapa = new HashMap<>();
        //CUENTAS CONTABLES DE IT
        mapa.put("5-20-01-03-38-00", "K13"); //ARRENDAMIENTO DE IMPRESORAS
        mapa.put("5-20-01-03-24-00", "K81"); //SOFTWARE
        mapa.put("5-20-01-03-01-00", "K173"); //PAPELERIA Y ARTICULOS DE OFICINA
        mapa.put("5-20-01-03-13-00", "K183"); //CAPACITACION Y ADIESTRAMIENTO
        mapa.put("5-20-01-03-14-00", "K184"); //MANTENIMIENTO DE EQUIPO DE TRANSPORTE
        mapa.put("5-20-01-03-15-00", "K185"); //MANTENIMIENTO EQUIPO DE COMPUTO
        mapa.put("5-20-01-03-19-00", "K189"); //EQUIPO DE PROTECCION PERSONAL
        mapa.put("5-20-01-03-23-00", "K193"); //INSCRIPCIONES, CUOTAS Y MEMBRESIAS
        mapa.put("5-20-01-03-27-00", "K196"); //GASTOS POR INSUMOS DE COMPUTACION
        mapa.put("5-20-01-03-29-00", "K198"); //ASESORIA Y ASISTENCIA TECNICA
        mapa.put("5-20-01-03-35-00", "K203"); //VERIFICACION VEHICULAR
        mapa.put("5-20-01-03-67-00", "K230"); //EQUIPO DE COMPUTO MENOR
        mapa.put("5-20-01-03-75-00", "K237"); //CLICS PARA IMPRESIONES
        mapa.put("5-20-01-04-02-00", "K240"); //ARRENDAMIENTO PERSONAS MORALES
        mapa.put("5-20-01-04-04-00", "K242"); //ARRENDAMIENTO PERSONAS FISICAS
        mapa.put("5-21-01-03-46-00", "K18"); //ARRENDAMIENTO IMPRESORAS
        mapa.put("5-21-01-03-24-00", "K80"); //SOFTWARE OP (08-080)
        mapa.put("5-21-01-03-05-00", "K107"); //CLICS PARA IMPRESIONES
        mapa.put("5-21-01-03-28-00", "K121"); //CELULARES Y RADIOS
        mapa.put("5-21-01-03-31-00", "K124"); //IMPRESIONES PARA PROCESO (CML & TT LABEL)
        return mapa;
    }

    public byte[] generarReporteCuentaContable(int anio, Map<String, Double> totalesCuentas) throws Exception {
        try (
                InputStream input = new ClassPathResource("AuthorizedBudget.xlsx").getInputStream();
                Workbook workbook = new XSSFWorkbook(input);
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Double> acumuladoActual = new HashMap<>();


            for (Map.Entry<String, String> entry : CELDAS_REPORTE.entrySet()) {
                String cuentaContable = entry.getKey();
                String celda = entry.getValue();

                double valorActual = totalesCuentas.getOrDefault(cuentaContable, 0.0);

                acumuladoActual.merge(celda, valorActual, Double::sum);
            }

            for (Map.Entry<String, Double> acumulado : acumuladoActual.entrySet()) {
                String celda = acumulado.getKey();
                int[] posicion = obtenerFilaColumna(celda);
                int fila = posicion[0];
                int col = posicion[1];

                Row rowActual = obtenerFila(sheet, fila);
                Cell cellActual = obtenerCelda(rowActual, col);
                cellActual.setCellValue(acumulado.getValue());
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

