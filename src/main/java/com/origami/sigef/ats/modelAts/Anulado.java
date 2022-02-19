/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anulados", propOrder = {"detalleAnulados"})
@XmlRootElement
public class Anulado {

    @XmlElement(name = "detalleAnulados")
    private List<DetalleAnulados> detalleAnulados;

    public Anulado() {
    }

    public Anulado(List<DetalleAnulados> detalleAnulados) {
        this.detalleAnulados = detalleAnulados;
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<DetalleAnulados> getDetalleAnulados() {
        return detalleAnulados;
    }

    public void setDetalleAnulados(List<DetalleAnulados> detalleAnulados) {
        this.detalleAnulados = detalleAnulados;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detalleAnulados", propOrder = {"tipoComprobante", "establecimiento", "puntoEmision", "secuencialInicio", "secuencialFin",
        "autorizacion"})
    @XmlRootElement
    public class DetalleAnulados {

        private String tipoComprobante;
        private String establecimiento;
        private String puntoEmision;
        private Integer secuencialInicio;
        private Integer secuencialFin;
        private Integer autorizacion;

        public DetalleAnulados() {
        }

        public DetalleAnulados(String tipoComprobante, String establecimiento, String puntoEmision, Integer secuencialInicio, Integer secuencialFin, Integer autorizacion) {
            this.tipoComprobante = tipoComprobante;
            this.establecimiento = establecimiento;
            this.puntoEmision = puntoEmision;
            this.secuencialInicio = secuencialInicio;
            this.secuencialFin = secuencialFin;
            this.autorizacion = autorizacion;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public String getTipoComprobante() {
            return tipoComprobante;
        }

        public void setTipoComprobante(String tipoComprobante) {
            this.tipoComprobante = tipoComprobante;
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

        public Integer getSecuencialInicio() {
            return secuencialInicio;
        }

        public void setSecuencialInicio(Integer secuencialInicio) {
            this.secuencialInicio = secuencialInicio;
        }

        public Integer getSecuencialFin() {
            return secuencialFin;
        }

        public void setSecuencialFin(Integer secuencialFin) {
            this.secuencialFin = secuencialFin;
        }

        public Integer getAutorizacion() {
            return autorizacion;
        }

        public void setAutorizacion(Integer autorizacion) {
            this.autorizacion = autorizacion;
        }
//</editor-fold>
    }
}
