/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ReporteCedulaPresupuestariaEgresoConsolidada implements Serializable {

    private BigDecimal totalInicial;
    private BigDecimal totalReforma;
    private BigDecimal totalCodificado;
    private BigDecimal totalDevengado;
    private BigDecimal totalRecaudado;
    private BigDecimal totalReservas;
    private BigDecimal totalComprometido;
    private BigDecimal totalPagado;
    private BigDecimal totalSaldoXcomprometer;
    private BigDecimal totalSaldoXdevengar;
    private BigDecimal tootalSaldoXPagar;
    private List<CedulaPresupuestariaEgresoConsolidada> lista;

    public ReporteCedulaPresupuestariaEgresoConsolidada() {

    }

    public ReporteCedulaPresupuestariaEgresoConsolidada(BigDecimal totalInicial, BigDecimal totalReforma, BigDecimal totalCodificado, BigDecimal totalDevengado, BigDecimal totalRecaudado, BigDecimal totalReservas, BigDecimal totalComprometido, BigDecimal totalPagado, BigDecimal totalSaldoXcomprometer, BigDecimal totalSaldoXdevengar, BigDecimal tootalSaldoXPagar, List<CedulaPresupuestariaEgresoConsolidada> lista) {
        this.totalInicial = totalInicial;
        this.totalReforma = totalReforma;
        this.totalCodificado = totalCodificado;
        this.totalDevengado = totalDevengado;
        this.totalRecaudado = totalRecaudado;
        this.totalReservas = totalReservas;
        this.totalComprometido = totalComprometido;
        this.totalPagado = totalPagado;
        this.totalSaldoXcomprometer = totalSaldoXcomprometer;
        this.totalSaldoXdevengar = totalSaldoXdevengar;
        this.tootalSaldoXPagar = tootalSaldoXPagar;
        this.lista = lista;
    }

    public BigDecimal getTotalInicial() {
        return totalInicial;
    }

    public void setTotalInicial(BigDecimal totalInicial) {
        this.totalInicial = totalInicial;
    }

    public BigDecimal getTotalReforma() {
        return totalReforma;
    }

    public void setTotalReforma(BigDecimal totalReforma) {
        this.totalReforma = totalReforma;
    }

    public BigDecimal getTotalCodificado() {
        return totalCodificado;
    }

    public void setTotalCodificado(BigDecimal totalCodificado) {
        this.totalCodificado = totalCodificado;
    }

    public BigDecimal getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(BigDecimal totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public BigDecimal getTotalRecaudado() {
        return totalRecaudado;
    }

    public void setTotalRecaudado(BigDecimal totalRecaudado) {
        this.totalRecaudado = totalRecaudado;
    }

    public BigDecimal getTotalReservas() {
        return totalReservas;
    }

    public void setTotalReservas(BigDecimal totalReservas) {
        this.totalReservas = totalReservas;
    }

    public BigDecimal getTotalComprometido() {
        return totalComprometido;
    }

    public void setTotalComprometido(BigDecimal totalComprometido) {
        this.totalComprometido = totalComprometido;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getTotalSaldoXcomprometer() {
        return totalSaldoXcomprometer;
    }

    public void setTotalSaldoXcomprometer(BigDecimal totalSaldoXcomprometer) {
        this.totalSaldoXcomprometer = totalSaldoXcomprometer;
    }

    public BigDecimal getTotalSaldoXdevengar() {
        return totalSaldoXdevengar;
    }

    public void setTotalSaldoXdevengar(BigDecimal totalSaldoXdevengar) {
        this.totalSaldoXdevengar = totalSaldoXdevengar;
    }

    public BigDecimal getTootalSaldoXPagar() {
        return tootalSaldoXPagar;
    }

    public void setTootalSaldoXPagar(BigDecimal tootalSaldoXPagar) {
        this.tootalSaldoXPagar = tootalSaldoXPagar;
    }

    public List<CedulaPresupuestariaEgresoConsolidada> getLista() {
        return lista;
    }

    public void setLista(List<CedulaPresupuestariaEgresoConsolidada> lista) {
        this.lista = lista;
    }

    
    
}
