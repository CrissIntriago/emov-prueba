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
@XmlType(name = "fValor", propOrder = {"detallefValor"})
@XmlRootElement
public class Fvalor {

    private List<DetallefValor> detallefValor;

    public Fvalor() {
    }

    public Fvalor(List<DetallefValor> detallefValor) {
        this.detallefValor = detallefValor;
    }

//<editor-fold defaultstate="collapsed" desc="Getter and setter">
    public List<DetallefValor> getDetallefValor() {
        return detallefValor;
    }
    
    public void setDetallefValor(List<DetallefValor> detallefValor) {
        this.detallefValor = detallefValor;
    }
//</editor-fold>
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detallefValor", propOrder = {"tipoFideicomiso", "totalF", "individualF", "porRetF", "valorRetF", "fechaPagoDiv",
        "imRentaSoc", "anioUtDiv", "pagoExterior"})
    @XmlRootElement
    public class DetallefValor {

        private String tipoFideicomiso;
        private Double totalF;
        private Double individualF;
        private Integer porRetF;
        private Double valorRetF;
        private String fechaPagoDiv;
        private Double imRentaSoc;
        private Integer anioUtDiv;
        private PagoExteriorModel pagoExterior;

        public DetallefValor() {
        }

        public DetallefValor(String tipoFideicomiso, Double totalF, Double individualF, Integer porRetF, Double valorRetF, String fechaPagoDiv, Double imRentaSoc, Integer anioUtDiv, PagoExteriorModel pagoExterior) {
            this.tipoFideicomiso = tipoFideicomiso;
            this.totalF = totalF;
            this.individualF = individualF;
            this.porRetF = porRetF;
            this.valorRetF = valorRetF;
            this.fechaPagoDiv = fechaPagoDiv;
            this.imRentaSoc = imRentaSoc;
            this.anioUtDiv = anioUtDiv;
            this.pagoExterior = pagoExterior;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public String getTipoFideicomiso() {
            return tipoFideicomiso;
        }

        public void setTipoFideicomiso(String tipoFideicomiso) {
            this.tipoFideicomiso = tipoFideicomiso;
        }

        public Double getTotalF() {
            return totalF;
        }

        public void setTotalF(Double totalF) {
            this.totalF = totalF;
        }

        public Double getIndividualF() {
            return individualF;
        }

        public void setIndividualF(Double individualF) {
            this.individualF = individualF;
        }

        public Integer getPorRetF() {
            return porRetF;
        }

        public void setPorRetF(Integer porRetF) {
            this.porRetF = porRetF;
        }

        public Double getValorRetF() {
            return valorRetF;
        }

        public void setValorRetF(Double valorRetF) {
            this.valorRetF = valorRetF;
        }

        public String getFechaPagoDiv() {
            return fechaPagoDiv;
        }

        public void setFechaPagoDiv(String fechaPagoDiv) {
            this.fechaPagoDiv = fechaPagoDiv;
        }

        public Double getImRentaSoc() {
            return imRentaSoc;
        }

        public void setImRentaSoc(Double imRentaSoc) {
            this.imRentaSoc = imRentaSoc;
        }

        public Integer getAnioUtDiv() {
            return anioUtDiv;
        }

        public void setAnioUtDiv(Integer anioUtDiv) {
            this.anioUtDiv = anioUtDiv;
        }

        public PagoExteriorModel getPagoExterior() {
            return pagoExterior;
        }

        public void setPagoExterior(PagoExteriorModel pagoExterior) {
            this.pagoExterior = pagoExterior;
        }
//</editor-fold>
    }

}
