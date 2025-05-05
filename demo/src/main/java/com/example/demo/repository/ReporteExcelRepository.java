package com.example.demo.repository;

import com.example.demo.entity.Llenado;
import com.example.demo.entity.ReporteGeneralDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ReporteExcelRepository extends JpaRepository<Llenado, Integer> {


    //consulta para realizar el reporte correspondiente
    @Query(value = "SELECT oc.solicitud_oc, oc.fecha_requerida, oc.descripcion, oc.orden_compra, " +
            "oc.fecha_orden, oc.fecha_emision, oc.centro_costo, oc.cuenta_contable, oc.desc_cuenta_contable, " +
            "oc.nombre_proveedor AS proveedor, l.fecha AS fecha_factura, l.factura, l.folio_fiscal, l.cantidad, " +
            "l.poliza_garantia, f.familia, r.responsable, ep.ext_presupuesto, l.moneda, l.pu, l.subtotal, " +
            "l.iva_porc AS iva, l.total, l.tipo_cambio, l.pesos_totales, " +
            "CASE oc.estado " +
            "WHEN 'O' THEN 'Cancelado' WHEN 'I' THEN 'Backorder' WHEN 'R' THEN 'Recibida' " +
            "WHEN 'E' THEN 'Tránsito' WHEN 'P' THEN 'Por aprobar' WHEN 'A' THEN 'Planeada' " +
            "WHEN 'U' THEN 'Cerrada' ELSE 'Desconocido' END AS estatus " +
            "FROM llenado l " +
            "JOIN orden_compra oc ON l.id = oc.id " +
            "LEFT JOIN familia f ON l.id_familia = f.id_familia " +
            "LEFT JOIN responsable r ON l.id_responsable = r.id_responsable " +
            "LEFT JOIN ext_presupuesto ep ON l.id_extpresupuesto = ep.id_extpresupuesto " +
            "WHERE EXTRACT(YEAR FROM l.fecha) = :anio AND EXTRACT(MONTH FROM l.fecha) BETWEEN :mesInicio AND :mesFin",
            nativeQuery = true)
    List<Object[]> obtenerReportePorPeriodo(@Param("anio") int anio,
                                            @Param("mesInicio") int mesInicio,
                                            @Param("mesFin") int mesFin);
}
