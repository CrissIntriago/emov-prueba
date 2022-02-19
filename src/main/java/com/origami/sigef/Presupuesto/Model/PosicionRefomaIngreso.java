/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class PosicionRefomaIngreso implements Serializable {

    private int posicion;
    private Long proforma;

    public PosicionRefomaIngreso() {

    }

    public PosicionRefomaIngreso(int posicion, Long proforma) {
        this.posicion = posicion;
        this.proforma = proforma;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Long getProforma() {
        return proforma;
    }

    public void setProforma(Long proforma) {
        this.proforma = proforma;
    }

}
