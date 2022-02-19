/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.util.Date;

/**
 *
 * @author Sandra Arroba
 */
public class ReformasUnificadas {
    private String periodo;
    private String tipoReforma; //T1 - Traspaso Tipo 1, T2 - Trapaso Tipo 2, SI - Suplemento Ingreso, RI -Reducción de Ingreso, SE -Suplemento de Egreso, RE - Reduccion de Egreso
    private Long idReforma;
    private String codigoReforma;
    private Date fechaRegistro; 
    private Date fechaAprobacion;
    private boolean traspasoT1OrSuplemento; //True - Traspaso Tipo 1, True Suplemento, False -Traspaso Tipo 2, False -REduccción
    private boolean ingresos;
    private Double sumplemento;
    private Double reduccion;
    private String estado;

    public ReformasUnificadas(String periodo, String tipoReforma, Long idReforma, String codigoReforma, Date fechaRegistro, Date fechaAprobacion, boolean traspasoT1OrSuplemento, boolean ingresos, Double sumplemento, Double reduccion, String estado) {
        this.periodo = periodo;
        this.tipoReforma = tipoReforma;
        this.idReforma = idReforma;
        this.codigoReforma = codigoReforma;
        this.fechaRegistro = fechaRegistro;
        this.fechaAprobacion = fechaAprobacion;
        this.traspasoT1OrSuplemento = traspasoT1OrSuplemento;
        this.ingresos = ingresos;
        this.sumplemento = sumplemento;
        this.reduccion = reduccion;
        this.estado = estado;
    }
    
    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getTipoReforma() {
        return tipoReforma;
    }

    public void setTipoReforma(String tipoReforma) {
        this.tipoReforma = tipoReforma;
    }

    public Long getIdReforma() {
        return idReforma;
    }

    public void setIdReforma(Long idReforma) {
        this.idReforma = idReforma;
    }

    public String getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(String codigoReforma) {
        this.codigoReforma = codigoReforma;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public boolean isTraspasoT1OrSuplemento() {
        return traspasoT1OrSuplemento;
    }

    public void setTraspasoT1OrSuplemento(boolean traspasoT1OrSuplemento) {
        this.traspasoT1OrSuplemento = traspasoT1OrSuplemento;
    }

    public boolean isIngresos() {
        return ingresos;
    }

    public void setIngresos(boolean ingresos) {
        this.ingresos = ingresos;
    }

    public Double getSumplemento() {
        return sumplemento;
    }

    public void setSumplemento(Double sumplemento) {
        this.sumplemento = sumplemento;
    }

    public Double getReduccion() {
        return reduccion;
    }

    public void setReduccion(Double reduccion) {
        this.reduccion = reduccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
