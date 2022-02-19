/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Models;

import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class FinaRenLiquidacionModel {

    private Long id;
    private Integer anio;
    private BigDecimal totalPago;
    private BigDecimal descuento;
    private BigDecimal interes;
    private BigDecimal recargo;
    private BigDecimal valorCoactiva;
    private BigDecimal pagoFinal;
    private BigDecimal abonado;

    public FinaRenLiquidacionModel() {
    }

    public FinaRenLiquidacionModel(Long id, Integer anio, BigDecimal totalPago, BigDecimal descuento,
            BigDecimal interes, BigDecimal recargo, BigDecimal valorCoactiva, BigDecimal pagoFinal, BigDecimal abonado) {
        this.id = id;
        this.anio = anio;
        this.totalPago = totalPago;
        this.descuento = descuento;
        this.interes = interes;
        this.recargo = recargo;
        this.valorCoactiva = valorCoactiva;
        this.pagoFinal = pagoFinal;
        this.abonado = abonado;
    }

    public BigDecimal getAbonado() {
        return abonado;
    }

    public void setAbonado(BigDecimal abonado) {
        this.abonado = abonado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getValorCoactiva() {
        return valorCoactiva;
    }

    public void setValorCoactiva(BigDecimal valorCoactiva) {
        this.valorCoactiva = valorCoactiva;
    }

    public BigDecimal getPagoFinal() {
        return pagoFinal;
    }

    public void setPagoFinal(BigDecimal pagoFinal) {
        this.pagoFinal = pagoFinal;
    }

    @Override
    public String toString() {
        return "FinaRenLiquidacionModels{" + "id=" + id + ", anio=" + anio + ", totalPago=" + totalPago
                + ", descuento=" + descuento + ", interes=" + interes + ", recargo=" + recargo
                + ", valorCoactiva=" + valorCoactiva + ", pagoFinal=" + pagoFinal + ", abonado=" + abonado + '}';
    }

}
