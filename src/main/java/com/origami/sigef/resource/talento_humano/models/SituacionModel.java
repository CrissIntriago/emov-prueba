/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.models;

/**
 *
 * @author Criss Intriago
 */
public class SituacionModel {

    private String proceso;
    private String subProceso;
    private String puesto;
    private String lugarTrabajo;
    private double remuneracion;
    private String codPartida;
    private String nomPartida;

    public SituacionModel() {

    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getSubProceso() {
        return subProceso;
    }

    public void setSubProceso(String subProceso) {
        this.subProceso = subProceso;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(String lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public double getRemuneracion() {
        return remuneracion;
    }

    public void setRemuneracion(double remuneracion) {
        this.remuneracion = remuneracion;
    }

    public String getCodPartida() {
        return codPartida;
    }

    public void setCodPartida(String codPartida) {
        this.codPartida = codPartida;
    }

    public String getNomPartida() {
        return nomPartida;
    }

    public void setNomPartida(String nomPartida) {
        this.nomPartida = nomPartida;
    }

}
