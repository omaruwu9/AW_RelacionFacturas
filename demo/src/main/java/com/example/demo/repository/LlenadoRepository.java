package com.example.demo.repository;

import com.example.demo.entity.Llenado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LlenadoRepository extends CrudRepository<Llenado, Integer> {
}

