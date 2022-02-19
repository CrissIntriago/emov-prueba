/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class Zebra {

    private String archivo; //ARCHIVO IMAGEN A IMPRIMIR
    private String impresora; //IMPRESORA
    private Boolean estadoImpresion; //SI LA IMPRESION FUE OK ? 
    private Integer cantidad;
    private String rfid;
    private String archivoGrabado;

    public Zebra() {
    }

    public Zebra(String impresora, String archivo, Integer cantidad) {
        this.archivo = archivo;
        this.impresora = impresora;
        this.cantidad = cantidad;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getImpresora() {
        return impresora;
    }

    public void setImpresora(String impresora) {
        this.impresora = impresora;
    }

    public Boolean getEstadoImpresion() {
        return estadoImpresion;
    }

    public void setEstadoImpresion(Boolean estadoImpresion) {
        this.estadoImpresion = estadoImpresion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getArchivoGrabado() {
        return archivoGrabado;
    }

    public void setArchivoGrabado(String archivoGrabado) {
        this.archivoGrabado = archivoGrabado;
    }
}
