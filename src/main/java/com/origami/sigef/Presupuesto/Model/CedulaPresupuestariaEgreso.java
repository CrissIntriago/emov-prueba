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
public class CedulaPresupuestariaEgreso implements Serializable {

    private String codigo;
    private String item;
    private Integer nivel;
    private String economico;
    private String codigo_estructura;
    private String descripcion_estructura;
    private BigDecimal total_presupuesto_inicial;
    private BigDecimal reforma;
    private BigDecimal codificado;
    private BigDecimal reservas;
    private BigDecimal comprometido;
    private BigDecimal devengado;
    private BigDecimal pagado;
    private BigDecimal saldo_xcomprometer;
    private BigDecimal saldo_xdevengar;
    private BigDecimal saldo_xpagar;
    private boolean movimiento;
    private BigInteger padre;

    public CedulaPresupuestariaEgreso() {
        total_presupuesto_inicial = BigDecimal.ZERO;
        reforma = BigDecimal.ZERO;
        codificado = BigDecimal.ZERO;
        reservas = BigDecimal.ZERO;
        comprometido = BigDecimal.ZERO;
        devengado = BigDecimal.ZERO;
        pagado = BigDecimal.ZERO;
        saldo_xcomprometer = BigDecimal.ZERO;
        saldo_xdevengar = BigDecimal.ZERO;
        saldo_xpagar = BigDecimal.ZERO;
    }

    public CedulaPresupuestariaEgreso(String codigo, String item, String codigo_estructura, String descripcion_estructura, boolean movimiento, BigInteger padre, Integer nivel) {
        this.codigo = codigo;
        this.item = item;
        this.codigo_estructura = codigo_estructura;
        this.descripcion_estructura = descripcion_estructura;
        this.movimiento = movimiento;
        this.padre = padre;
        this.nivel = nivel;
        total_presupuesto_inicial = BigDecimal.ZERO;
        reforma = BigDecimal.ZERO;
        codificado = BigDecimal.ZERO;
        reservas = BigDecimal.ZERO;
        comprometido = BigDecimal.ZERO;
        devengado = BigDecimal.ZERO;
        pagado = BigDecimal.ZERO;
        saldo_xcomprometer = BigDecimal.ZERO;
        saldo_xdevengar = BigDecimal.ZERO;
        saldo_xpagar = BigDecimal.ZERO;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public String getCodigo_estructura() {
        return codigo_estructura;
    }

    public void setCodigo_estructura(String codigo_estructura) {
        this.codigo_estructura = codigo_estructura;
    }

    public String getDescripcion_estructura() {
        return descripcion_estructura;
    }

    public void setDescripcion_estructura(String descripcion_estructura) {
        this.descripcion_estructura = descripcion_estructura;
    }

    public BigDecimal getTotal_presupuesto_inicial() {
        return total_presupuesto_inicial;
    }

    public void setTotal_presupuesto_inicial(BigDecimal total_presupuesto_inicial) {
        this.total_presupuesto_inicial = total_presupuesto_inicial;
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

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public BigDecimal getSaldo_xcomprometer() {
        return saldo_xcomprometer;
    }

    public void setSaldo_xcomprometer(BigDecimal saldo_xcomprometer) {
        this.saldo_xcomprometer = saldo_xcomprometer;
    }

    public BigDecimal getSaldo_xdevengar() {
        return saldo_xdevengar;
    }

    public void setSaldo_xdevengar(BigDecimal saldo_xdevengar) {
        this.saldo_xdevengar = saldo_xdevengar;
    }

    public BigDecimal getSaldo_xpagar() {
        return saldo_xpagar;
    }

    public void setSaldo_xpagar(BigDecimal saldo_xpagar) {
        this.saldo_xpagar = saldo_xpagar;
    }

    public boolean isMovimiento() {
        return movimiento;
    }

    public void setMovimiento(boolean movimiento) {
        this.movimiento = movimiento;
    }

    public BigInteger getPadre() {
        return padre;
    }

    public void setPadre(BigInteger padre) {
        this.padre = padre;
    }

}
