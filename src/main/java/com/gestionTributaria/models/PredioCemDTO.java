/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class PredioCemDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Short sector;
    private String nombre;
    private Integer id_ciudadela;

    public Short getSector() {
        return sector;
    }

    public void setSector(Short sector) {
        this.sector = sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId_ciudadela() {
        return id_ciudadela;
    }

    public void setId_ciudadela(Integer id_ciudadela) {
        this.id_ciudadela = id_ciudadela;
    }

    @Override
    public String toString() {
        return "sector: " + this.sector + "nombre: " + this.nombre;
    }

}
