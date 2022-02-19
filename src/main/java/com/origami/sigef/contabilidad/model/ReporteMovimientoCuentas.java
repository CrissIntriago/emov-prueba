/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author jesus
 */
public class ReporteMovimientoCuentas {

    private String codigoCuenta;
    private String nombreCuenta;
    private List<DetalleReporteMovimientoCuenta> detalleReporteMovimientoCuentas;
    private BigDecimal totalDebe;
    private BigDecimal totalHaber;
    private BigDecimal totalSaldo;
    private BigDecimal saldoAnterior;

    public ReporteMovimientoCuentas() {
    }

    public ReporteMovimientoCuentas(String codigoCuenta, String nombreCuenta, BigDecimal saldoAnterior) {
        this.codigoCuenta = codigoCuenta;
        this.nombreCuenta = nombreCuenta;
        this.saldoAnterior = saldoAnterior;
        this.totalDebe = BigDecimal.ZERO;
        this.totalHaber = BigDecimal.ZERO;
        this.totalSaldo = BigDecimal.ZERO;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public List<DetalleReporteMovimientoCuenta> getDetalleReporteMovimientoCuentas() {
        return detalleReporteMovimientoCuentas;
    }

    public void setDetalleReporteMovimientoCuentas(List<DetalleReporteMovimientoCuenta> detalleReporteMovimientoCuentas) {
        this.detalleReporteMovimientoCuentas = detalleReporteMovimientoCuentas;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public BigDecimal getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(BigDecimal totalSaldo) {
        this.totalSaldo = totalSaldo;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    @Override
    public String toString() {
        return "ReporteMovimientoCuentas{" + "codigoCuenta=" + codigoCuenta + ", nombreCuenta=" + nombreCuenta + ", detalleReporteMovimientoCuentas=" + detalleReporteMovimientoCuentas + ", totalDebe=" + totalDebe + ", totalHaber=" + totalHaber + ", totalSaldo=" + totalSaldo + ", saldoAnterior=" + saldoAnterior + '}';
    }

}
