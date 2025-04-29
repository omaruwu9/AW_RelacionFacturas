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

@Repository
public interface LlenadoRepository extends CrudRepository<Llenado, LlenadoId> {
    @Query(value = "SELECT f.familia AS familia, " +
            "COALESCE(SUM(l.pesos_totales), 0) AS total " +
            "FROM familia f " +
            "LEFT JOIN llenado l ON l.id_familia = f.id_familia " +
            "GROUP BY f.familia", nativeQuery = true)
    List<FamiliaGrafica> obtenerTotalesPorFamilia();


    @Query(value = "SELECT id_familia, COALESCE(SUM(pesos_totales), 0) " +
            "FROM llenado l " +
            "WHERE EXTRACT(YEAR FROM fecha) = :anio " +
            "GROUP BY id_familia", nativeQuery = true)
    List<Object[]> obtenerTotalesPorFamiliaYAnio(@Param("anio") int anio);


}


