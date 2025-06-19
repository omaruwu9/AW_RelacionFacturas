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

    public String obtenerRutaXmlPorId(Integer id) {
        LlenadoId llenadoId = new LlenadoId(id);
        Llenado llenado = repository.findById(llenadoId)
                .orElseThrow(() -> new RuntimeException("Llenado no encontrado con id: " + id));

        return "C:/Users/omar.p/Documents/RESIDENCIAS/PROYECTO_CODIGO/PROYECTO_CODIGO/xml/" + llenado.getRutaXml();
    }

    public String obtenerRutaPdfPorId(Integer id) {
        Llenado llenado = repository.findById(new LlenadoId(id))
                .orElseThrow(() -> new RuntimeException("Llenado no encontrado con id: " + id));

        return "C:/.../pdf/" + llenado.getRutaPdf(); // que sería 5652.xml.pdf
    }


}
