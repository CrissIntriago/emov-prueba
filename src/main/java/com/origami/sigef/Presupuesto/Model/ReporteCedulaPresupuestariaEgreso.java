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
public class ReporteCedulaPresupuestariaEgreso implements Serializable {

    private BigDecimal totalInicial;
    private BigDecimal totalReformas;
    private BigDecimal totalCodificado;
    private BigDecimal totalReservas;
    private BigDecimal totalComprometido;
    private BigDecimal totalDevengado;
    private BigDecimal totalPagado;
    private BigDecimal totalPorComprometer;
    private BigDecimal totalPorDevengar;
    private BigDecimal totalPorPagar;
    private List<CedulaPresupuestariaEgreso> lista;

    public ReporteCedulaPresupuestariaEgreso() {
    }

    public ReporteCedulaPresupuestariaEgreso(BigDecimal totalInicial, BigDecimal totalReformas, BigDecimal totalCodificado, BigDecimal totalReservas, BigDecimal totalComprometido, BigDecimal totalDevengado, BigDecimal totalPagado, BigDecimal totalPorComprometer, BigDecimal totalPorDevengar, BigDecimal totalPorPagar, List<CedulaPresupuestariaEgreso> lista) {
        this.totalInicial = totalInicial;
        this.totalReformas = totalReformas;
        this.totalCodificado = totalCodificado;
        this.totalReservas = totalReservas;
        this.totalComprometido = totalComprometido;
        this.totalDevengado = totalDevengado;
        this.totalPagado = totalPagado;
        this.totalPorComprometer = totalPorComprometer;
        this.totalPorDevengar = totalPorDevengar;
        this.totalPorPagar = totalPorPagar;
        this.lista = lista;
    }

    public BigDecimal getTotalInicial() {
        return totalInicial;
    }

    public void setTotalInicial(BigDecimal totalInicial) {
        this.totalInicial = totalInicial;
    }

    public BigDecimal getTotalReformas() {
        return totalReformas;
    }

    public void setTotalReformas(BigDecimal totalReformas) {
        this.totalReformas = totalReformas;
    }

    public BigDecimal getTotalCodificado() {
        return totalCodificado;
    }

    public void setTotalCodificado(BigDecimal totalCodificado) {
        this.totalCodificado = totalCodificado;
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

    public BigDecimal getTotalDevengado() {
        return totalDevengado;
    }

    public void setTotalDevengado(BigDecimal totalDevengado) {
        this.totalDevengado = totalDevengado;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getTotalPorComprometer() {
        return totalPorComprometer;
    }

    public void setTotalPorComprometer(BigDecimal totalPorComprometer) {
        this.totalPorComprometer = totalPorComprometer;
    }

    public BigDecimal getTotalPorDevengar() {
        return totalPorDevengar;
    }

    public void setTotalPorDevengar(BigDecimal totalPorDevengar) {
        this.totalPorDevengar = totalPorDevengar;
    }

    public BigDecimal getTotalPorPagar() {
        return totalPorPagar;
    }

    public void setTotalPorPagar(BigDecimal totalPorPagar) {
        this.totalPorPagar = totalPorPagar;
    }

    public List<CedulaPresupuestariaEgreso> getLista() {
        return lista;
    }

    public void setLista(List<CedulaPresupuestariaEgreso> lista) {
        this.lista = lista;
    }

}
