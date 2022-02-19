/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.gestionTributaria.Entities.AvalBandaImpositiva;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class AvalImpuestoPrediosDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private short parroquia;
    private short zona;
    private short sector;
    private short solar;
    private short mz;
    private Integer anioInicio;
    private Integer anioFin;
    private AvalBandaImpositiva bandaImpositiva;
    private String estado;

    public AvalImpuestoPrediosDTO() {
    }

    public AvalImpuestoPrediosDTO(Long id) {
        this.id = id;
    }

    public AvalImpuestoPrediosDTO(Long id, short parroquia, short zona, short sector, short mz) {
        this.id = id;
        this.parroquia = parroquia;
        this.zona = zona;
        this.sector = sector;
        this.mz = mz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getParroquia() {
        return parroquia;
    }

    public void setParroquia(short parroquia) {
        this.parroquia = parroquia;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
    }

    public short getSector() {
        return sector;
    }

    public void setSector(short sector) {
        this.sector = sector;
    }

    public short getSolar() {
        return solar;
    }

    public void setSolar(short solar) {
        this.solar = solar;
    }

    public short getMz() {
        return mz;
    }

    public void setMz(short mz) {
        this.mz = mz;
    }

    public Integer getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Integer anioInicio) {
        this.anioInicio = anioInicio;
    }

    public Integer getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Integer anioFin) {
        this.anioFin = anioFin;
    }

    public AvalBandaImpositiva getBandaImpositiva() {
        return bandaImpositiva;
    }

    public void setBandaImpositiva(AvalBandaImpositiva bandaImpositiva) {
        this.bandaImpositiva = bandaImpositiva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    

}
