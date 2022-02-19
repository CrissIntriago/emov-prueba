/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.models.DetalleConvenio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class DetalleConvenioModel implements Serializable {

    private static final Logger LOG = Logger.getLogger(DetalleConvenioModel.class.getName());

    private Long convenio;
    private Integer cuota;
    private BigDecimal capitalReducido;
    private BigDecimal abono;
    private BigDecimal interes;
    private BigDecimal dividendo;
    private Date fechaPago;
    private String estado;

    public DetalleConvenioModel() {
    }

    public Long getConvenio() {
        return convenio;
    }

    public void setConvenio(Long convenio) {
        this.convenio = convenio;
    }

    public Integer getCuota() {
        return cuota;
    }

    public void setCuota(Integer cuota) {
        this.cuota = cuota;
    }

    public BigDecimal getCapitalReducido() {
        return capitalReducido;
    }

    public void setCapitalReducido(BigDecimal capitalReducido) {
        this.capitalReducido = capitalReducido;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getDividendo() {
        return dividendo;
    }

    public void setDividendo(BigDecimal dividendo) {
        this.dividendo = dividendo;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
