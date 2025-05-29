package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatosNullService {
    @Autowired
    @Qualifier("sqlServerJdbcTemplate")
    private JdbcTemplate sqlServerJdbc;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate postgresJdbc;

    /**
     * Actualiza en PostgreSQL las órdenes que tienen estado o fecha_emision en null,
     * obteniendo los datos faltantes desde SQL Server.
     */
    @Scheduled(cron = "0 35 10 * * *") // Ejecución programada junto con la de inserciín de datos
    public void actualizarOrdenesIncompletas() {

        // Paso 1: Obtener órdenes incompletas desde PostgreSQL
        String queryOrdenesNulas = """
            SELECT orden_compra
            FROM orden_compra
            WHERE estado IS NULL OR fecha_emision IS NULL
        """;

        List<Map<String, Object>> ordenesIncompletas = postgresJdbc.queryForList(queryOrdenesNulas);

        if (ordenesIncompletas.isEmpty()) {
            System.out.println("✅ No hay órdenes con campos nulos.");
            return;
        }

        System.out.println("🔍 Órdenes con campos nulos: " + ordenesIncompletas.size());

        for (Map<String, Object> orden : ordenesIncompletas) {
            String ordenCompra = (String) orden.get("orden_compra");

            // Paso 2: Consultar los datos actualizados en SQL Server
            String consultaSqlServer = """
                SELECT
                    CAST(oc.FECHA_EMISION AS DATE) AS FECHA_EMISION,
                    oc.ESTADO
                FROM PROMEX.ORDEN_COMPRA oc
                WHERE oc.ORDEN_COMPRA = ?
            """;

            List<Map<String, Object>> resultados = sqlServerJdbc.queryForList(consultaSqlServer, ordenCompra);

            if (!resultados.isEmpty()) {
                Map<String, Object> fila = resultados.get(0);

                Object nuevaFechaEmision = fila.get("FECHA_EMISION");
                Object nuevoEstado = fila.get("ESTADO");

                // Paso 3: Actualizar PostgreSQL solo si hay datos válidos
                postgresJdbc.update("""
                    UPDATE orden_compra
                    SET fecha_emision = COALESCE(?, fecha_emision),
                        estado = COALESCE(?, estado)
                    WHERE orden_compra = ?
                """, nuevaFechaEmision, nuevoEstado, ordenCompra);

                System.out.println("✅ Orden actualizada: " + ordenCompra);
            } else {
                System.out.println("⚠ No se encontró en SQL Server: " + ordenCompra);
            }
        }

        System.out.println("🛠 Actualización de órdenes incompletas finalizada.");
    }
}
