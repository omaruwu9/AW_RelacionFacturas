package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rol_centros_costo")
public class RolCentroCosto {
    @EmbeddedId
    private RolCentroCostoId id;

    @ManyToOne
    @MapsId("idRol")
    @JoinColumn(name = "id_rol")
    private Rol rol;

    public RolCentroCostoId getId() {
        return id;
    }

    public void setId(RolCentroCostoId id) {
        this.id = id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}

