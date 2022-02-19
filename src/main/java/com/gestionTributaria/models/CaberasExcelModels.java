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
public class CaberasExcelModels implements Serializable {

    private String cabecera;
    private int columna;

    public CaberasExcelModels() {
    }

    public CaberasExcelModels(String cabecera) {
        this.cabecera = cabecera;
    }

    public CaberasExcelModels(String cabecera, int columna) {
        this.cabecera = cabecera;
        this.columna = columna;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public String toString() {
        return "cabecera " + this.cabecera + " columna " + this.columna; //To change body of generated methods, choose Tools | Templates.
    }

}
