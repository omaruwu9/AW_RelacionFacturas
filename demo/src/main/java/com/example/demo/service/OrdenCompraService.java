package com.example.demo.service;

import com.example.demo.entity.OrdenCompra;
import com.example.demo.repository.OrdenCompraRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    public List<OrdenCompra> obtenerTodas() {
        return ordenCompraRepository.findAll();
    }

    public OrdenCompra guardar(OrdenCompra orden) {
        return ordenCompraRepository.save(orden);
    }

    public Optional<OrdenCompra> obtenerPorId(String id) {
        return ordenCompraRepository.findById(id);
    }

}
