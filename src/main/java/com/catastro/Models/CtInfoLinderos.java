/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class CtInfoLinderos implements Serializable {

    private static final long serialVersionUID = 1L;
    private String vertice;
    private String col_Codigo;
    private String rumbo;
    private String vertices;
    private BigDecimal longitud_mts;
    private Integer ultimo;
    private BigDecimal eje_x;
    private BigDecimal eje_y;

    public CtInfoLinderos() {
    }

    /**
     * Descripcion del punto
     *
     * @return
     */
    public String getVertice() {
        return vertice;
    }

    /**
     * Descripcion del punto
     *
     * @param vertice
     */
    public void setVertice(String vertice) {
        this.vertice = vertice;
    }

    public String getCol_Codigo() {
        return col_Codigo;
    }

    public void setCol_Codigo(String col_Codigo) {
        this.col_Codigo = col_Codigo;
    }

    public String getRumbo() {
        return rumbo;
    }

    public void setRumbo(String rumbo) {
        this.rumbo = rumbo;
    }

    /**
     * Punto desde hasta
     *
     * @return Desde donde sale el punto hasta su punto de llegada
     */
    public String getVertices() {
        return vertices;
    }

    /**
     * Desde donde sale el punto hasta su punto de llegada
     *
     * @param vertices ejemplo 1-2
     */
    public void setVertices(String vertices) {
        this.vertices = vertices;
    }

    public BigDecimal getLongitud_mts() {
        return longitud_mts;
    }

    public void setLongitud_mts(BigDecimal longitud_mts) {
        this.longitud_mts = longitud_mts;
    }

    public Integer getUltimo() {
        return ultimo;
    }

    public void setUltimo(Integer ultimo) {
        this.ultimo = ultimo;
    }

    public BigDecimal getEje_x() {
        return eje_x;
    }

    public void setEje_x(BigDecimal eje_x) {
        this.eje_x = eje_x;
    }

    public BigDecimal getEje_y() {
        return eje_y;
    }

    public void setEje_y(BigDecimal eje_y) {
        this.eje_y = eje_y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.vertice);
        hash = 29 * hash + Objects.hashCode(this.eje_x);
        hash = 29 * hash + Objects.hashCode(this.eje_y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CtInfoLinderos other = (CtInfoLinderos) obj;
        if (!Objects.equals(this.vertice, other.vertice)) {
            return false;
        }
        if (!Objects.equals(this.eje_x, other.eje_x)) {
            return false;
        }
        if (!Objects.equals(this.eje_y, other.eje_y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CtInfoLinderos{" + "vertice=" + vertice + ", colCodigo=" + col_Codigo + ", rumbo=" + rumbo + ", vertices=" + vertices + ", longitudMts=" + longitud_mts + ", ultimo=" + ultimo + ", ejeX=" + eje_x + ", ejeY=" + eje_y + '}';
    }

}
