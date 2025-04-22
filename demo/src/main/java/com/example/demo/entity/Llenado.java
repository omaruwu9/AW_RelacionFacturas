package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "llenado")
@IdClass(LlenadoId.class)
public class Llenado {

    @Id
    private Integer id;

//    @Column(name = "orden_compra")
//    private String ordenCompra;

    private String factura;
    private LocalDate fecha;
    private String folio_fiscal;

    private Double cantidad;
    private String moneda;
    private Double pu;
    private Double subtotal;
    private Double iva_porc;
    private Double total;
    private Double tipo_cambio;
    private Double pesos_totales;
    private String poliza_garantia;
    private Integer id_familia;
    private Integer id_responsable;
    private Integer id_extpresupuesto;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

//    public String getOrdenCompra() { return ordenCompra; }
//    public void setOrdenCompra(String ordenCompra) { this.ordenCompra = ordenCompra; }

    public String getFactura() { return factura; }
    public void setFactura(String factura) { this.factura = factura; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getFolio_fiscal() { return folio_fiscal; }
    public void setFolio_fiscal(String folio_fiscal) { this.folio_fiscal = folio_fiscal; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public Double getPu() { return pu; }
    public void setPu(Double pu) { this.pu = pu; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getIva_porc() { return iva_porc; }
    public void setIva_porc(Double iva_porc) { this.iva_porc = iva_porc; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Double getTipo_cambio() { return tipo_cambio; }
    public void setTipo_cambio(Double tipo_cambio) { this.tipo_cambio = tipo_cambio; }

    public Double getPesos_totales() { return pesos_totales; }
    public void setPesos_totales(Double pesos_totales) { this.pesos_totales = pesos_totales; }

    public String getPoliza_garantia() { return poliza_garantia; }
    public void setPoliza_garantia(String poliza_garantia) { this.poliza_garantia = poliza_garantia; }

    public Integer getId_familia() { return id_familia; }
    public void setId_familia(Integer id_familia) { this.id_familia = id_familia; }

    public Integer getId_responsable() { return id_responsable; }
    public void setId_responsable(Integer id_responsable) { this.id_responsable = id_responsable; }

    public Integer getId_extpresupuesto() { return id_extpresupuesto; }
    public void setId_extpresupuesto(Integer id_extpresupuesto) { this.id_extpresupuesto = id_extpresupuesto; }
}
