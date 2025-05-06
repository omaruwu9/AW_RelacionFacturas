package com.example.demo.service;

import com.example.demo.entity.Llenado;
import com.example.demo.entity.LlenadoId;
import com.example.demo.repository.LlenadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LlenadoService {

    private final LlenadoRepository repository;

    public LlenadoService(LlenadoRepository repository) {
        this.repository = repository;
    }

    public Llenado guardar(Llenado llenado) {
        return repository.save(llenado);
    }

    public Optional<Llenado> obtenerPorId(Integer id) {
        return repository.findById(new LlenadoId(id));
    }

}
