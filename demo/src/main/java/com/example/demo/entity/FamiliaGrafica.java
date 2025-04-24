package com.example.demo.entity;

import java.math.BigDecimal;

public class FamiliaGrafica {
    private String familia;
    private BigDecimal total;

    public FamiliaGrafica(String familia, BigDecimal total) {
        this.familia = familia;
        this.total = total;
    }

    public String getFamilia() { return familia; }
    public BigDecimal getTotal() { return total; }
}

