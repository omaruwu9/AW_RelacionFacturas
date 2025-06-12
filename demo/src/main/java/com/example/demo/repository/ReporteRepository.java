package com.example.demo.repository;

import com.example.demo.entity.Llenado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReporteRepository extends CrudRepository<Llenado, Integer> {

    //consulta para realizar el reporte por el mes
    @Query(value = """
        SELECT\s
            oc.solicitud_oc, oc.fecha_requerida, oc.descripcion, oc.orden_compra, oc.fecha_orden, oc.fecha_emision,
            oc.centro_costo, oc.cuenta_contable, oc.desc_cuenta_contable, oc.nombre_proveedor,
            l.fecha AS fecha_factura, l.factura, l.folio_fiscal, l.cantidad, l.poliza_garantia,
            f.familia, r.responsable, e.ext_presupuesto,
            l.moneda, l.pu, l.subtotal, l.iva_porc, l.total,
            l.tipo_cambio, l.pesos_totales,
            CASE oc.estado
                WHEN 'O' THEN 'Cancelado'
                WHEN 'I' THEN 'Backorder'
                WHEN 'R' THEN 'Recibida'
                WHEN 'E' THEN 'Tránsito'
                WHEN 'P' THEN 'Por aprobar'
                WHEN 'A' THEN 'Planeada'
                WHEN 'U' THEN 'Cerrada'
                ELSE 'Desconocido'
            END AS estatus
        FROM orden_compra oc
        LEFT JOIN llenado l ON oc.id = l.id
        LEFT JOIN familia f ON l.id_familia = f.id_familia
        LEFT JOIN responsable r ON l.id_responsable = r.id_responsable
        LEFT JOIN ext_presupuesto e ON l.id_extpresupuesto = e.id_extpresupuesto
        WHERE EXTRACT(MONTH FROM COALESCE(l.fecha, oc.fecha_orden)) = :mes
        ORDER BY CASE WHEN l.id IS NULL THEN 1 ELSE 0 END, oc.fecha_orden
    """, nativeQuery = true)
    List<Object[]> obtenerReportePorMes(@Param("mes") int mes);

}

