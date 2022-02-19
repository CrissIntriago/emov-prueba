/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

/**
 *
 * @author Administrator
 */
public class AniosEmision {

    private Integer id;
    private Integer anioEmision;
    private Boolean consideraConstruccion = Boolean.TRUE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnioEmision() {
        return anioEmision;
    }

    public void setAnioEmision(Integer anioEmision) {
        this.anioEmision = anioEmision;
    }

    public Boolean getConsideraConstruccion() {
        return consideraConstruccion;
    }

    public void setConsideraConstruccion(Boolean consideraConstruccion) {
        this.consideraConstruccion = consideraConstruccion;
    }
}
