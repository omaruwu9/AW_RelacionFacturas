package com.example.demo.repository;

import com.example.demo.entity.FamiliaGrafica;
import com.example.demo.entity.Llenado;
import com.example.demo.entity.LlenadoId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface LlenadoRepository extends CrudRepository<Llenado, LlenadoId> {
    //se obtiene los totales por familia (en general)
    @Query(value = "SELECT f.familia AS familia, " +
            "COALESCE(SUM(l.pesos_totales), 0) AS total " +
            "FROM familia f " +
            "LEFT JOIN llenado l ON l.id_familia = f.id_familia " +
            "GROUP BY f.familia", nativeQuery = true)
    List<FamiliaGrafica> obtenerTotalesPorFamilia();


    //se obtiene los totales por famiala pero por año
    @Query(value = "SELECT f.familia AS familia, " +
            "COALESCE(SUM(l.pesos_totales), 0) AS total " +
            "FROM familia f " +
            "LEFT JOIN llenado l ON l.id_familia = f.id_familia " +
            "WHERE EXTRACT(YEAR FROM l.fecha) = :anio " +
            "GROUP BY f.familia", nativeQuery = true)
    List<FamiliaGrafica> obtenerTotalesPorFamiliaYAnio(@Param("anio") int anio);

    @Query(value = "SELECT f.familia AS familia, COALESCE(SUM(l.pesos_totales), 0) AS total " +
            "FROM llenado l " +
            "JOIN familia f ON l.id_familia = f.id_familia " +
            "WHERE EXTRACT(YEAR FROM l.fecha) = :anio AND EXTRACT(MONTH FROM l.fecha) = :mes " +
            "GROUP BY f.familia", nativeQuery = true)
    List<FamiliaGrafica> obtenerTotalesPorFamiliaYMes(@Param("anio") int anio, @Param("mes") int mes);

}


