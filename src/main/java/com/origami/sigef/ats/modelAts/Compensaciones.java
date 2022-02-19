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
@XmlType(name = "compensaciones", propOrder = {"compensacion"})
@XmlRootElement
public class Compensaciones {

    @XmlElement
    private List<Compensacion> compensacion;

    public Compensaciones() {
    }

    public Compensaciones(List<Compensacion> compensacion) {
        this.compensacion = compensacion;
    }

//<editor-fold defaultstate="collapsed" desc="Get and Set">
    public List<Compensacion> getCompensacion() {
        return compensacion;
    }

    public void setCompensacion(List<Compensacion> compensacion) {
        this.compensacion = compensacion;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "compensacion", propOrder = {"tipoCompe", "monto"})
    @XmlRootElement
    public static class Compensacion {

        private String tipoCompe;
        private Double monto;

        public Compensacion() {
        }

        public Compensacion(String tipoCompe, Double monto) {
            this.tipoCompe = tipoCompe;
            this.monto = monto;
        }

        public String getTipoCompe() {
            return tipoCompe;
        }

        public void setTipoCompe(String tipoCompe) {
            this.tipoCompe = tipoCompe;
        }

        public Double getMonto() {
            return monto;
        }

        public void setMonto(Double monto) {
            this.monto = monto;
        }

    }

}
