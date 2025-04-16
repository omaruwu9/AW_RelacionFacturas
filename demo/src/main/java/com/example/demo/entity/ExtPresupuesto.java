package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ext_presupuesto")
public class ExtPresupuesto {

    @Id
    private Integer id_extpresupuesto;

    private String ext_presupuesto;

    public Integer getId_extpresupuesto() {
        return id_extpresupuesto;
    }

    public void setId_extpresupuesto(Integer id_extpresupuesto) {
        this.id_extpresupuesto = id_extpresupuesto;
    }

    public String getExt_presupuesto() {
        return ext_presupuesto;
    }

    public void setExt_presupuesto(String ext_presupuesto) {
        this.ext_presupuesto = ext_presupuesto;
    }
}
