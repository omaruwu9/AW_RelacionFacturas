package com.example.demo.repository;

import com.example.demo.entity.CuentaContableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CuentaContableRepository {

    @Autowired
    @Qualifier("sqlServerJdbcTemplate")  // Inyectamos el JdbcTemplate de SQL Server
    private JdbcTemplate sqlServerJdbcTemplate;

    @Repository
    public interface CuentaContableRepo extends JpaRepository<CuentaContableDTO, Long> {

        @Query(value = """
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
        WHERE YEAR(oc.FECHA) = :anio
          AND MONTH(oc.FECHA) = :mes
        GROUP BY ocl.CUENTA_CONTABLE
        """, nativeQuery = true)
        List<Object[]> obtenerBudgetMes(@Param("anio") int anio, @Param("mes") int mes);
    }
}
