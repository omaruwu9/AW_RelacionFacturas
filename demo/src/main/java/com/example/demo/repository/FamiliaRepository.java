package com.example.demo.repository;

import com.example.demo.entity.Familia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliaRepository extends CrudRepository<Familia, Integer> {
}