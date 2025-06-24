package com.example.demo.controller;

import com.example.demo.entity.FamiliaGrafica;
import com.example.demo.repository.CuentaContableRepository;
import com.example.demo.repository.FamiliaRepository;
import com.example.demo.repository.LlenadoRepository;
import com.example.demo.service.CuentaContableService;
import com.example.demo.service.ReporteAnualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentasContables")
public class ReporteCuentaContableController {

    @Autowired
    @Qualifier("sqlServerJdbcTemplate")
    private JdbcTemplate sqlServerJdbcTemplate;

    @Autowired
    private CuentaContableService cuentaContableService;

    @GetMapping("/reporte")
    public ResponseEntity<byte[]> generarReporte(@RequestParam int anio, @RequestParam int mes, @RequestParam String centro_costos) {
        try {
            Map<String, Double> totalesActual = traerTotalesPorCuenta(anio, mes, centro_costos);
            byte[] excel = cuentaContableService.generarReporteCuentaContable(anio, totalesActual);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AuthorizedBudget" + anio + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private Map<String, Double> traerTotalesPorCuenta(int anio, int mes, String centro_costos) {
        String sql = """
            SELECT
                ocl.CUENTA_CONTABLE,
                SUM(
                    CASE
                        WHEN oc.MONEDA = 'USD' THEN ocl.PRECIO_UNITARIO * tch.MONTO * ocl.CANTIDAD_ORDENADA
                        WHEN oc.MONEDA = 'MXN' THEN ocl.PRECIO_UNITARIO * ocl.CANTIDAD_ORDENADA
                        ELSE 0
                    END
                ) AS IMPORTE_TOTAL_LOCAL
            FROM PROMEX.ORDEN_COMPRA oc
            JOIN PROMEX.ORDEN_COMPRA_LINEA ocl ON oc.ORDEN_COMPRA = ocl.ORDEN_COMPRA
            LEFT JOIN PROMEX.TIPO_CAMBIO_HIST tch ON CAST(oc.fecha AS DATE) = CAST(tch.FECHA AS DATE)
            WHERE YEAR(oc.FECHA) = ? AND MONTH(oc.FECHA) = ? AND ocl.CENTRO_COSTO = ?
            GROUP BY ocl.CUENTA_CONTABLE, ocl.CENTRO_COSTO
        """;

        List<Map<String, Object>> rows = sqlServerJdbcTemplate.queryForList(sql, anio, mes, centro_costos);

        Map<String, Double> resultado = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String cuenta = (String) row.get("CUENTA_CONTABLE");
            Double total = row.get("IMPORTE_TOTAL_LOCAL") != null
                    ? ((Number) row.get("IMPORTE_TOTAL_LOCAL")).doubleValue()
                    : 0.0;
            resultado.put(cuenta, total);
        }

        return resultado;
    }
}
