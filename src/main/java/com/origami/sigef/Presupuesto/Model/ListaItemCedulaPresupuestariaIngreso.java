/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class ListaItemCedulaPresupuestariaIngreso {

    private BigInteger id;
    private String codigo;
    private String descripcion;
    private Short periodo;
    private Integer nivel;
    private BigInteger fuente;
    private String nombre;
    private BigDecimal presupuesto_inicial;
    private BigDecimal reforma;
    private BigDecimal codificado;
    private BigDecimal devengado;
    private BigDecimal recaudado;
    private Boolean movimiento;
    private BigInteger padre;
    private BigDecimal saldoPorDevengar;

    public ListaItemCedulaPresupuestariaIngreso() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public BigInteger getFuente() {
        return fuente;
    }

    public void setFuente(BigInteger fuente) {
        this.fuente = fuente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPresupuesto_inicial() {
        return presupuesto_inicial;
    }

    public void setPresupuesto_inicial(BigDecimal presupuesto_inicial) {
        this.presupuesto_inicial = presupuesto_inicial;
    }

    public BigDecimal getReforma() {
        return reforma;
    }

    public void setReforma(BigDecimal reforma) {
        this.reforma = reforma;
    }

    public BigDecimal getCodificado() {
        return codificado;
    }

    public void setCodificado(BigDecimal codificado) {
        this.codificado = codificado;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getRecaudado() {
        return recaudado;
    }

    public void setRecaudado(BigDecimal recaudado) {
        this.recaudado = recaudado;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getPadre() {
        return padre;
    }

    public void setPadre(BigInteger padre) {
        this.padre = padre;
    }

    public BigDecimal getSaldoPorDevengar() {
        return saldoPorDevengar;
    }

    public void setSaldoPorDevengar(BigDecimal sladoPorDevengar) {
        this.saldoPorDevengar = sladoPorDevengar;
    }

}
