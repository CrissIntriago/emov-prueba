/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;

/**
 *
 * @author gutya
 */
public class FirmaElectronica {

    private Cajero cajero;
    private String archivo;
    private String password;
    private String estado;

    public FirmaElectronica() {
    }

    public FirmaElectronica(Cajero cajero, String archivo, String password, String estado) {
        this.cajero = cajero;
        this.archivo = archivo;
        this.password = password;
        this.estado = estado;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "FirmaElectronica{" + "cajero:" + cajero + ", archivo:" + archivo + ", password:" + password + ", estado:" + estado + '}';
    }

}
