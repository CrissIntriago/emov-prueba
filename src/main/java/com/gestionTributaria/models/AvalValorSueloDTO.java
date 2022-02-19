/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class AvalValorSueloDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private short parroquia;
    private short zona;
    private short sector;
    private short mz;
    private BigDecimal valorM2;
    private Integer anioInicio;
    private Integer anioFin;
    private boolean estado = true;
    private short solar;
    

    public AvalValorSueloDTO() {
    }

    public AvalValorSueloDTO(Long id) {
        this.id = id;
    }

    public AvalValorSueloDTO(Long id, short parroquia, short zona, short sector, short mz, BigDecimal valorM2) {
        this.id = id;
        this.parroquia = parroquia;
        this.zona = zona;
        this.sector = sector;
        this.mz = mz;
        this.valorM2 = valorM2;
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

    public short getMz() {
        return mz;
    }

    public void setMz(short mz) {
        this.mz = mz;
    }

    public BigDecimal getValorM2() {
        return valorM2;
    }

    public void setValorM2(BigDecimal valorM2) {
        this.valorM2 = valorM2;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public short getSolar() {
        return solar;
    }

    public void setSolar(short solar) {
        this.solar = solar;
    }
    
    

    
}
