/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.math.BigDecimal;

/**
 *
 * @author ORIGAMIEC
 */
public class PartidasGlobalesDistributivo {

    private String partida;
    private BigDecimal montoReformado;
    private BigDecimal reservado;
    private BigDecimal comprometido;
    private BigDecimal saldoDisponiblel;
    private BigDecimal montoReducido;
    private BigDecimal reduccion;
    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public BigDecimal getMontoReformado() {
        return montoReformado;
    }

    public void setMontoReformado(BigDecimal montoReformado) {
        this.montoReformado = montoReformado;
    }

    public BigDecimal getReservado() {
        return reservado;
    }

    public void setReservado(BigDecimal reservado) {
        this.reservado = reservado;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getSaldoDisponiblel() {
        return saldoDisponiblel;
    }

    public void setSaldoDisponiblel(BigDecimal saldoDisponiblel) {
        this.saldoDisponiblel = saldoDisponiblel;
    }

    public BigDecimal getMontoReducido() {
        return montoReducido;
    }

    public void setMontoReducido(BigDecimal montoReducido) {
        this.montoReducido = montoReducido;
    }            

    public BigDecimal getReduccion() {
        return reduccion;
    }

    public void setReduccion(BigDecimal reduccion) {
        this.reduccion = reduccion;
    }
    
}

