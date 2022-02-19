/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class BloquePredio implements Serializable {

    private Long gid;
    private Long gidPredio;
    private Short numBloq;
    private Integer numeracion;
    private String codigoPh;
    private BigDecimal area;
    private BigDecimal areaBloque;
    private BigDecimal areaPiso;
    private Integer orden;
    private Long predio;
    private Long edificacion;
    private String bloCod;
    private Boolean editado = true;
    private List<Bloques> niveles;

    public BloquePredio() {
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getGidPredio() {
        return gidPredio;
    }

    public void setGidPredio(Long gidPredio) {
        this.gidPredio = gidPredio;
    }

    public Short getNumBloq() {
        return numBloq;
    }

    public void setNumBloq(Short numBloq) {
        this.numBloq = numBloq;
    }

    public Integer getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Integer numeracion) {
        this.numeracion = numeracion;
    }

    public String getCodigoPh() {
        return codigoPh;
    }

    public void setCodigoPh(String codigoPh) {
        this.codigoPh = codigoPh;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getAreaBloque() {
        return areaBloque;
    }

    public void setAreaBloque(BigDecimal areaBloque) {
        this.areaBloque = areaBloque;
    }

    public BigDecimal getAreaPiso() {
        return areaPiso;
    }

    public void setAreaPiso(BigDecimal areaPiso) {
        this.areaPiso = areaPiso;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public Long getEdificacion() {
        return edificacion;
    }

    public void setEdificacion(Long edificacion) {
        this.edificacion = edificacion;
    }

    public String getBloCod() {
        return bloCod;
    }

    public void setBloCod(String bloCod) {
        this.bloCod = bloCod;
    }

    public Boolean getEditado() {
        if (editado == null) {
            editado = true;
        }
        return editado;
    }

    public void setEditado(Boolean editado) {
        this.editado = editado;
    }

    public List<Bloques> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Bloques> niveles) {
        this.niveles = niveles;
    }

    @Override
    public String toString() {
        return "BloquePredio{" + "gid=" + gid + ", gidPredio=" + gidPredio + ", numBloq=" + numBloq + ", numeracion=" + numeracion + ", codigoPh=" + codigoPh + ", area=" + area + ", areaBloque=" + areaBloque + ", orden=" + orden + ", predio=" + predio + ", edificacion=" + edificacion + ", bloCod=" + bloCod + ", editado=" + editado + ", niveles=" + niveles + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.gid);
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
        final BloquePredio other = (BloquePredio) obj;
        if (!Objects.equals(this.gid, other.gid)) {
            return false;
        }
        return true;
    }

}
