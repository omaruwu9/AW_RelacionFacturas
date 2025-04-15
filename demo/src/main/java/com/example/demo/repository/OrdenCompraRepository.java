package com.example.demo.repository;

import com.example.demo.entity.OrdenCompra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraRepository extends CrudRepository<OrdenCompra, String> {
    List<OrdenCompra> findAll();

    Optional<OrdenCompra> findById(Integer id);

}
