/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Criss Intriago
 */
public class RolDetalleModel implements Serializable {

    private Long idcuenta;
    private Long idestructura;
    private Long idpresupuesto;
    private Long idfuente;
    private String partida;
    private BigDecimal debe;
    private BigDecimal haber;

    public RolDetalleModel() {

    }

    public RolDetalleModel(Long idcuenta, Long idestructura, Long idpresupuesto, Long idfuente, String partida, BigDecimal debe, BigDecimal haber) {
        this.idcuenta = idcuenta;
        this.idestructura = idestructura;
        this.idpresupuesto = idpresupuesto;
        this.idfuente = idfuente;
        this.partida = partida;
        this.debe = debe;
        this.haber = haber;
    }

    public Long getIdcuenta() {
        return idcuenta;
    }

    public void setIdcuenta(Long idcuenta) {
        this.idcuenta = idcuenta;
    }

    public Long getIdestructura() {
        return idestructura;
    }

    public void setIdestructura(Long idestructura) {
        this.idestructura = idestructura;
    }

    public Long getIdpresupuesto() {
        return idpresupuesto;
    }

    public void setIdpresupuesto(Long idpresupuesto) {
        this.idpresupuesto = idpresupuesto;
    }

    public Long getIdfuente() {
        return idfuente;
    }

    public void setIdfuente(Long idfuente) {
        this.idfuente = idfuente;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

}
