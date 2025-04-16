package com.example.demo.repository;

import com.example.demo.entity.Responsable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsableRepository extends CrudRepository<Responsable, Integer> {
}
