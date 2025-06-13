package com.example.demo.service;

import com.example.demo.entity.ReporteGeneralDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ReporteDinamicoRepository;
import com.example.demo.repository.ReporteRepository;
import com.example.demo.repository.UsuarioRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private ReporteDinamicoRepository reporteDinamicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ReporteGeneralDTO> generarReporteFiltradoMes(Authentication auth, int mes) {
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
                return reporteRepository.obtenerReportePorMes(mes)
                        .stream().map(ReporteGeneralDTO::new).toList();
            default:
                return List.of();
        }

        return reporteDinamicoRepository.obtenerReporteConCentrosMes(mes, centros);
    }

}
