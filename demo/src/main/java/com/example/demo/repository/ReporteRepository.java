package com.example.demo.repository;

import com.example.demo.entity.Llenado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReporteRepository extends CrudRepository<Llenado, Integer> {

    //consulta para realizar el reporte por el mes
    @Query(value = """
        SELECT 
            oc.solicitud_oc, oc.fecha_requerida, oc.descripcion, oc.orden_compra, oc.fecha_orden, oc.fecha_emision,
            oc.centro_costo, oc.cuenta_contable, oc.desc_cuenta_contable, oc.nombre_proveedor,
            l.fecha AS fecha_factura, l.factura, l.folio_fiscal, l.cantidad, l.poliza_garantia,
            f.familia, r.responsable, e.ext_presupuesto,
            l.moneda, l.pu, l.subtotal, l.iva_porc, l.total,
            l.tipo_cambio, l.pesos_totales, s.estatus
        FROM orden_compra oc
        JOIN llenado l ON oc.id = l.id
        LEFT JOIN familia f ON l.id_familia = f.id_familia
        LEFT JOIN responsable r ON l.id_responsable = r.id_responsable
        LEFT JOIN ext_presupuesto e ON l.id_extpresupuesto = e.id_extpresupuesto
        LEFT JOIN estatus s ON l.id_estatus = s.id_estatus
        WHERE EXTRACT(MONTH FROM l.fecha) = :mes
    """, nativeQuery = true)
    List<Object[]> obtenerReportePorMes(@Param("mes") int mes);
}

