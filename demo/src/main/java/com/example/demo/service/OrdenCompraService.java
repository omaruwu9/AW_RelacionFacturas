package com.example.demo.service;

import com.example.demo.entity.OrdenCompra;
import com.example.demo.repository.OrdenCompraRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrdenCompraService {
    private final OrdenCompraRepository ordenCompraRepository;

    @Scheduled(cron = "0 0 1 * * ?")
    public void cargarOrdenComprasDesdeSoftland() {
        OrdenCompra f = new OrdenCompra();
        f.setOrdenCompra("OC12345");
        f.setCentroCosto("05-050");
        f.setCuentaContable("601.10");
        f.setFecha(LocalDateTime.now().toString());
        ordenCompraRepository.save(f);
    }
}