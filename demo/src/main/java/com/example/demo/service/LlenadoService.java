package com.example.demo.service;

import com.example.demo.entity.Llenado;
import com.example.demo.repository.LlenadoRepository;
import org.springframework.stereotype.Service;

@Service
public class LlenadoService {

    private final LlenadoRepository repository;

    public LlenadoService(LlenadoRepository repository) {
        this.repository = repository;
    }

    public Llenado guardar(Llenado llenado) {
        return repository.save(llenado);
    }
}
