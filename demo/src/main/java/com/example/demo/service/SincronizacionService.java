package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SincronizacionService {

    @Autowired
    @Qualifier("sqlServerJdbcTemplate")
    private JdbcTemplate sqlServerJdbc;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate postgresJdbc;

    @Scheduled(cron = "0 35 10 * * *") // Todos los días a las 10:00 AM
    public void sincronizarOrdenesDeAyer() {
        String consultaSqlServer = """
            SELECT
                LEFT(LTRIM(REPLACE(REPLACE(CAST(oc.OBSERVACpromex12
            IONES AS VARCHAR), CHAR(13), ''), CHAR(10), '')), 6) AS SOLICITUD_EXTRAIDA,
                soce.SOLICITUD_OC,
                CAST(soce.FECHA_REQUERIDA AS DATE) AS FECHA_REQUERIDA,
                oce.ORDEN_COMPRA,
                CAST(oce.DESCRIPCION AS VARCHAR(MAX)) AS DESCRIPCION,
                CAST(oc.FECHA AS DATE) AS FECHA_OC,
                CAST(oc.FECHA_EMISION AS DATE) AS FECHA_EMISION,
                oce.CENTRO_COSTO,
                oce.CUENTA_CONTABLE,
                cc.DESCRIPCION AS DESC_CUENTA_CONTABLE,
                p.NOMBRE AS NOMBRE_PROVEEDOR,
                oc.ESTADO
            FROM PROMEX.ORDEN_COMPRA_LINEA oce
            OUTER APPLY (
                SELECT TOP 1 SOLICITUD_OC, FECHA_REQUERIDA
                FROM PROMEX.SOLICITUD_OC_LINEA
                WHERE CENTRO_COSTO = oce.CENTRO_COSTO
                    AND YEAR(FECHA_REQUERIDA) = 2025
                ORDER BY ABS(DATEDIFF(DAY, oce.FECHA, FECHA_REQUERIDA)) ASC
            ) soce
            JOIN PROMEX.ORDEN_COMPRA oc ON oce.ORDEN_COMPRA = oc.ORDEN_COMPRA
            JOIN PROMEX.PROVEEDOR p ON oc.PROVEEDOR = p.PROVEEDOR
            JOIN PROMEX.CUENTA_CONTABLE cc ON oce.CUENTA_CONTABLE = cc.CUENTA_CONTABLE
            WHERE oce.CENTRO_COSTO IN ('05-050', '08-085')
              AND CAST(oc.FECHA AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE)
        """;

        List<Map<String, Object>> resultados = sqlServerJdbc.queryForList(consultaSqlServer);

        for (Map<String, Object> fila : resultados) {
            postgresJdbc.update("""
                INSERT INTO orden_compra (
                    solicitud_oc,
                    fecha_requerida,
                    orden_compra,
                    fecha_emision,
                    centro_costo,
                    cuenta_contable,
                    desc_cuenta_contable,
                    nombre_proveedor, 
                    estado,
                    descripcion,
                    fecha_oc
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
                    fila.get("SOLICITUD_EXTRAIDA"),
                    fila.get("FECHA_REQUERIDA"),
                    fila.get("ORDEN_COMPRA"),
                    fila.get("FECHA_EMISION"),
                    fila.get("CENTRO_COSTO"),
                    fila.get("CUENTA_CONTABLE"),
                    fila.get("DESC_CUENTA_CONTABLE"),
                    fila.get("NOMBRE_PROVEEDOR"),
                    fila.get("ESTADO"),
                    fila.get("DESCRIPCION"),
                    fila.get("FECHA_OC")

            );
        }

        System.out.println("▶ Sincronización del día anterior completada. Registros procesados: " + resultados.size());
    }
}
