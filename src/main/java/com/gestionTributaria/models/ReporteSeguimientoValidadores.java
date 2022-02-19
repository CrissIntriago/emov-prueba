/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class ReporteSeguimientoValidadores {

    private String user;
    private String nombres;
    private BigInteger asignados;
    private BigInteger validados;
    private Double eficiencia;

    public ReporteSeguimientoValidadores() {
    }

  

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public BigInteger getAsignados() {
        return asignados;
    }

    public void setAsignados(BigInteger asignados) {
        this.asignados = asignados;
    }

    public BigInteger getValidados() {
        return validados;
    }

    public void setValidados(BigInteger validados) {
        this.validados = validados;
    }

    public Double getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(Double eficiencia) {
        this.eficiencia = eficiencia;
    }

}
