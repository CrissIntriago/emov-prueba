/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Criss Intriago
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ventaEst", propOrder = {"codEstab", "ventasEstab", "ivaComp"})
@XmlRootElement
public class VentaEst {

    private String codEstab;
    private BigDecimal ventasEstab;
    private BigDecimal ivaComp;

    public VentaEst() {
    }

    public VentaEst(String codEstab, BigDecimal ventasEstab, BigDecimal ivaComp) {
        this.codEstab = codEstab;
        this.ventasEstab = ventasEstab;
        this.ivaComp = ivaComp;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getCodEstab() {
        return codEstab;
    }

    public void setCodEstab(String codEstab) {
        this.codEstab = codEstab;
    }

    public BigDecimal getVentasEstab() {
        return ventasEstab;
    }

    public void setVentasEstab(BigDecimal ventasEstab) {
        this.ventasEstab = ventasEstab;
    }

    public BigDecimal getIvaComp() {
        return ivaComp;
    }

    public void setIvaComp(BigDecimal ivaComp) {
        this.ivaComp = ivaComp;
    }
//</editor-fold>
}
