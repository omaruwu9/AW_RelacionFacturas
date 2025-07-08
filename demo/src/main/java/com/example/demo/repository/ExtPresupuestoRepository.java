package com.example.demo.repository;

import com.example.demo.entity.ExtPresupuesto;
import com.example.demo.entity.Familia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtPresupuestoRepository extends CrudRepository<ExtPresupuesto, Integer> {
    List<ExtPresupuesto> findAll();
}
