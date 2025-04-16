package com.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

public class LlenadoId implements Serializable {
    private Integer id;

    public LlenadoId() {}
    public LlenadoId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LlenadoId that = (LlenadoId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

