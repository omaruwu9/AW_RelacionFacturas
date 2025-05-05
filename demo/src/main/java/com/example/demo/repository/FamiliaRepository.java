package com.example.demo.repository;

import com.example.demo.entity.Familia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamiliaRepository extends CrudRepository<Familia, Integer> {
    //para listar las familais, mostrarlas todas
    List<Familia> findAll();
}