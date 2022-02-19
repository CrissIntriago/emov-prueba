/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class CertificadoAdeudarDeta implements Serializable {

    private String idLiquidacion;
    private String nombreTitulo;
    private Integer anio;
    private String clavePredial;
    private String numLocal;
    private Date fechaIngreso;
    private String identificacion;
    private String solicitante;
    private BigDecimal valorEmitido;
    private String userLiquidador;
    public CertificadoAdeudarDeta(){
    }

    public String getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(String idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public String getNombreTitulo() {
        return nombreTitulo;
    }

    public void setNombreTitulo(String nombreTitulo) {
        this.nombreTitulo = nombreTitulo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getClavePredial() {
        return clavePredial;
    }

    public void setClavePredial(String clavePredial) {
        this.clavePredial = clavePredial;
    }

    public String getNumLocal() {
        return numLocal;
    }

    public void setNumLocal(String numLocal) {
        this.numLocal = numLocal;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public BigDecimal getValorEmitido() {
        return valorEmitido;
    }

    public void setValorEmitido(BigDecimal valorEmitido) {
        this.valorEmitido = valorEmitido;
    }

    public String getUserLiquidador() {
        return userLiquidador;
    }

    public void setUserLiquidador(String userLiquidador) {
        this.userLiquidador = userLiquidador;
    }

   
    
}
