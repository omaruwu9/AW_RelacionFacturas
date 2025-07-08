package com.example.demo.entity;

import com.example.demo.config.JsonConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "bitacora")
public class Bitacora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Se mapea como Map para JSONB (puede ser también String si no necesitas parsear)
    @Column(name = "id_usuario", columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> nomina;

    @Column(name = "tabla_afectada", nullable = false)
    private String tabla_afectada;

    @Column(name = "operacion", nullable = false)
    private String operacion;

    @Column(name = "datos_previos", columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> datos_previos;

    @Column(name = "datos_nuevos", columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> datos_nuevos;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, Object> getNomina() {
        return nomina;
    }

    public void setNomina(Map<String, Object> nomina) {
        this.nomina = nomina;
    }

    public String getTabla_afectada() {
        return tabla_afectada;
    }

    public void setTabla_afectada(String tabla_afectada) {
        this.tabla_afectada = tabla_afectada;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Map<String, Object> getDatos_previos() {
        return datos_previos;
    }

    public void setDatos_previos(Map<String, Object> datos_previos) {
        this.datos_previos = datos_previos;
    }

    public Map<String, Object> getDatos_nuevos() {
        return datos_nuevos;
    }

    public void setDatos_nuevos(Map<String, Object> datos_nuevos) {
        this.datos_nuevos = datos_nuevos;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
