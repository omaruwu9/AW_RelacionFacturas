package com.example.demo.repository;

import com.example.demo.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByNomina(String nomina);
    //encontrar el usuario por su nomina
}
