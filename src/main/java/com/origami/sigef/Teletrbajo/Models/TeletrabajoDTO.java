/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Models;

import com.origami.sigef.Teletrbajo.Entity.Teletrabajo;
import java.io.Serializable;

/**
 *
 * @author DEVELOPER
 */
public class TeletrabajoDTO  implements Serializable{

    private Teletrabajo teletrabajo;
    private String avanceUno;
    private String avanceDos;
    private String avanceTres;
    private String unidad;
    private String tiempoEjecucion;

    public TeletrabajoDTO() {
    }

    public Teletrabajo getTeletrabajo() {
        return teletrabajo;
    }

    public void setTeletrabajo(Teletrabajo teletrabajo) {
        this.teletrabajo = teletrabajo;
    }

    public String getAvanceUno() {
        return avanceUno;
    }

    public void setAvanceUno(String avanceUno) {
        this.avanceUno = avanceUno;
    }

    public String getAvanceDos() {
        return avanceDos;
    }

    public void setAvanceDos(String avanceDos) {
        this.avanceDos = avanceDos;
    }

    public String getAvanceTres() {
        return avanceTres;
    }

    public void setAvanceTres(String avanceTres) {
        this.avanceTres = avanceTres;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(String tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }
    
    
    

}
