package com.example.demo.entity;

import jakarta.persistence.*;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "orden_compra")
    private String ordenCompra;

    @Column(name = "solicitud_oc")
    private String solicitudOc;

    @Column(name = "fecha_requerida")
    private LocalDateTime fechaRequerida;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "centro_costo")
    private String centroCosto;

    @Column(name = "cuenta_contable")
    private String cuentaContable;

    @Column(name = "desc_cuenta_contable")
    private String descCuentaContable;

    @Column(name = "nombre_proveedor")
    private String nombreProveedor;

    @Column(name = "estado")
    private String estado;

    // getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public String getSolicitudOc() {
        return solicitudOc;
    }

    public void setSolicitudOc(String solicitudOc) {
        this.solicitudOc = solicitudOc;
    }

    public LocalDateTime getFechaRequerida() {
        return fechaRequerida;
    }

    public void setFechaRequerida(LocalDateTime fechaRequerida) {
        this.fechaRequerida = fechaRequerida;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public String getDescCuentaContable() {
        return descCuentaContable;
    }

    public void setDescCuentaContable(String descCuentaContable) {
        this.descCuentaContable = descCuentaContable;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
