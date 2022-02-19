/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Models;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class ModelPhs implements Serializable {

    private Short piso;
    private Short numeracion;
    private Short bloque;
    private Integer numPhsHijas;
    private Map<Short, Short> phsHijas;

    public ModelPhs(Short piso, Integer numPhsHijas, Map<Short, Short> phsHijas) {
        this.piso = piso;
        this.numPhsHijas = numPhsHijas;
        this.phsHijas = phsHijas;
    }

    public ModelPhs() {
    }

    public ModelPhs(Short piso, Short numeracion, Short bloque) {
        this.piso = piso;
        this.numeracion = numeracion;
        this.bloque = bloque;
    }

    public ModelPhs(Short piso) {
        this.piso = piso;
    }

    public Short getPiso() {
        return piso;
    }

    public void setPiso(Short piso) {
        this.piso = piso;
    }

    public Integer getNumPhsHijas() {
        return numPhsHijas;
    }

    public void setNumPhsHijas(Integer numPhsHijas) {
        this.numPhsHijas = numPhsHijas;
    }

    public Map<Short, Short> getPhsHijas() {
        return phsHijas;
    }

    /**
     * El key es el orden de cada ph hija
     *
     * @param phsHijas
     */
    public void setPhsHijas(Map<Short, Short> phsHijas) {
        this.phsHijas = phsHijas;
    }

    public Short getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Short numeracion) {
        this.numeracion = numeracion;
    }

    public Short getBloque() {
        return bloque;
    }

    public void setBloque(Short bloque) {
        this.bloque = bloque;
    }

    @Override
    public String toString() {
        return "COD PISO " + piso + " PHS " + numPhsHijas;
    }

    @Override
    public boolean equals(Object obj) {
        ModelPhs ob = (ModelPhs) obj;
        if (Objects.isNull(ob)) {
            return false;
        }

        return Objects.equals(this.piso, ob.piso);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.piso);
        return hash;
    }

}
