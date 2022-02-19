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
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iva", propOrder = {"TipoIDInformante", "IdInformante", "razonSocial", "Anio",
    "Mes", "numEstabRuc", "totalVentas", "codigoOperativo", "compras", "ventas", "ventasEstablecimiento"})
//    "exportaciones", "recap", "fideicomisos", "anulados", "rendFinancieros"})
@XmlRootElement
public class IVA implements Serializable {

    private String TipoIDInformante;
    private String IdInformante;
    private String razonSocial;
    private Integer Anio;
    private String Mes;
    private String numEstabRuc;
    private BigDecimal totalVentas;
    private String codigoOperativo;
    private Compras compras;
    private Ventas ventas;
    private VentaEstablecimiento ventasEstablecimiento;
//    @XmlElement(name = "ventasEstablecimiento")
//    private VentaEstablecimiento ventasEstablecimiento;
//    @XmlElement(name = "exportaciones")
//    private Exportacion exportaciones;
//    @XmlElement(name = "recap")
//    private Recap recap;
//    @XmlElement(name = "fideicomisos")
//    private Fideicomiso fideicomisos;
//    @XmlElement(name = "anulados")
//    private Anulado anulados;
//    @XmlElement(name = "rendFinancieros")
//    private RendFinancieros rendFinancieros;

    public IVA() {
    }

    public IVA(String TipoIDInformante, String IdInformante, String razonSocial, Integer Anio, String Mes,
            String numEstabRuc, BigDecimal totalVentas, String codigoOperativo) {
        this.TipoIDInformante = TipoIDInformante;
        this.IdInformante = IdInformante;
        this.razonSocial = razonSocial;
        this.Anio = Anio;
        this.Mes = Mes;
        this.numEstabRuc = numEstabRuc;
        this.totalVentas = totalVentas;
        this.codigoOperativo = codigoOperativo;
    }

    public IVA(String TipoIDInformante, String IdInformante,
            String razonSocial, Integer Anio, String Mes, String numEstabRuc,
            BigDecimal totalVentas, String codigoOperativo, Compras compras, Ventas ventas) {
//            
//            VentaEstablecimiento ventasEstablecimiento, Exportacion exportaciones,
//            Recap recap, Fideicomiso fideicomisos, Anulado anulados, RendFinancieros rendFinancieros) 

        this.TipoIDInformante = TipoIDInformante;
        this.IdInformante = IdInformante;
        this.razonSocial = razonSocial;
        this.Anio = Anio;
        this.Mes = Mes;
        this.numEstabRuc = numEstabRuc;
        this.totalVentas = totalVentas;
        this.codigoOperativo = codigoOperativo;
        this.compras = compras;
        this.ventas = ventas;
//        this.ventasEstablecimiento = ventasEstablecimiento;
//        this.exportaciones = exportaciones;
//        this.recap = recap;
//        this.fideicomisos = fideicomisos;
//        this.anulados = anulados;
//        this.rendFinancieros = rendFinancieros;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getTipoIDInformante() {
        return TipoIDInformante;
    }

    public void setTipoIDInformante(String TipoIDInformante) {
        this.TipoIDInformante = TipoIDInformante;
    }

    public String getIdInformante() {
        return IdInformante;
    }

    public void setIdInformante(String IdInformante) {
        this.IdInformante = IdInformante;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Integer getAnio() {
        return Anio;
    }

    public void setAnio(Integer Anio) {
        this.Anio = Anio;
    }

    public String getMes() {
        return Mes;
    }

    public void setMes(String Mes) {
        this.Mes = Mes;
    }

    public String getNumEstabRuc() {
        return numEstabRuc;
    }

    public void setNumEstabRuc(String numEstabRuc) {
        this.numEstabRuc = numEstabRuc;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    public String getCodigoOperativo() {
        return codigoOperativo;
    }

    public void setCodigoOperativo(String codigoOperativo) {
        this.codigoOperativo = codigoOperativo;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }

    public Ventas getVentas() {
        return ventas;
    }

    public void setVentas(Ventas ventas) {
        this.ventas = ventas;
    }

    public VentaEstablecimiento getVentasEstablecimiento() {
        return ventasEstablecimiento;
    }

    public void setVentasEstablecimiento(VentaEstablecimiento ventasEstablecimiento) {
        this.ventasEstablecimiento = ventasEstablecimiento;
    }
//
//    public Exportacion getExportaciones() {
//        return exportaciones;
//    }
//
//    public void setExportaciones(Exportacion exportaciones) {
//        this.exportaciones = exportaciones;
//    }
//
//    public Recap getRecap() {
//        return recap;
//    }
//
//    public void setRecap(Recap recap) {
//        this.recap = recap;
//    }
//
//    public Fideicomiso getFideicomisos() {
//        return fideicomisos;
//    }
//
//    public void setFideicomisos(Fideicomiso fideicomisos) {
//        this.fideicomisos = fideicomisos;
//    }
//
//    public Anulado getAnulados() {
//        return anulados;
//    }
//
//    public void setAnulados(Anulado anulados) {
//        this.anulados = anulados;
//    }
//
//    public RendFinancieros getRendFinancieros() {
//        return rendFinancieros;
//    }
//
//    public void setRendFinancieros(RendFinancieros rendFinancieros) {
//        this.rendFinancieros = rendFinancieros;
//    }
    @Override
    public String toString() {
        return "ATS{" + "TipoIDInformante=" + TipoIDInformante
                + ", IdInformante=" + IdInformante + ", razonSocial=" + razonSocial
                + ", Anio=" + Anio + ", Mes=" + Mes + ", numEstabRuc=" + numEstabRuc + ", totalVentas=" + totalVentas
                + ", codigoOperativo=" + codigoOperativo + ", compras=" + compras + ", ventas=" + ventas + "}";
//                + " ventasEstablecimiento=" + ventasEstablecimiento
//                + ", exportaciones=" + exportaciones + ", recap=" + recap + ", fideicomisos=" + fideicomisos + ", anulados=" + anulados
//                + ", rendFinancieros=" + rendFinancieros + '}';
    }

//</editor-fold>
}
