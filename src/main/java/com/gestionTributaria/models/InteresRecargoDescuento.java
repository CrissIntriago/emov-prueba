/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class InteresRecargoDescuento implements Serializable {

    private BigDecimal recargo;
    private BigDecimal descuento;
    private BigDecimal interes;

    public InteresRecargoDescuento(BigDecimal recargo, BigDecimal descuento, BigDecimal interes) {
        this.recargo = recargo;
        this.descuento = descuento;
        this.interes = interes;
    }

    public InteresRecargoDescuento() {

    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    @Override
    public String toString() {
        return "InteresRecargoDescuento{" + "recargo=" + recargo + ", descuento=" + descuento + ", interes=" + interes + '}';
    }

}
