/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.models;

/**
 *
 * @author ORIGAMIEC
 */
public class PredioCemCiudadelaManzanaDTO {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private Short manzana;
    private Integer id_ciudadela;

    public Short getManzana() {
        return manzana;
    }

    public void setManzana(Short manzana) {
        this.manzana = manzana;
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

    public String toString() {
        return "ciudadela: " + this.nombre + " manzana: " + this.manzana;
    }

}
