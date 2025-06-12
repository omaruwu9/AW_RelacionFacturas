package com.example.demo.entity;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.sql.Date;
import java.time.format.DateTimeFormatter;

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

    //es para la creación (como si fuera un mapeo) para el excel de los reportes
    public void llenarRow(Row row, CellStyle style) {
        int i = 0;
        row.createCell(i++).setCellValue(solicitudOc);
        row.createCell(i++).setCellValue(formatDate(fechaRequerida));
        row.createCell(i++).setCellValue(descripcion);
        row.createCell(i++).setCellValue(ordenCompra);
        row.createCell(i++).setCellValue(formatDate(fechaOrden));
        row.createCell(i++).setCellValue(formatDate(fechaEmision));
        row.createCell(i++).setCellValue(centroCostos);
        row.createCell(i++).setCellValue(cuentaContable);
        row.createCell(i++).setCellValue(descripcionCuentaContable);
        row.createCell(i++).setCellValue(proveedor);
        row.createCell(i++).setCellValue(formatDate(fechaFactura)); // puede ser null
        row.createCell(i++).setCellValue(factura != null ? factura : "");
        row.createCell(i++).setCellValue(folioFiscal != null ? folioFiscal : "");
        row.createCell(i++).setCellValue(cantidad != null ? cantidad : 0);
        row.createCell(i++).setCellValue(polizaGarantia != null ? polizaGarantia : "");
        row.createCell(i++).setCellValue(familia != null ? familia : "");
        row.createCell(i++).setCellValue(responsable != null ? responsable : "");
        row.createCell(i++).setCellValue(extPresupuesto != null ? extPresupuesto : "");
        row.createCell(i++).setCellValue(moneda != null ? moneda : "");
        row.createCell(i++).setCellValue(pu != null ? pu : 0.0);
        row.createCell(i++).setCellValue(subtotal != null ? subtotal : 0.0);
        row.createCell(i++).setCellValue(ivaPorc != null ? ivaPorc : 0);
        row.createCell(i++).setCellValue(total != null ? total : 0.0);
        row.createCell(i++).setCellValue(tipoCambio != null ? tipoCambio : 0.0);
        row.createCell(i++).setCellValue(pesosTotales != null ? pesosTotales : 0.0);
        row.createCell(i++).setCellValue(estatus);

        for (int j = 0; j < i; j++) {
            row.getCell(j).setCellStyle(style);
        }
    }


    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return "";
        try {
            java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            return new java.text.SimpleDateFormat("M/d/yyyy").format(date);
        } catch (Exception e) {
            return dateStr; // devuélvelo tal cual si no se puede convertir
        }
    }




}
