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
@XmlType(name = "fideicomisos", propOrder = {"detalleFideicomisos"})
@XmlRootElement
public class Fideicomiso {

    @XmlElement(name = "detalleFideicomisos")
    private List<DetalleFideicomisos> detalleFideicomisos;

    public Fideicomiso() {
    }

    public Fideicomiso(List<DetalleFideicomisos> detalleFideicomisos) {
        this.detalleFideicomisos = detalleFideicomisos;
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public List<DetalleFideicomisos> getDetalleFideicomisos() {
        return detalleFideicomisos;
    }

    public void setDetalleFideicomisos(List<DetalleFideicomisos> detalleFideicomisos) {
        this.detalleFideicomisos = detalleFideicomisos;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detalleFideicomisos", propOrder = {"tipoBeneficiario", "idBeneficiario", "parteRelExp", "rucFideicomiso",
        "fvalor"})
    @XmlRootElement
    public class DetalleFideicomisos {

        private String tipoBeneficiario;
        private String idBeneficiario;
        private String parteRelExp;
        private String rucFideicomiso;
        @XmlElement(name = "fvalor")
        private Fvalor fvalor;

        public DetalleFideicomisos() {
        }

        public DetalleFideicomisos(String tipoBeneficiario, String idBeneficiario, String parteRelExp, String rucFideicomiso, Fvalor fvalor) {
            this.tipoBeneficiario = tipoBeneficiario;
            this.idBeneficiario = idBeneficiario;
            this.parteRelExp = parteRelExp;
            this.rucFideicomiso = rucFideicomiso;
            this.fvalor = fvalor;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public String getTipoBeneficiario() {
            return tipoBeneficiario;
        }

        public void setTipoBeneficiario(String tipoBeneficiario) {
            this.tipoBeneficiario = tipoBeneficiario;
        }

        public String getIdBeneficiario() {
            return idBeneficiario;
        }

        public void setIdBeneficiario(String idBeneficiario) {
            this.idBeneficiario = idBeneficiario;
        }

        public String getParteRelExp() {
            return parteRelExp;
        }

        public void setParteRelExp(String parteRelExp) {
            this.parteRelExp = parteRelExp;
        }

        public String getRucFideicomiso() {
            return rucFideicomiso;
        }

        public void setRucFideicomiso(String rucFideicomiso) {
            this.rucFideicomiso = rucFideicomiso;
        }

        public Fvalor getFvalor() {
            return fvalor;
        }

        public void setFvalor(Fvalor fvalor) {
            this.fvalor = fvalor;
        }
//</editor-fold>
    }

}
