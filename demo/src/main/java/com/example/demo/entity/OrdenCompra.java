package com.example.demo.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "orden_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "orden_compra")
    private String ordenCompra;

    @Column(name = "descripcion")
    private String descripcionOC;

    @Column(name = "solicitud_oc")
    private String solicitudOc;

    @Column(name = "fecha_requerida")
    private Date fechaRequerida;

    @Column(name = "fecha_emision")
    private Date fecha_emision;

    @Column(name = "fecha_orden")
    private Date fecha_orden;

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

    @Column(name = "rowpointer")
    private String rowpointer;

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

    public String getDescripcionOC() {
        return descripcionOC;
    }

    public void setDescripcionOC(String descripcion) {
        this.descripcionOC = descripcion;
    }

    public String getSolicitudOc() {
        return solicitudOc;
    }

    public void setSolicitudOc(String solicitudOc) {
        this.solicitudOc = solicitudOc;
    }

    public Date getFechaRequerida() {
        return fechaRequerida;
    }

    public void setFechaRequerida(Date fechaRequerida) {
        this.fechaRequerida = fechaRequerida;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha) {
        this.fecha_emision = fecha;
    }

    public Date getFecha_orden() {
        return fecha_orden;
    }

    public void setFecha_orden(Date fecha_requeridaOC) {
        this.fecha_orden = fecha_requeridaOC;
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

    public String getRowpointer() {return rowpointer;}

    public void setRowpointer(String rowpointer) {this.rowpointer = rowpointer;}
}
