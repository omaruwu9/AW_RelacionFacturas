package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Row;

public class ReporteGeneralDTO {
    private String solicitudOc;
    private String fechaRequerida;
    private String descripcion;
    private String ordenCompra;
    private String fechaOrden;
    private String fechaEmision;
    private String centroCostos;
    private String cuentaContable;
    private String descripcionCuentaContable;
    private String proveedor;
    private String fechaFactura;
    private String factura;
    private String folioFiscal;
    private Integer cantidad;
    private String polizaGarantia;
    private String familia;
    private String responsable;
    private String extPresupuesto;
    private String moneda;
    private Double pu;
    private Double subtotal;
    private Integer ivaPorc;
    private Double total;
    private Double tipoCambio;
    private Double pesosTotales;
    private String estatus;

    public ReporteGeneralDTO() {}

    public ReporteGeneralDTO(Object[] obj) {
        this.solicitudOc = asString(obj[0]);
        this.fechaRequerida = asString(obj[1]);
        this.descripcion = asString(obj[2]);
        this.ordenCompra = asString(obj[3]);
        this.fechaOrden = asString(obj[4]);
        this.fechaEmision = asString(obj[5]);
        this.centroCostos = asString(obj[6]);
        this.cuentaContable = asString(obj[7]);
        this.descripcionCuentaContable = asString(obj[8]);
        this.proveedor = asString(obj[9]);
        this.fechaFactura = asString(obj[10]);
        this.factura = asString(obj[11]);
        this.folioFiscal = asString(obj[12]);
        this.cantidad = asInteger(obj[13]);
        this.polizaGarantia = asString(obj[14]);
        this.familia = asString(obj[15]);
        this.responsable = asString(obj[16]);
        this.extPresupuesto = asString(obj[17]);
        this.moneda = asString(obj[18]);
        this.pu = asDouble(obj[19]);
        this.subtotal = asDouble(obj[20]);
        this.ivaPorc = asInteger(obj[21]);
        this.total = asDouble(obj[22]);
        this.tipoCambio = asDouble(obj[23]);
        this.pesosTotales = asDouble(obj[24]);
        this.estatus = asString(obj[25]);
    }

    private String asString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    private Integer asInteger(Object obj) {
        return obj != null ? Integer.parseInt(obj.toString()) : null;
    }

    private Double asDouble(Object obj) {
        return obj != null ? Double.parseDouble(obj.toString()) : null;
    }

    public void llenarRow(Row row) {
        row.createCell(0).setCellValue(solicitudOc);
        row.createCell(1).setCellValue(fechaRequerida);
        row.createCell(2).setCellValue(descripcion);
        row.createCell(3).setCellValue(ordenCompra);
        row.createCell(4).setCellValue(fechaOrden);
        row.createCell(5).setCellValue(fechaEmision);
        row.createCell(6).setCellValue(centroCostos);
        row.createCell(7).setCellValue(cuentaContable);
        row.createCell(8).setCellValue(descripcionCuentaContable);
        row.createCell(9).setCellValue(proveedor);
        row.createCell(10).setCellValue(fechaFactura);
        row.createCell(11).setCellValue(factura);
        row.createCell(12).setCellValue(folioFiscal);
        row.createCell(13).setCellValue(cantidad != null ? cantidad : 0);
        row.createCell(14).setCellValue(polizaGarantia);
        row.createCell(15).setCellValue(familia);
        row.createCell(16).setCellValue(responsable);
        row.createCell(17).setCellValue(extPresupuesto);
        row.createCell(18).setCellValue(moneda);
        row.createCell(19).setCellValue(pu != null ? pu : 0);
        row.createCell(20).setCellValue(subtotal != null ? subtotal : 0);
        row.createCell(21).setCellValue(ivaPorc != null ? ivaPorc : 0);
        row.createCell(22).setCellValue(total != null ? total : 0);
        row.createCell(23).setCellValue(tipoCambio != null ? tipoCambio : 0);
        row.createCell(24).setCellValue(pesosTotales != null ? pesosTotales : 0);
        row.createCell(25).setCellValue(estatus);
    }
}
