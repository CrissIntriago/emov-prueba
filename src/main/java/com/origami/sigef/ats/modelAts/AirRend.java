/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.io.Serializable;
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
@XmlType(name = "airRend", propOrder = {"detalleAirRen"})
@XmlRootElement
public class AirRend implements Serializable {

    @XmlElement(name = "detalleAirRen")
    private List<DetalleAirRen> detalleAirRen;

    public AirRend() {
    }

    public AirRend(List<DetalleAirRen> detalleAirRen) {
        this.detalleAirRen = detalleAirRen;
    }

//<editor-fold defaultstate="collapsed" desc="Getter And Setter">
    public List<DetalleAirRen> getDetalleAirRen() {
        return detalleAirRen;
    }

    public void setDetalleAirRen(List<DetalleAirRen> detalleAirRen) {
        this.detalleAirRen = detalleAirRen;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detalleAirRen", propOrder = {"codRetAir", "deposito", "baseImpAir", "porcentajeAir", "valRetAir"})
    @XmlRootElement
    public class DetalleAirRen implements Serializable {

        private String codRetAir;
        private Double deposito;
        private Double baseImpAir;
        private Integer porcentajeAir;
        private Double valRetAir;

        public DetalleAirRen() {
        }

        public DetalleAirRen(String codRetAir, Double deposito, Double baseImpAir, Integer porcentajeAir, Double valRetAir) {
            this.codRetAir = codRetAir;
            this.deposito = deposito;
            this.baseImpAir = baseImpAir;
            this.porcentajeAir = porcentajeAir;
            this.valRetAir = valRetAir;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public String getCodRetAir() {
            return codRetAir;
        }

        public void setCodRetAir(String codRetAir) {
            this.codRetAir = codRetAir;
        }

        public Double getDeposito() {
            return deposito;
        }

        public void setDeposito(Double deposito) {
            this.deposito = deposito;
        }

        public Double getBaseImpAir() {
            return baseImpAir;
        }

        public void setBaseImpAir(Double baseImpAir) {
            this.baseImpAir = baseImpAir;
        }

        public Integer getPorcentajeAir() {
            return porcentajeAir;
        }

        public void setPorcentajeAir(Integer porcentajeAir) {
            this.porcentajeAir = porcentajeAir;
        }

        public Double getValRetAir() {
            return valRetAir;
        }

        public void setValRetAir(Double valRetAir) {
            this.valRetAir = valRetAir;
        }
//</editor-fold>
    }
}
