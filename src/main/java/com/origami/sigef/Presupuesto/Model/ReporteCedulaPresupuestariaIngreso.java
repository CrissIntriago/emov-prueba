/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ReporteCedulaPresupuestariaIngreso {

    private BigDecimal totalInicial = BigDecimal.ZERO;
    private BigDecimal totalReforma = BigDecimal.ZERO;
    private BigDecimal totalCodificado = BigDecimal.ZERO;
    private BigDecimal totalDevengado = BigDecimal.ZERO;
    private BigDecimal totalRecaudado = BigDecimal.ZERO;
    private BigDecimal totalSaldoPorDevengar = BigDecimal.ZERO;

    private List<ListaItemCedulaPresupuestariaIngreso> lista;

    public ReporteCedulaPresupuestariaIngreso() {
    }

    public ReporteCedulaPresupuestariaIngreso(BigDecimal totalInicial, BigDecimal totalReforma, BigDecimal totalCodificado, BigDecimal totalDevengado,
            BigDecimal totalRecaudado, BigDecimal totalSaldoPorDevengar, List<ListaItemCedulaPresupuestariaIngreso> lista) {
        this.totalInicial = totalInicial;
        this.totalReforma = totalReforma;
        this.totalCodificado = totalCodificado;
        this.totalDevengado = totalDevengado;
        this.totalRecaudado = totalRecaudado;
        this.totalSaldoPorDevengar = totalSaldoPorDevengar;
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

    public BigDecimal getTotalSaldoPorDevengar() {
        return totalSaldoPorDevengar;
    }

    public void setTotalSaldoPorDevengar(BigDecimal totalSaldoPorDevengar) {
        this.totalSaldoPorDevengar = totalSaldoPorDevengar;
    }

    public List<ListaItemCedulaPresupuestariaIngreso> getLista() {
        return lista;
    }

    public void setLista(List<ListaItemCedulaPresupuestariaIngreso> lista) {
        this.lista = lista;
    }

}
