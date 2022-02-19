/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author jesus
 */
public class DetalleReporteMovimientoCuenta {

    private BigInteger numTransaccion;
    private String tipo;
    private String fecha;
    private BigInteger numComprobantePago;
    private String beneficiario;
    private String concepto;
    private BigDecimal debe;
    private BigDecimal haber;
    private BigDecimal saldo;

    public DetalleReporteMovimientoCuenta() {
    }

    public DetalleReporteMovimientoCuenta(BigInteger numTransaccion, String tipo, String fecha, BigInteger numComprobantePago,
            String beneficiario, String concepto, BigDecimal debe, BigDecimal haber, BigDecimal saldo) {
        this.numTransaccion = numTransaccion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.numComprobantePago = numComprobantePago;
        this.beneficiario = beneficiario;
        this.concepto = concepto;
        this.debe = debe;
        this.haber = haber;
        this.saldo = saldo;
    }

    public BigInteger getNumTransaccion() {
        return numTransaccion;
    }

    public void setNumTransaccion(BigInteger numTransaccion) {
        this.numTransaccion = numTransaccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigInteger getNumComprobantePago() {
        return numComprobantePago;
    }

    public void setNumComprobantePago(BigInteger numComprobantePago) {
        this.numComprobantePago = numComprobantePago;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "DetalleReporteMovimientoCuenta{" + "numTransaccion=" + numTransaccion + ", tipo=" + tipo + ", fecha=" + fecha + ", numComprobantePago=" + numComprobantePago + ", beneficiario=" + beneficiario + ", concepto=" + concepto + ", debe=" + debe + ", haber=" + haber + ", saldo=" + saldo + '}';
    }
    
    

}
