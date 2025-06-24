package com.example.demo.entity;

import java.math.BigDecimal;

public class CuentaContableDTO {
    private String cuentaContable;
    private BigDecimal importeTotalLocal;

    // Constructor
    public CuentaContableDTO(String cuentaContable, BigDecimal importeTotalLocal) {
        this.cuentaContable = cuentaContable;
        this.importeTotalLocal = importeTotalLocal;
    }

    // Getters
    public String getCuentaContable() {
        return cuentaContable;
    }

    public BigDecimal getImporteTotalLocal() {
        return importeTotalLocal;
    }
}

