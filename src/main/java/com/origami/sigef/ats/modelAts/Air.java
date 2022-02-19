/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@XmlType(name = "air", propOrder = {"detalleAir"})
@XmlRootElement
public class Air implements Serializable {

    @XmlElement
    private List<DetalleAir> detalleAir;

    public Air() {
        detalleAir = new ArrayList<>();
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public List<DetalleAir> getDetalleAir() {
        return detalleAir;
    }

    public void setDetalleAir(List<DetalleAir> detalleAir) {
        this.detalleAir = detalleAir;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detalleAir", propOrder = {"codRetAir", "baseImpAir", "porcentajeAir", "valRetAir"})
    @XmlRootElement
    public static class DetalleAir implements Serializable {

        private String codRetAir;
        private BigDecimal baseImpAir;
        private BigDecimal porcentajeAir;
        private BigDecimal valRetAir;

        public DetalleAir() {
        }
        
        public DetalleAir(String codRetAir) {
            this.codRetAir = codRetAir;
            this.baseImpAir = BigDecimal.ZERO;
            this.porcentajeAir = BigDecimal.ZERO;
            this.valRetAir = BigDecimal.ZERO;
        }

        public DetalleAir(String codRetAir, BigDecimal baseImpAir, BigDecimal porcentajeAir, BigDecimal valRetAir) {
            this.codRetAir = codRetAir;
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

        public BigDecimal getBaseImpAir() {
            return baseImpAir;
        }

        public void setBaseImpAir(BigDecimal baseImpAir) {
            this.baseImpAir = baseImpAir;
        }

        public BigDecimal getPorcentajeAir() {
            return porcentajeAir;
        }

        public void setPorcentajeAir(BigDecimal porcentajeAir) {
            this.porcentajeAir = porcentajeAir;
        }

        public BigDecimal getValRetAir() {
            return valRetAir;
        }

        public void setValRetAir(BigDecimal valRetAir) {
            this.valRetAir = valRetAir;
        }
//</editor-fold>
    }

}
