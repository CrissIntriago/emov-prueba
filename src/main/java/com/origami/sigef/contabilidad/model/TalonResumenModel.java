/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.math.BigDecimal;

/**
 *
 * @author jesus
 */
public class TalonResumenModel {

    Long liquidacionId;
    Long facturaId;
    String codCompra;
    String transaccion;
    BigDecimal bi_tarifa0;
    BigDecimal bi_tarifa_diferente0;
    BigDecimal bi_no_objetivoIva;
    BigDecimal valor_iva;
    Integer numRegistros;

    //totales de este man 
    public TalonResumenModel(Long liquidacionId, Long facturaId, String codCompra, String transaccion, BigDecimal bi_tarifa0, BigDecimal bi_tarifa_diferente0, BigDecimal bi_no_objetivoIva, BigDecimal valor_iva) {
        this.liquidacionId = liquidacionId;
        this.facturaId = facturaId;
        this.codCompra = codCompra;
        this.transaccion = transaccion;
        this.bi_tarifa0 = bi_tarifa0;
        this.bi_tarifa_diferente0 = bi_tarifa_diferente0;
        this.bi_no_objetivoIva = bi_no_objetivoIva;
        this.valor_iva = valor_iva;
    }

    public TalonResumenModel() {
        this.bi_tarifa0 = BigDecimal.ZERO;
        this.bi_no_objetivoIva = BigDecimal.ZERO;
        this.valor_iva = BigDecimal.ZERO;
        this.bi_tarifa_diferente0 = BigDecimal.ZERO;
    }

    public Integer getNumRegistros() {
        return numRegistros;
    }

    public void setNumRegistros(Integer numRegistros) {
        this.numRegistros = numRegistros;
    }

    public Long getLiquidacionId() {
        return liquidacionId;
    }

    public void setLiquidacionId(Long liquidacionId) {
        this.liquidacionId = liquidacionId;
    }

    public Long getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Long facturaId) {
        this.facturaId = facturaId;
    }

    public String getCodCompra() {
        return codCompra;
    }

    public void setCodCompra(String codCompra) {
        this.codCompra = codCompra;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public BigDecimal getBi_tarifa0() {
        return bi_tarifa0;
    }

    public void setBi_tarifa0(BigDecimal bi_tarifa0) {
        this.bi_tarifa0 = bi_tarifa0;
    }

    public BigDecimal getBi_tarifa_diferente0() {
        return bi_tarifa_diferente0;
    }

    public void setBi_tarifa_diferente0(BigDecimal bi_tarifa_diferente0) {
        this.bi_tarifa_diferente0 = bi_tarifa_diferente0;
    }

    public BigDecimal getBi_no_objetivoIva() {
        return bi_no_objetivoIva;
    }

    public void setBi_no_objetivoIva(BigDecimal bi_no_objetivoIva) {
        this.bi_no_objetivoIva = bi_no_objetivoIva;
    }

    public BigDecimal getValor_iva() {
        return valor_iva;
    }

    public void setValor_iva(BigDecimal valor_iva) {
        this.valor_iva = valor_iva;
    }

    @Override
    public String toString() {
        return "TalonResumenModel{" + "liquidacionId=" + liquidacionId
                + ", facturaId=" + facturaId + ", codCompra=" + codCompra
                + ", transaccion=" + transaccion + ", bi_tarifa0=" + bi_tarifa0
                + ", bi_tarifa_diferente0=" + bi_tarifa_diferente0 + ", bi_no_objetivoIva="
                + bi_no_objetivoIva + ", valor_iva=" + valor_iva + ", numRegistros=" + numRegistros + '}';
    }

}
