/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleCompras", propOrder = {"codSustento", "tpIdProv", "idProv", "tipoComprobante", "parteRel", "fechaRegistro", "establecimiento",
    "puntoEmision", "secuencial", "fechaEmision", "autorizacion", "baseNoGraIva", "baseImponible", "baseImpGrav", "baseImpExe", "montoIce",
    "montoIva", "valRetBien10", "valRetServ20", "valorRetBienes", "valRetServ50", "valorRetServicios", "valRetServ100", "totbasesImpReemb",
    "pagoExterior", "formasDePago", "air", "estabRetencion1", "ptoEmiRetencion1", "secRetencion1", "autRetencion1", "fechaEmiRet1"})
@XmlRootElement
public class DetalleCompras implements Serializable {

    private String codSustento;
    private String tpIdProv = "01"; //RUC 
    private String idProv;
    private String tipoComprobante; //Comprobante 01-factura 05-nota de debito
    private String parteRel = "NO";
    private String fechaRegistro;
    private String establecimiento;
    private String puntoEmision;
    private String secuencial;
    private String fechaEmision;
    private String autorizacion;
    private BigDecimal baseNoGraIva = BigDecimal.ZERO.setScale(2);
    private BigDecimal baseImponible; // 0%
    private BigDecimal baseImpGrav; // diferente del 0%
    private BigDecimal baseImpExe = BigDecimal.ZERO.setScale(2); //no objeto a iva
    private BigDecimal montoIce;
    private BigDecimal montoIva; // valor iva
    private BigDecimal valRetBien10 = BigDecimal.ZERO.setScale(2); //10%
    private BigDecimal valRetServ20 = BigDecimal.ZERO.setScale(2);// 20 %
    private BigDecimal valorRetBienes = BigDecimal.ZERO.setScale(2); // 30%
    private BigDecimal valRetServ50 = BigDecimal.ZERO.setScale(2); // 50%
    private BigDecimal valorRetServicios = BigDecimal.ZERO.setScale(2); // 70%
    private BigDecimal valRetServ100 = BigDecimal.ZERO.setScale(2);//100%
    private BigDecimal totbasesImpReemb;
    private PagoExteriorModel pagoExterior;
    private FormasPagoModel formasDePago;
    private Air air;
    private String estabRetencion1;
    private String ptoEmiRetencion1;
    private String secRetencion1;
    private String autRetencion1;
    private String fechaEmiRet1;
    //private Reembolsos reembolsos;
    @XmlTransient
    private String numFactura;

    public DetalleCompras() {
    }

    public DetalleCompras(String codSustento, String tpIdProv, String idProv,
            String tipoComprobante, String parteRel, String fechaRegistro, String establecimiento,
            String puntoEmision, String secuencial, String fechaEmision, String autorizacion, BigDecimal baseNoGraIva,
            BigDecimal baseImponible, BigDecimal baseImpGrav, BigDecimal baseImpExe, BigDecimal montoIce, BigDecimal montoIva,
            BigDecimal valRetBien10, BigDecimal valRetServ20, BigDecimal valorRetBienes, BigDecimal valRetServ50, BigDecimal valorRetServicios,
            BigDecimal valRetServ100, BigDecimal totbasesImpReemb, PagoExteriorModel pagoExterior, FormasPagoModel formasDePago, Air air,
            String estabRetencion1, String ptoEmiRetencion1, String secRetencion1, String autRetencion1, String fechaEmiRet1) {
        this.codSustento = codSustento;
        this.tpIdProv = tpIdProv;
        this.idProv = idProv;
        this.tipoComprobante = tipoComprobante;
        this.parteRel = parteRel;
        this.fechaRegistro = fechaRegistro;
        this.establecimiento = establecimiento;
        this.puntoEmision = puntoEmision;
        this.secuencial = secuencial;
        this.fechaEmision = fechaEmision;
        this.autorizacion = autorizacion;
        this.baseNoGraIva = baseNoGraIva;
        this.baseImponible = baseImponible;
        this.baseImpGrav = baseImpGrav;
        this.baseImpExe = baseImpExe;
        this.montoIce = montoIce;
        this.montoIva = montoIva;
        this.valRetBien10 = valRetBien10;
        this.valRetServ20 = valRetServ20;
        this.valorRetBienes = valorRetBienes;
        this.valRetServ50 = valRetServ50;
        this.valorRetServicios = valorRetServicios;
        this.valRetServ100 = valRetServ100;
        this.totbasesImpReemb = totbasesImpReemb;
        this.pagoExterior = pagoExterior;
        this.formasDePago = formasDePago;
        this.air = air;
        this.estabRetencion1 = estabRetencion1;
        this.ptoEmiRetencion1 = ptoEmiRetencion1;
        this.secRetencion1 = secRetencion1;
        this.autRetencion1 = autRetencion1;
        this.fechaEmiRet1 = fechaEmiRet1;
        //this.reembolsos = reembolsos;
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
//    public Reembolsos getReembolsos() {
//        return reembolsos;
//    }
//
//    public void setReembolsos(Reembolsos reembolsos) {
//        this.reembolsos = reembolsos;
//    }
    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public String getCodSustento() {
        return codSustento;
    }

    public void setCodSustento(String codSustento) {
        this.codSustento = codSustento;
    }

    public String getTpIdProv() {
        return tpIdProv;
    }

    public void setTpIdProv(String tpIdProv) {
        this.tpIdProv = tpIdProv;
    }

    public String getIdProv() {
        return idProv;
    }

    public void setIdProv(String idProv) {
        this.idProv = idProv;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getParteRel() {
        return parteRel;
    }

    public void setParteRel(String parteRel) {
        this.parteRel = parteRel;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
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

    public BigDecimal getBaseImpExe() {
        return baseImpExe;
    }

    public void setBaseImpExe(BigDecimal baseImpExe) {
        this.baseImpExe = baseImpExe;
    }

    public BigDecimal getMontoIce() {
        return montoIce;
    }

    public void setMontoIce(BigDecimal montoIce) {
        this.montoIce = montoIce;
    }

    public BigDecimal getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(BigDecimal montoIva) {
        this.montoIva = montoIva;
    }

    public BigDecimal getValRetBien10() {
        return valRetBien10;
    }

    public void setValRetBien10(BigDecimal valRetBien10) {
        this.valRetBien10 = valRetBien10;
    }

    public BigDecimal getValRetServ20() {
        return valRetServ20;
    }

    public void setValRetServ20(BigDecimal valRetServ20) {
        this.valRetServ20 = valRetServ20;
    }

    public BigDecimal getValorRetBienes() {
        return valorRetBienes;
    }

    public void setValorRetBienes(BigDecimal valorRetBienes) {
        this.valorRetBienes = valorRetBienes;
    }

    public BigDecimal getValRetServ50() {
        return valRetServ50;
    }

    public void setValRetServ50(BigDecimal valRetServ50) {
        this.valRetServ50 = valRetServ50;
    }

    public BigDecimal getValorRetServicios() {
        return valorRetServicios;
    }

    public void setValorRetServicios(BigDecimal valorRetServicios) {
        this.valorRetServicios = valorRetServicios;
    }

    public BigDecimal getValRetServ100() {
        return valRetServ100;
    }

    public void setValRetServ100(BigDecimal valRetServ100) {
        this.valRetServ100 = valRetServ100;
    }

    public BigDecimal getTotbasesImpReemb() {
        return totbasesImpReemb;
    }

    public void setTotbasesImpReemb(BigDecimal totbasesImpReemb) {
        this.totbasesImpReemb = totbasesImpReemb;
    }

    public PagoExteriorModel getPagoExterior() {
        return pagoExterior;
    }

    public void setPagoExterior(PagoExteriorModel pagoExterior) {
        this.pagoExterior = pagoExterior;
    }

    public FormasPagoModel getFormasDePago() {
        return formasDePago;
    }

    public void setFormasDePago(FormasPagoModel formasDePago) {
        this.formasDePago = formasDePago;
    }

    public Air getAir() {
        return air;
    }

    public void setAir(Air air) {
        this.air = air;
    }

    public String getEstabRetencion1() {
        return estabRetencion1;
    }

    public void setEstabRetencion1(String estabRetencion1) {
        this.estabRetencion1 = estabRetencion1;
    }

    public String getPtoEmiRetencion1() {
        return ptoEmiRetencion1;
    }

    public void setPtoEmiRetencion1(String ptoEmiRetencion1) {
        this.ptoEmiRetencion1 = ptoEmiRetencion1;
    }

    public String getSecRetencion1() {
        return secRetencion1;
    }

    public void setSecRetencion1(String secRetencion1) {
        this.secRetencion1 = secRetencion1;
    }

    public String getAutRetencion1() {
        return autRetencion1;
    }

    public void setAutRetencion1(String autRetencion1) {
        this.autRetencion1 = autRetencion1;
    }

    public String getFechaEmiRet1() {
        return fechaEmiRet1;
    }

    public void setFechaEmiRet1(String fechaEmiRet1) {
        this.fechaEmiRet1 = fechaEmiRet1;
    }

    @Override
    public String toString() {
        return "DetalleCompras{" + "codSustento=" + codSustento + ", tpIdProv=" + tpIdProv
                + ", idProv=" + idProv + ", tipoComprobante=" + tipoComprobante + ", parteRel=" + parteRel
                + ", fechaRegistro=" + fechaRegistro + ", establecimiento=" + establecimiento + ", puntoEmision=" + puntoEmision
                + ", secuencial=" + secuencial + ", fechaEmision=" + fechaEmision + ", autorizacion=" + autorizacion
                + ", baseNoGraIva=" + baseNoGraIva + ", baseImponible=" + baseImponible + ", baseImpGrav=" + baseImpGrav
                + ", baseImpExe=" + baseImpExe + ", montoIce=" + montoIce + ", montoIva=" + montoIva
                + ", valRetBien10=" + valRetBien10 + ", valRetServ20=" + valRetServ20 + ", valorRetBienes=" + valorRetBienes
                + ", valRetServ50=" + valRetServ50 + ", valorRetServicios=" + valorRetServicios + ", valRetServ100=" + valRetServ100
                + ", totbasesImpReemb=" + totbasesImpReemb + ", pagoExterior=" + pagoExterior + ", formasDePago=" + formasDePago
                + ", air=" + air + ", estabRetencion1=" + estabRetencion1 + ", ptoEmiRetencion1=" + ptoEmiRetencion1
                + ", secRetencion1=" + secRetencion1 + ", autRetencion1=" + autRetencion1 + ", fechaEmiRet1=" + fechaEmiRet1 + ", numFactura=" + numFactura + '}';
    }

//</editor-fold>
}
