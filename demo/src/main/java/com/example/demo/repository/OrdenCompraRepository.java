package com.example.demo.repository;

import com.example.demo.entity.OrdenCompra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraRepository extends CrudRepository<OrdenCompra, String> {
    List<OrdenCompra> findAll();

    Optional<OrdenCompra> findById(Integer id);

    //lo anterior muestra la lista de OC además de poder encontrarlos por medio de sus id que se les asigno
    //el id es un int que es autoincrementable a partir de los registros que se vayan realizando en la tabla

    List<OrdenCompra> findByCentroCostoIn(List<String> centroCosto);


}
