package com.example.demo.service;

import com.example.demo.entity.Llenado;
import com.example.demo.repository.LlenadoRepository;
import org.springframework.stereotype.Service;

@Service
public class LlenadoService {
    private final LlenadoRepository llenadoRepository;

    public LlenadoService(LlenadoRepository llenadoRepository) {
        this.llenadoRepository = llenadoRepository;
    }

    public Llenado guardarLlenado(Llenado llenado) {
        return llenadoRepository.save(llenado);
    }
}

