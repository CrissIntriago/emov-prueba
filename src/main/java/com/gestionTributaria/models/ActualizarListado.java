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
public class ActualizarListado {
    
    private boolean actualizarCobros;
    private boolean actualizarValidar;

    public ActualizarListado() {
    }

    public boolean isActualizarCobros() {
        return actualizarCobros;
    }

    public void setActualizarCobros(boolean actualizarCobros) {
        this.actualizarCobros = actualizarCobros;
    }

    public boolean isActualizarValidar() {
        return actualizarValidar;
    }

    public void setActualizarValidar(boolean actualizarValidar) {
        this.actualizarValidar = actualizarValidar;
    }
}
