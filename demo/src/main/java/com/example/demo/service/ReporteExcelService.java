package com.example.demo.service;

import com.example.demo.entity.ReporteGeneralDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ReporteDinamicoRepository;
import com.example.demo.repository.ReporteExcelRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteExcelService {

    @Autowired
    private ReporteExcelRepository repository;

    public List<ReporteGeneralDTO> obtenerDatosPorPeriodo(int anio, int mesInicio, int mesFin) {
        List<Object[]> resultados = repository.obtenerReportePorPeriodo(anio, mesInicio, mesFin);
        return resultados.stream().map(obj -> new ReporteGeneralDTO(obj)).toList();
    }

    public ByteArrayInputStream exportarExcel(List<ReporteGeneralDTO> datos, HttpServletResponse response) throws IOException {
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Reporte");

            // Estilos
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            HSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            HSSFCellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setBorderTop(BorderStyle.THIN);
            bodyStyle.setBorderBottom(BorderStyle.THIN);
            bodyStyle.setBorderLeft(BorderStyle.THIN);
            bodyStyle.setBorderRight(BorderStyle.THIN);

            String[] columnas = {
                    "Solicitud_OC", "Fecha_requerida", "Descripcion", "Orden_Compra", "Fecha_Orden", "Fecha_Emision",
                    "Centro_Costos", "Cuenta_Contable", "Descripcion_Cuenta_Contable", "Proveedor", "Fecha_Factura",
                    "Factura", "Folio_Fiscal", "Cantidad", "Poliza_Garantia", "Familia", "Responsable", "Ext_Presupuesto",
                    "Moneda", "PU", "Subtotal", "IVA%", "Total", "Tipo_Cambio", "Pesos_Totales", "Estatus"
            };

            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar filas con datos
            int rowIdx = 1;
            for (ReporteGeneralDTO dto : datos) {
                Row row = sheet.createRow(rowIdx++);
                dto.llenarRow(row, bodyStyle);  // pasamos el estilo
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Congelar encabezados
            sheet.createFreezePane(0, 1);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Autowired
    private ReporteDinamicoRepository reporteDinamicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ReporteGeneralDTO> generarReporteFiltrado(Authentication auth, int anio, int mesInicio, int mesFin) {
        String nomina = auth.getName();
        Usuario usuario = usuarioRepository.findByNomina(nomina).orElse(null);
        if (usuario == null) return List.of();

        int rol = usuario.getId_rol();
        List<String> centros;

        switch (rol) {
            case 2: centros = List.of("05-050", "08-085"); break;
            case 3: centros = List.of("02-020", "08-082"); break;
            case 4: centros = List.of("04-040", "08-084"); break;
            case 5: centros = List.of("03-030", "08-083"); break;
            case 6: centros = List.of("06-060"); break;
            case 7: centros = List.of("08-087"); break;
            case 8: centros = List.of("07-070"); break;
            case 1: // Admin
                // Aquí puedes seguir usando el método existente que no filtra
                return repository.obtenerReportePorPeriodo(anio, mesInicio, mesFin)
                        .stream().map(ReporteGeneralDTO::new).toList();
            default:
                return List.of();
        }

        return reporteDinamicoRepository.obtenerReporteConCentros(anio, mesInicio, mesFin, centros);
    }


}
