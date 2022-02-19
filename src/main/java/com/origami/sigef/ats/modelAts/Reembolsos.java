/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reembolsos", propOrder = {"reembolso"})
@XmlRootElement
public class Reembolsos {

    private List<Reembolso> reembolso;

    public Reembolsos() {
    }

    public Reembolsos(List<Reembolso> reembolso) {
        this.reembolso = reembolso;
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public List<Reembolso> getReembolso() {
        return reembolso;
    }

    public void setReembolso(List<Reembolso> reembolso) {
        this.reembolso = reembolso;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "reembolso", propOrder = {"tipoComprobanteReemb", "tpIdProvReemb", "idProvReemb", "establecimientoReemb",
        "puntoEmisionReemb", "secuencialReemb", "fechaEmisionReemb", "autorizacionReemb", "baseImponibleReemb", "baseImpGravReemb",
        "baseNoGraIvaReemb", "baseImpExeReemb", "montoIceRemb", "montoIvaRemb"})
    @XmlRootElement
    public class Reembolso {

        private String tipoComprobanteReemb;
        private String tpIdProvReemb;
        private String idProvReemb;
        private String establecimientoReemb;
        private String puntoEmisionReemb;
        private Integer secuencialReemb;
        private String fechaEmisionReemb;
        private Integer autorizacionReemb;
        private Double baseImponibleReemb;
        private Double baseImpGravReemb;
        private Double baseNoGraIvaReemb;
        private Double baseImpExeReemb;
        private Double montoIceRemb;
        private Double montoIvaRemb;

        public Reembolso() {
        }

        public Reembolso(String tipoComprobanteReemb, String tpIdProvReemb,
                String idProvReemb, String establecimientoReemb,
                String puntoEmisionReemb, Integer secuencialReemb,
                String fechaEmisionReemb, Integer autorizacionReemb, Double baseImponibleReemb,
                Double baseImpGravReemb, Double baseNoGraIvaReemb, Double baseImpExeReemb, Double montoIceRemb, Double montoIvaRemb) {
            this.tipoComprobanteReemb = tipoComprobanteReemb;
            this.tpIdProvReemb = tpIdProvReemb;
            this.idProvReemb = idProvReemb;
            this.establecimientoReemb = establecimientoReemb;
            this.puntoEmisionReemb = puntoEmisionReemb;
            this.secuencialReemb = secuencialReemb;
            this.fechaEmisionReemb = fechaEmisionReemb;
            this.autorizacionReemb = autorizacionReemb;
            this.baseImponibleReemb = baseImponibleReemb;
            this.baseImpGravReemb = baseImpGravReemb;
            this.baseNoGraIvaReemb = baseNoGraIvaReemb;
            this.baseImpExeReemb = baseImpExeReemb;
            this.montoIceRemb = montoIceRemb;
            this.montoIvaRemb = montoIvaRemb;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public String getTipoComprobanteReemb() {
            return tipoComprobanteReemb;
        }

        public void setTipoComprobanteReemb(String tipoComprobanteReemb) {
            this.tipoComprobanteReemb = tipoComprobanteReemb;
        }

        public String getTpIdProvReemb() {
            return tpIdProvReemb;
        }

        public void setTpIdProvReemb(String tpIdProvReemb) {
            this.tpIdProvReemb = tpIdProvReemb;
        }

        public String getIdProvReemb() {
            return idProvReemb;
        }

        public void setIdProvReemb(String idProvReemb) {
            this.idProvReemb = idProvReemb;
        }

        public String getEstablecimientoReemb() {
            return establecimientoReemb;
        }

        public void setEstablecimientoReemb(String establecimientoReemb) {
            this.establecimientoReemb = establecimientoReemb;
        }

        public String getPuntoEmisionReemb() {
            return puntoEmisionReemb;
        }

        public void setPuntoEmisionReemb(String puntoEmisionReemb) {
            this.puntoEmisionReemb = puntoEmisionReemb;
        }

        public Integer getSecuencialReemb() {
            return secuencialReemb;
        }

        public void setSecuencialReemb(Integer secuencialReemb) {
            this.secuencialReemb = secuencialReemb;
        }

        public String getFechaEmisionReemb() {
            return fechaEmisionReemb;
        }

        public void setFechaEmisionReemb(String fechaEmisionReemb) {
            this.fechaEmisionReemb = fechaEmisionReemb;
        }

        public Integer getAutorizacionReemb() {
            return autorizacionReemb;
        }

        public void setAutorizacionReemb(Integer autorizacionReemb) {
            this.autorizacionReemb = autorizacionReemb;
        }

        public Double getBaseImponibleReemb() {
            return baseImponibleReemb;
        }

        public void setBaseImponibleReemb(Double baseImponibleReemb) {
            this.baseImponibleReemb = baseImponibleReemb;
        }

        public Double getBaseImpGravReemb() {
            return baseImpGravReemb;
        }

        public void setBaseImpGravReemb(Double baseImpGravReemb) {
            this.baseImpGravReemb = baseImpGravReemb;
        }

        public Double getBaseNoGraIvaReemb() {
            return baseNoGraIvaReemb;
        }

        public void setBaseNoGraIvaReemb(Double baseNoGraIvaReemb) {
            this.baseNoGraIvaReemb = baseNoGraIvaReemb;
        }

        public Double getBaseImpExeReemb() {
            return baseImpExeReemb;
        }

        public void setBaseImpExeReemb(Double baseImpExeReemb) {
            this.baseImpExeReemb = baseImpExeReemb;
        }

        public Double getMontoIceRemb() {
            return montoIceRemb;
        }

        public void setMontoIceRemb(Double montoIceRemb) {
            this.montoIceRemb = montoIceRemb;
        }

        public Double getMontoIvaRemb() {
            return montoIvaRemb;
        }

        public void setMontoIvaRemb(Double montoIvaRemb) {
            this.montoIvaRemb = montoIvaRemb;
        }
//</editor-fold>
    }
}
