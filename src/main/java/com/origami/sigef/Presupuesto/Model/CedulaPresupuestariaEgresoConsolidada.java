/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class CedulaPresupuestariaEgresoConsolidada implements Serializable {

    private String codigo;
    private String descripcion;
    private Short periodo;
    private Integer nivel;
    private String economico;
    private BigDecimal presupuestInicial;
    private BigDecimal reforma;
    private BigDecimal codificado;
    private BigDecimal devengado;
    private BigDecimal recaudado;
    private BigDecimal reservas;
    private BigDecimal comprometido;
    private BigDecimal pagado;
    private BigDecimal saldoXcomprometer;
    private BigDecimal saldoXdevengar;
    private BigDecimal saldoXPagar;
    private BigInteger padre;
    private boolean movimiento;

    public CedulaPresupuestariaEgresoConsolidada() {
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

    public String getEconomico() {
        return economico;
    }

    public void setEconomico(String economico) {
        this.economico = economico;
    }

    public BigDecimal getPresupuestInicial() {
        return presupuestInicial;
    }

    public void setPresupuestInicial(BigDecimal presupuestInicial) {
        this.presupuestInicial = presupuestInicial;
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

    public BigDecimal getReservas() {
        return reservas;
    }

    public void setReservas(BigDecimal reservas) {
        this.reservas = reservas;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public BigDecimal getSaldoXcomprometer() {
        return saldoXcomprometer;
    }

    public void setSaldoXcomprometer(BigDecimal saldoXcomprometer) {
        this.saldoXcomprometer = saldoXcomprometer;
    }

    public BigDecimal getSaldoXdevengar() {
        return saldoXdevengar;
    }

    public void setSaldoXdevengar(BigDecimal saldoXdevengar) {
        this.saldoXdevengar = saldoXdevengar;
    }

    public BigDecimal getSaldoXPagar() {
        return saldoXPagar;
    }

    public void setSaldoXPagar(BigDecimal saldoXPagar) {
        this.saldoXPagar = saldoXPagar;
    }

    public BigInteger getPadre() {
        return padre;
    }

    public void setPadre(BigInteger padre) {
        this.padre = padre;
    }

    public boolean isMovimiento() {
        return movimiento;
    }

    public void setMovimiento(boolean movimiento) {
        this.movimiento = movimiento;
    }

}
