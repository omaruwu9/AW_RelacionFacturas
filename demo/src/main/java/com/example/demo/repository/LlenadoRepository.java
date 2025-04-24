package com.example.demo.repository;

import com.example.demo.entity.FamiliaGrafica;
import com.example.demo.entity.Llenado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlenadoRepository extends CrudRepository<Llenado, Integer> {
    @Query(value = "SELECT f.familia AS familia, " +
            "COALESCE(SUM(l.pesos_totales), 0) AS total " +
            "FROM familia f " +
            "LEFT JOIN llenado l ON l.id_familia = f.id_familia " +
            "GROUP BY f.familia", nativeQuery = true)
    List<FamiliaGrafica> obtenerTotalesPorFamilia();
}


