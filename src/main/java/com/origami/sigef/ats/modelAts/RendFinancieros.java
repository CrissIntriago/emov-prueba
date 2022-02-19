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
@XmlType(name = "rendFinancieros", propOrder = {"detalleRendFinancieros"})
@XmlRootElement
public class RendFinancieros {

    @XmlElement(name = "detalleRendFinancieros")
    private List<DetalleRendFinancieros> detalleRendFinancieros;

    public RendFinancieros() {
    }

    public RendFinancieros(List<DetalleRendFinancieros> detalleRendFinancieros) {
        this.detalleRendFinancieros = detalleRendFinancieros;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setters">
    public List<DetalleRendFinancieros> getDetalleRendFinancieros() {
        return detalleRendFinancieros;
    }

    public void setDetalleRendFinancieros(List<DetalleRendFinancieros> detalleRendFinancieros) {
        this.detalleRendFinancieros = detalleRendFinancieros;
    }

//</editor-fold>
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detalleRendFinancieros", propOrder = {"retenido", "idRetenido", "parteRelFid", "ahorroPN", "ctaExenta", "retenciones"})
    @XmlRootElement
    public class DetalleRendFinancieros {

        private String retenido;
        private String idRetenido;
        private String parteRelFid;
        @XmlElement(name = "ahorroPN")
        private AhorroPN ahorroPN;
        @XmlElement(name = "ctaExenta")
        private CtaExenta ctaExenta;
        @XmlElement(name = "retenciones")
        private Retenciones retenciones;

        public DetalleRendFinancieros() {
        }

        public DetalleRendFinancieros(String retenido, String idRetenido, String parteRelFid, AhorroPN ahorroPN, CtaExenta ctaExenta,
                Retenciones retenciones) {
            this.retenido = retenido;
            this.idRetenido = idRetenido;
            this.parteRelFid = parteRelFid;
            this.ahorroPN = ahorroPN;
            this.ctaExenta = ctaExenta;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public Retenciones getRetenciones() {
            return retenciones;
        }

        public void setRetenciones(Retenciones retenciones) {
            this.retenciones = retenciones;
        }

        public String getRetenido() {
            return retenido;
        }

        public void setRetenido(String retenido) {
            this.retenido = retenido;
        }

        public String getIdRetenido() {
            return idRetenido;
        }

        public void setIdRetenido(String idRetenido) {
            this.idRetenido = idRetenido;
        }

        public String getParteRelFid() {
            return parteRelFid;
        }

        public void setParteRelFid(String parteRelFid) {
            this.parteRelFid = parteRelFid;
        }

        public AhorroPN getAhorroPN() {
            return ahorroPN;
        }

        public void setAhorroPN(AhorroPN ahorroPN) {
            this.ahorroPN = ahorroPN;
        }

        public CtaExenta getCtaExenta() {
            return ctaExenta;
        }

        public void setCtaExenta(CtaExenta ctaExenta) {
            this.ctaExenta = ctaExenta;
        }
//</editor-fold>

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "ahorroPN", propOrder = {"totalDep", "rendGen"})
        @XmlRootElement
        public class AhorroPN {

            private Double totalDep;
            private Double rendGen;

            public AhorroPN() {
            }

            public AhorroPN(Double totalDep, Double rendGen) {
                this.totalDep = totalDep;
                this.rendGen = rendGen;
            }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
            public Double getTotalDep() {
                return totalDep;
            }

            public void setTotalDep(Double totalDep) {
                this.totalDep = totalDep;
            }

            public Double getRendGen() {
                return rendGen;
            }

            public void setRendGen(Double rendGen) {
                this.rendGen = rendGen;
            }
//</editor-fold>
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "ctaExenta", propOrder = {"totalDep", "rendGen"})
        @XmlRootElement
        public class CtaExenta {

            private Double totalDep;
            private Double rendGen;

            public CtaExenta() {
            }

            public CtaExenta(Double totalDep, Double rendGen) {
                this.totalDep = totalDep;
                this.rendGen = rendGen;
            }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
            public Double getTotalDep() {
                return totalDep;
            }

            public void setTotalDep(Double totalDep) {
                this.totalDep = totalDep;
            }

            public Double getRendGen() {
                return rendGen;
            }

            public void setRendGen(Double rendGen) {
                this.rendGen = rendGen;
            }
//</editor-fold>
        }
    }
}
