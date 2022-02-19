/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.modelTarifario;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;

/**
 *
 * @author OrigamiEC
 */
public class ModelExcel {

    private String numPapeleta;
    private String numOrden;
    private BigDecimal valor;
    private String Placa;
    private String totalValor;
    private Boolean estado;
    private Date fecha;

    @PostConstruct
    public void init() {
        this.estado = Boolean.FALSE;
        this.fecha = new Date();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumPapeleta() {
        return numPapeleta;
    }

    public void setNumPapeleta(String numPapeleta) {
        this.numPapeleta = numPapeleta;
    }

    public String getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(String numOrden) {
        this.numOrden = numOrden;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String Placa) {
        this.Placa = Placa;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(String totalValor) {
        this.totalValor = totalValor;
    }

}
