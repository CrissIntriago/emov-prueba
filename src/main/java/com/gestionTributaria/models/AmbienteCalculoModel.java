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
public class AmbienteCalculoModel implements Serializable {

    private Integer veces = 1;
    private BigDecimal valorTonelada = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;

    public AmbienteCalculoModel() {
    }

    public AmbienteCalculoModel(Integer veces, BigDecimal valorTonelada) {
        this.veces = veces;
        this.valorTonelada = valorTonelada;

        if (this.veces != null && this.valorTonelada != null) {
            this.total = BigDecimal.valueOf(this.veces.longValue()).multiply(this.valorTonelada);
        } else {
            this.total = BigDecimal.ZERO;
        }
    }

    public Integer getVeces() {
        return veces;
    }

    public void setVeces(Integer veces) {
        this.veces = veces;
    }

    public BigDecimal getValorTonelada() {
        return valorTonelada;
    }

    public void setValorTonelada(BigDecimal valorTonelada) {
        this.valorTonelada = valorTonelada;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
