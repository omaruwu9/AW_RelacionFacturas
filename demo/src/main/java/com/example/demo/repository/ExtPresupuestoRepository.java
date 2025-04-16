package com.example.demo.repository;

import com.example.demo.entity.ExtPresupuesto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtPresupuestoRepository extends CrudRepository<ExtPresupuesto, Integer> {
}
