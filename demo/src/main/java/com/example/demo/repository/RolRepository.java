package com.example.demo.repository;
import com.example.demo.entity.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends CrudRepository<Rol, Integer>{
        //para listar los roles, mostrarlos todas
        List<Rol> findAll();
}
