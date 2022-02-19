/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleVentas", propOrder = {"tpIdCliente", "idCliente", "parteRelVtas", "tipoComprobante", "tipoEmision", "numeroComprobantes",
    "baseNoGraIva", "baseImponible", "baseImpGrav", "montoIva", "compensaciones", "montoIce", "valorRetIva", "valorRetRenta",
    "formasDePago"})
@XmlRootElement
public class DetalleVentas implements Serializable {

    private String tpIdCliente;
    private String idCliente;
    private String parteRelVtas = "NO";
    private String tipoComprobante;
    private String tipoEmision;
    private BigInteger numeroComprobantes;
    private BigDecimal baseNoGraIva = BigDecimal.ZERO.setScale(2);
    private BigDecimal baseImponible;
    private BigDecimal baseImpGrav;
    private BigDecimal montoIva;
    private Compensaciones compensaciones;
    private BigDecimal montoIce = BigDecimal.ZERO.setScale(2);
    private BigDecimal valorRetIva = BigDecimal.ZERO.setScale(2);
    private BigDecimal valorRetRenta = BigDecimal.ZERO.setScale(2);
    private FormasPagoModel formasDePago;

    public DetalleVentas() {
    }

    public DetalleVentas(String tpIdCliente, String idCliente, String tipoComprobante, String tipoEmision, BigInteger numeroComprobantes,
            BigDecimal baseImponible, BigDecimal baseImpGrav, BigDecimal montoIva) {
        this.tpIdCliente = tpIdCliente;
        this.idCliente = idCliente;
        this.tipoComprobante = tipoComprobante;
        this.tipoEmision = tipoEmision;
        this.baseImponible = baseImponible;
        this.baseImpGrav = baseImpGrav;
        this.montoIva = montoIva;
        this.numeroComprobantes = numeroComprobantes;
    }

    public DetalleVentas(String tpIdCliente, String idCliente, String parteRelVtas,
            String tipoComprobante, String tipoEmision, BigInteger numeroComprobantes,
            BigDecimal baseNoGraIva, BigDecimal baseImponible, BigDecimal baseImpGrav, BigDecimal montoIva,
            Compensaciones compensaciones, BigDecimal montoIce, BigDecimal valorRetIva, BigDecimal valorRetRenta, FormasPagoModel formasDePago) {
        this.tpIdCliente = tpIdCliente;
        this.idCliente = idCliente;
        this.parteRelVtas = parteRelVtas;
        this.tipoComprobante = tipoComprobante;
        this.tipoEmision = tipoEmision;
        this.numeroComprobantes = numeroComprobantes;
        this.baseNoGraIva = baseNoGraIva;
        this.baseImponible = baseImponible;
        this.baseImpGrav = baseImpGrav;
        this.montoIva = montoIva;
        this.compensaciones = compensaciones;
        this.montoIce = montoIce;
        this.valorRetIva = valorRetIva;
        this.valorRetRenta = valorRetRenta;
        this.formasDePago = formasDePago;
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public String getTpIdCliente() {
        return tpIdCliente;
    }

    public void setTpIdCliente(String tpIdCliente) {
        this.tpIdCliente = tpIdCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getParteRelVtas() {
        return parteRelVtas;
    }

    public void setParteRelVtas(String parteRelVtas) {
        this.parteRelVtas = parteRelVtas;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public BigInteger getNumeroComprobantes() {
        return numeroComprobantes;
    }

    public void setNumeroComprobantes(BigInteger numeroComprobantes) {
        this.numeroComprobantes = numeroComprobantes;
    }

    public Compensaciones getCompensaciones() {
        return compensaciones;
    }

    public void setCompensaciones(Compensaciones compensaciones) {
        this.compensaciones = compensaciones;
    }

    public BigDecimal getBaseNoGraIva() {
        return baseNoGraIva;
    }

    public void setBaseNoGraIva(BigDecimal baseNoGraIva) {
        this.baseNoGraIva = baseNoGraIva;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getBaseImpGrav() {
        return baseImpGrav;
    }

    public void setBaseImpGrav(BigDecimal baseImpGrav) {
        this.baseImpGrav = baseImpGrav;
    }

    public BigDecimal getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(BigDecimal montoIva) {
        this.montoIva = montoIva;
    }

    public BigDecimal getMontoIce() {
        return montoIce;
    }

    public void setMontoIce(BigDecimal montoIce) {
        this.montoIce = montoIce;
    }

    public BigDecimal getValorRetIva() {
        return valorRetIva;
    }

    public void setValorRetIva(BigDecimal valorRetIva) {
        this.valorRetIva = valorRetIva;
    }

    public BigDecimal getValorRetRenta() {
        return valorRetRenta;
    }

    public void setValorRetRenta(BigDecimal valorRetRenta) {
        this.valorRetRenta = valorRetRenta;
    }

    public FormasPagoModel getFormasDePago() {
        return formasDePago;
    }

    public void setFormasDePago(FormasPagoModel formasDePago) {
        this.formasDePago = formasDePago;
    }

    @Override
    public String toString() {
        return "DetalleVentas{" + "tpIdCliente=" + tpIdCliente + ", idCliente=" + idCliente + ", parteRelVtas=" + parteRelVtas + ", tipoComprobante=" + tipoComprobante + ", tipoEmision=" + tipoEmision + ", numeroComprobantes=" + numeroComprobantes + ", baseNoGraIva=" + baseNoGraIva + ", baseImponible=" + baseImponible + ", baseImpGrav=" + baseImpGrav + ", montoIva=" + montoIva + ", compensaciones=" + compensaciones + ", montoIce=" + montoIce + ", valorRetIva=" + valorRetIva + ", valorRetRenta=" + valorRetRenta + ", formasDePago=" + formasDePago + '}';
    }

//</editor-fold>
}
