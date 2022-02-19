/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.math.BigDecimal;

/**
 *
 * @author ENRIQUE
 */
public class RegManualesFinanModel {
    
    private String partida;
    private BigDecimal devengado;
    private BigDecimal ejecutado;

    public RegManualesFinanModel() {
    }
    
    public RegManualesFinanModel(String partida, BigDecimal devengado, BigDecimal ejecutado) {
        this.partida = partida;
        this.devengado = devengado;
        this.ejecutado = ejecutado;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(BigDecimal ejecutado) {
        this.ejecutado = ejecutado;
    }
    
    
    
}
