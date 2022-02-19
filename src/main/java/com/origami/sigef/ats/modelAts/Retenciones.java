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
@XmlType(name = "retenciones", propOrder = {"detRet"})
@XmlRootElement
public class Retenciones {

    @XmlElement(name = "detRet")
    private List<DetRet> detRet;

    public Retenciones() {
    }

    public Retenciones(List<DetRet> detRet) {
        this.detRet = detRet;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<DetRet> getDetRet() {
        return detRet;
    }

    public void setDetRet(List<DetRet> detRet) {
        this.detRet = detRet;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detRet", propOrder = {"pagoExterior", "estabRetencion", "ptoEmiRetencion", "secRetencion", "autRetencion",
        "fechaEmiRet", "airRend"})
    @XmlRootElement
    public class DetRet {

        @XmlElement(name = "pagoExterior")
        private PagoExteriorModel pagoExterior;
        private String estabRetencion;
        private String ptoEmiRetencion;
        private Integer secRetencion;
        private Integer autRetencion;
        private String fechaEmiRet;
        @XmlElement(name = "airRend")
        private AirRend airRend;

        public DetRet() {
        }

        public DetRet(PagoExteriorModel pagoExterior, String estabRetencion, String ptoEmiRetencion, Integer secRetencion, Integer autRetencion, String fechaEmiRet, AirRend airRend) {
            this.pagoExterior = pagoExterior;
            this.estabRetencion = estabRetencion;
            this.ptoEmiRetencion = ptoEmiRetencion;
            this.secRetencion = secRetencion;
            this.autRetencion = autRetencion;
            this.fechaEmiRet = fechaEmiRet;
            this.airRend = airRend;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public PagoExteriorModel getPagoExterior() {
            return pagoExterior;
        }

        public void setPagoExterior(PagoExteriorModel pagoExterior) {
            this.pagoExterior = pagoExterior;
        }

        public String getEstabRetencion() {
            return estabRetencion;
        }

        public void setEstabRetencion(String estabRetencion) {
            this.estabRetencion = estabRetencion;
        }

        public String getPtoEmiRetencion() {
            return ptoEmiRetencion;
        }

        public void setPtoEmiRetencion(String ptoEmiRetencion) {
            this.ptoEmiRetencion = ptoEmiRetencion;
        }

        public Integer getSecRetencion() {
            return secRetencion;
        }

        public void setSecRetencion(Integer secRetencion) {
            this.secRetencion = secRetencion;
        }

        public Integer getAutRetencion() {
            return autRetencion;
        }

        public void setAutRetencion(Integer autRetencion) {
            this.autRetencion = autRetencion;
        }

        public String getFechaEmiRet() {
            return fechaEmiRet;
        }

        public void setFechaEmiRet(String fechaEmiRet) {
            this.fechaEmiRet = fechaEmiRet;
        }

        public AirRend getAirRend() {
            return airRend;
        }

        public void setAirRend(AirRend airRend) {
            this.airRend = airRend;
        }
//</editor-fold>

    }
}
