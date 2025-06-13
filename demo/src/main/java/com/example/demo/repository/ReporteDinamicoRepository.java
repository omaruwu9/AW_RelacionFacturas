// src/main/java/com/example/demo/repository/ReporteDinamicoRepository.java
package com.example.demo.repository;

import com.example.demo.entity.ReporteGeneralDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReporteDinamicoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ReporteGeneralDTO> obtenerReporteConCentros(int anio, int mesInicio, int mesFin, List<String> centros) {
        if (centros == null || centros.isEmpty()) {
            return List.of(); // Retornar vacío si no hay centros permitidos
        }

        String inClause = centros.stream()
                .map(c -> "'" + c + "'")
                .collect(Collectors.joining(","));

        String sql = """
            SELECT oc.solicitud_oc, oc.fecha_requerida, oc.descripcion, oc.orden_compra,
                   oc.fecha_orden, oc.fecha_emision, oc.centro_costo, oc.cuenta_contable,
                   oc.desc_cuenta_contable, oc.nombre_proveedor AS proveedor,
                   l.fecha AS fecha_factura, l.factura, l.folio_fiscal, l.cantidad,
                   l.poliza_garantia, f.familia, r.responsable, ep.ext_presupuesto,
                   l.moneda, l.pu, l.subtotal, l.iva_porc AS iva, l.total,
                   l.tipo_cambio, l.pesos_totales,
                   CASE oc.estado
                       WHEN 'O' THEN 'Cancelado' WHEN 'I' THEN 'Backorder'
                       WHEN 'R' THEN 'Recibida' WHEN 'E' THEN 'Tránsito'
                       WHEN 'P' THEN 'Por aprobar' WHEN 'A' THEN 'Planeada'
                       WHEN 'U' THEN 'Cerrada' ELSE 'Desconocido'
                   END AS estatus
            FROM orden_compra oc
            LEFT JOIN llenado l ON l.id = oc.id
            LEFT JOIN familia f ON l.id_familia = f.id_familia
            LEFT JOIN responsable r ON l.id_responsable = r.id_responsable
            LEFT JOIN ext_presupuesto ep ON l.id_extpresupuesto = ep.id_extpresupuesto
            WHERE (:anio IS NULL OR EXTRACT(YEAR FROM COALESCE(l.fecha, oc.fecha_orden)) = :anio)
              AND (:mesInicio IS NULL OR EXTRACT(MONTH FROM COALESCE(l.fecha, oc.fecha_orden)) BETWEEN :mesInicio AND :mesFin)
              AND oc.centro_costo IN (""" + inClause + ") " +
                "ORDER BY CASE WHEN l.id IS NULL THEN 1 ELSE 0 END, oc.fecha_orden";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter("anio", anio);
        query.setParameter("mesInicio", mesInicio);
        query.setParameter("mesFin", mesFin);

        List<Object[]> resultados = query.getResultList();
        return resultados.stream().map(ReporteGeneralDTO::new).collect(Collectors.toList());
    }


    public List<ReporteGeneralDTO> obtenerReporteConCentrosMes(int mes, List<String> centros) {
        if (centros == null || centros.isEmpty()) {
            return List.of(); // Retornar vacío si no hay centros permitidos
        }

        String inClause = centros.stream()
                .map(c -> "'" + c + "'")
                .collect(Collectors.joining(","));

        String sql = """
            SELECT oc.solicitud_oc, oc.fecha_requerida, oc.descripcion, oc.orden_compra,
                   oc.fecha_orden, oc.fecha_emision, oc.centro_costo, oc.cuenta_contable,
                   oc.desc_cuenta_contable, oc.nombre_proveedor AS proveedor,
                   l.fecha AS fecha_factura, l.factura, l.folio_fiscal, l.cantidad,
                   l.poliza_garantia, f.familia, r.responsable, ep.ext_presupuesto,
                   l.moneda, l.pu, l.subtotal, l.iva_porc AS iva, l.total,
                   l.tipo_cambio, l.pesos_totales,
                   CASE oc.estado
                       WHEN 'O' THEN 'Cancelado' WHEN 'I' THEN 'Backorder'
                       WHEN 'R' THEN 'Recibida' WHEN 'E' THEN 'Tránsito'
                       WHEN 'P' THEN 'Por aprobar' WHEN 'A' THEN 'Planeada'
                       WHEN 'U' THEN 'Cerrada' ELSE 'Desconocido'
                   END AS estatus
            FROM orden_compra oc
            LEFT JOIN llenado l ON l.id = oc.id
            LEFT JOIN familia f ON l.id_familia = f.id_familia
            LEFT JOIN responsable r ON l.id_responsable = r.id_responsable
            LEFT JOIN ext_presupuesto ep ON l.id_extpresupuesto = ep.id_extpresupuesto
            WHERE (:mes IS NULL OR EXTRACT(MONTH FROM COALESCE(l.fecha, oc.fecha_orden)) = :mes)
              AND oc.centro_costo IN (""" + inClause + ") " +
                "ORDER BY CASE WHEN l.id IS NULL THEN 1 ELSE 0 END, oc.fecha_orden";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter("mes", mes);

        List<Object[]> resultados = query.getResultList();
        return resultados.stream().map(ReporteGeneralDTO::new).collect(Collectors.toList());
    }
}
