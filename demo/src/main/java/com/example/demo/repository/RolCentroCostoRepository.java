package com.example.demo.repository;

import com.example.demo.entity.RolCentroCosto;
import com.example.demo.entity.RolCentroCostoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolCentroCostoRepository extends JpaRepository<RolCentroCosto, RolCentroCostoId> {
    @Query("SELECT rcc.id.centroCostos FROM RolCentroCosto rcc WHERE rcc.id.idRol = :idRol")
    List<String> findCentrosCostoByIdRol(@Param("idRol") Integer idRol);
}

