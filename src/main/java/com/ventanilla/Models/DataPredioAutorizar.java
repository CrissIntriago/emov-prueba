/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Models;

/**
 *
 * @author Administrator
 */
public class DataPredioAutorizar {

    private String servicio;
    private String concepto;
    private String observacion;
    private CatPredioModel catPredio;

    public DataPredioAutorizar() {
    }

    public DataPredioAutorizar(String servicio, String concepto, String observacion, CatPredioModel catPredio) {
        this.servicio = servicio;
        this.concepto = concepto;
        this.observacion = observacion;
        this.catPredio = catPredio;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CatPredioModel getCatPredio() {
        return catPredio;
    }

    public void setCatPredio(CatPredioModel catPredio) {
        this.catPredio = catPredio;
    }

    @Override
    public String toString() {
        return "DataPredioAutorizar{" + "servicio=" + servicio + ", concepto=" + concepto
                + ", observacion=" + observacion + ", catPredio=" + catPredio + '}';
    }

}
