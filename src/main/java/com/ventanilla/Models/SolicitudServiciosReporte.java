/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Models;

/**
 *
 * @author ricar
 */
public class SolicitudServiciosReporte {

    private String nombre;
    private Integer cantidad;

    public SolicitudServiciosReporte() {
    }

    public SolicitudServiciosReporte(String nombre, Integer cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "SolicitudServiciosReporte{" + "nombre=" + nombre + ", cantidad=" + cantidad + '}';
    }

}
