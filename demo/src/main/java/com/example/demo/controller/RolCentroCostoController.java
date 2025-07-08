package com.example.demo.controller;

import com.example.demo.entity.Rol;
import com.example.demo.entity.RolCentroCosto;
import com.example.demo.entity.RolCentroCostoId;
import com.example.demo.repository.RolCentroCostoRepository;
import com.example.demo.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/rolcentro")
public class RolCentroCostoController {
    @Autowired
    private RolCentroCostoRepository repository;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public List<RolCentroCosto> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public RolCentroCosto create(@RequestBody RolCentroCostoId dto) {
        if (dto.getIdRol() == null) {
            throw new IllegalArgumentException("idRol no puede ser null");
        }

        // Busca el Rol en BD, lanza error si no existe
        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new IllegalArgumentException("Rol no existe"));

        RolCentroCostoId id = new RolCentroCostoId();
        id.setIdRol(dto.getIdRol());
        id.setCentroCostos(dto.getCentroCostos());

        RolCentroCosto rcc = new RolCentroCosto();
        rcc.setId(id);
        rcc.setRol(rol);

        return repository.save(rcc);
    }


    @DeleteMapping
    public void delete(@RequestBody RolCentroCostoId id) {
        repository.deleteById(id);
    }
}
