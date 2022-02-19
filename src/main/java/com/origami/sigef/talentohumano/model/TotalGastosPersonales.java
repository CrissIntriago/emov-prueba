/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.model;

import java.math.BigDecimal;

/**
 *
 * @author ORIGAMI2
 */
public class TotalGastosPersonales {

    private BigDecimal ingreso_gravado;
    private BigDecimal otros_ingresos;
    private BigDecimal total_ingreso;
    private BigDecimal gasto_vestimenta;
    private BigDecimal gasto_educacion;
    private BigDecimal gasto_salud;
    private BigDecimal gasto_turismo;
    private BigDecimal gasto_alimentacion;
    private BigDecimal gasto_vivienda;
    private BigDecimal total_gasto;

    private BigDecimal impuesto_renta_anual;
    private BigDecimal cuota_mensual;

    public TotalGastosPersonales() {
    }

    public TotalGastosPersonales(BigDecimal total_ingreso, BigDecimal impuesto_renta_anual, BigDecimal cuota_mensual) {
        this.total_ingreso = total_ingreso;
        this.impuesto_renta_anual = impuesto_renta_anual;
        this.cuota_mensual = cuota_mensual;
    }

    public TotalGastosPersonales(BigDecimal ingreso_gravado, BigDecimal otros_ingresos, BigDecimal total_ingreso, BigDecimal gasto_vestimenta, BigDecimal gasto_educacion, BigDecimal gasto_salud, BigDecimal gasto_turismo, BigDecimal gasto_alimentacion, BigDecimal gasto_vivienda, BigDecimal total_gasto) {
        this.ingreso_gravado = ingreso_gravado;
        this.otros_ingresos = otros_ingresos;
        this.total_ingreso = total_ingreso;
        this.gasto_vestimenta = gasto_vestimenta;
        this.gasto_educacion = gasto_educacion;
        this.gasto_salud = gasto_salud;
        this.gasto_turismo = gasto_turismo;
        this.gasto_alimentacion = gasto_alimentacion;
        this.gasto_vivienda = gasto_vivienda;
        this.total_gasto = total_gasto;
    }

    public BigDecimal getIngreso_gravado() {
        return ingreso_gravado;
    }

    public void setIngreso_gravado(BigDecimal ingreso_gravado) {
        this.ingreso_gravado = ingreso_gravado;
    }

    public BigDecimal getOtros_ingresos() {
        return otros_ingresos;
    }

    public void setOtros_ingresos(BigDecimal otros_ingresos) {
        this.otros_ingresos = otros_ingresos;
    }

    public BigDecimal getTotal_ingreso() {
        return total_ingreso;
    }

    public void setTotal_ingreso(BigDecimal total_ingreso) {
        this.total_ingreso = total_ingreso;
    }

    public BigDecimal getGasto_vestimenta() {
        return gasto_vestimenta;
    }

    public void setGasto_vestimenta(BigDecimal gasto_vestimenta) {
        this.gasto_vestimenta = gasto_vestimenta;
    }

    public BigDecimal getGasto_educacion() {
        return gasto_educacion;
    }

    public void setGasto_educacion(BigDecimal gasto_educacion) {
        this.gasto_educacion = gasto_educacion;
    }

    public BigDecimal getGasto_salud() {
        return gasto_salud;
    }

    public void setGasto_salud(BigDecimal gasto_salud) {
        this.gasto_salud = gasto_salud;
    }

    public BigDecimal getGasto_turismo() {
        return gasto_turismo;
    }

    public void setGasto_turismo(BigDecimal gasto_turismo) {
        this.gasto_turismo = gasto_turismo;
    }

    public BigDecimal getGasto_alimentacion() {
        return gasto_alimentacion;
    }

    public void setGasto_alimentacion(BigDecimal gasto_alimentacion) {
        this.gasto_alimentacion = gasto_alimentacion;
    }

    public BigDecimal getGasto_vivienda() {
        return gasto_vivienda;
    }

    public void setGasto_vivienda(BigDecimal gasto_vivienda) {
        this.gasto_vivienda = gasto_vivienda;
    }

    public BigDecimal getTotal_gasto() {
        return total_gasto;
    }

    public void setTotal_gasto(BigDecimal total_gasto) {
        this.total_gasto = total_gasto;
    }

    public BigDecimal getImpuesto_renta_anual() {
        return impuesto_renta_anual;
    }

    public void setImpuesto_renta_anual(BigDecimal impuesto_renta_anual) {
        this.impuesto_renta_anual = impuesto_renta_anual;
    }

    public BigDecimal getCuota_mensual() {
        return cuota_mensual;
    }

    public void setCuota_mensual(BigDecimal cuota_mensual) {
        this.cuota_mensual = cuota_mensual;
    }

    @Override
    public String toString() {
        return "TotalGastosPersonales{" + "ingreso_gravado=" + ingreso_gravado + ", otros_ingresos=" + otros_ingresos + ", total_ingreso=" + total_ingreso + ", gasto_vestimenta=" + gasto_vestimenta + ", gasto_educacion=" + gasto_educacion + ", gasto_salud=" + gasto_salud + ", gasto_turismo=" + gasto_turismo + ", gasto_alimentacion=" + gasto_alimentacion + ", gasto_vivienda=" + gasto_vivienda + ", total_gasto=" + total_gasto + ", impuesto_renta_anual=" + impuesto_renta_anual + ", cuota_mensual=" + cuota_mensual + '}';
    }

}
