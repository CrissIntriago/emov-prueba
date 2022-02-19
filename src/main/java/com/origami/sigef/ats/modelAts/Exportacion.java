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
@XmlType(name = "exportaciones", propOrder = {"detalleExportaciones"})
@XmlRootElement
public class Exportacion {

    @XmlElement(name = "detalleExportaciones")
    private List<DetalleExportaciones> detalleExportaciones;

    public Exportacion() {
    }

    public Exportacion(List<DetalleExportaciones> detalleExportaciones) {
        this.detalleExportaciones = detalleExportaciones;
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<DetalleExportaciones> getDetalleExportaciones() {
        return detalleExportaciones;
    }

    public void setDetalleExportaciones(List<DetalleExportaciones> detalleExportaciones) {
        this.detalleExportaciones = detalleExportaciones;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detalleExportaciones", propOrder = {"tpIdClienteEx", "idClienteEx", "parteRelExp", "tipoRegi", "paisEfecPagoParFis",
        "paisEfecExp", "exportacionDe", "tipIngExt", "ingExtGravOtroPais", "tipoComprobante", "fechaEmbarque", "valorFOB",
        "valorFOBComprobante", "establecimiento", "puntoEmision", "secuencial", "autorizacion", "fechaEmision"})
    @XmlRootElement
    public class DetalleExportaciones {

        private String tpIdClienteEx;
        private String idClienteEx;
        private String parteRelExp;
        private String tipoRegi;
        private String paisEfecPagoParFis;
        private String paisEfecExp;
        private String exportacionDe;
        private String tipIngExt;
        private String ingExtGravOtroPais;
        private String tipoComprobante;
        private String fechaEmbarque;
        private Double valorFOB;
        private Double valorFOBComprobante;
        private String establecimiento;
        private String puntoEmision;
        private Integer secuencial;
        private Integer autorizacion;
        private String fechaEmision;

        public DetalleExportaciones() {
        }

        public DetalleExportaciones(String tpIdClienteEx, String idClienteEx, String parteRelExp,
                String tipoRegi, String paisEfecPagoParFis, String paisEfecExp, String exportacionDe,
                String tipIngExt, String ingExtGravOtroPais, String tipoComprobante, String fechaEmbarque,
                Double valorFOB, Double valorFOBComprobante, String establecimiento, String puntoEmision,
                Integer secuencial, Integer autorizacion, String fechaEmision) {
            this.tpIdClienteEx = tpIdClienteEx;
            this.idClienteEx = idClienteEx;
            this.parteRelExp = parteRelExp;
            this.tipoRegi = tipoRegi;
            this.paisEfecPagoParFis = paisEfecPagoParFis;
            this.paisEfecExp = paisEfecExp;
            this.exportacionDe = exportacionDe;
            this.tipIngExt = tipIngExt;
            this.ingExtGravOtroPais = ingExtGravOtroPais;
            this.tipoComprobante = tipoComprobante;
            this.fechaEmbarque = fechaEmbarque;
            this.valorFOB = valorFOB;
            this.valorFOBComprobante = valorFOBComprobante;
            this.establecimiento = establecimiento;
            this.puntoEmision = puntoEmision;
            this.secuencial = secuencial;
            this.autorizacion = autorizacion;
            this.fechaEmision = fechaEmision;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public String getTpIdClienteEx() {
            return tpIdClienteEx;
        }

        public void setTpIdClienteEx(String tpIdClienteEx) {
            this.tpIdClienteEx = tpIdClienteEx;
        }

        public String getIdClienteEx() {
            return idClienteEx;
        }

        public void setIdClienteEx(String idClienteEx) {
            this.idClienteEx = idClienteEx;
        }

        public String getParteRelExp() {
            return parteRelExp;
        }

        public void setParteRelExp(String parteRelExp) {
            this.parteRelExp = parteRelExp;
        }

        public String getTipoRegi() {
            return tipoRegi;
        }

        public void setTipoRegi(String tipoRegi) {
            this.tipoRegi = tipoRegi;
        }

        public String getPaisEfecPagoParFis() {
            return paisEfecPagoParFis;
        }

        public void setPaisEfecPagoParFis(String paisEfecPagoParFis) {
            this.paisEfecPagoParFis = paisEfecPagoParFis;
        }

        public String getPaisEfecExp() {
            return paisEfecExp;
        }

        public void setPaisEfecExp(String paisEfecExp) {
            this.paisEfecExp = paisEfecExp;
        }

        public String getExportacionDe() {
            return exportacionDe;
        }

        public void setExportacionDe(String exportacionDe) {
            this.exportacionDe = exportacionDe;
        }

        public String getTipIngExt() {
            return tipIngExt;
        }

        public void setTipIngExt(String tipIngExt) {
            this.tipIngExt = tipIngExt;
        }

        public String getIngExtGravOtroPais() {
            return ingExtGravOtroPais;
        }

        public void setIngExtGravOtroPais(String ingExtGravOtroPais) {
            this.ingExtGravOtroPais = ingExtGravOtroPais;
        }

        public String getTipoComprobante() {
            return tipoComprobante;
        }

        public void setTipoComprobante(String tipoComprobante) {
            this.tipoComprobante = tipoComprobante;
        }

        public String getFechaEmbarque() {
            return fechaEmbarque;
        }

        public void setFechaEmbarque(String fechaEmbarque) {
            this.fechaEmbarque = fechaEmbarque;
        }

        public Double getValorFOB() {
            return valorFOB;
        }

        public void setValorFOB(Double valorFOB) {
            this.valorFOB = valorFOB;
        }

        public Double getValorFOBComprobante() {
            return valorFOBComprobante;
        }

        public void setValorFOBComprobante(Double valorFOBComprobante) {
            this.valorFOBComprobante = valorFOBComprobante;
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

        public Integer getSecuencial() {
            return secuencial;
        }

        public void setSecuencial(Integer secuencial) {
            this.secuencial = secuencial;
        }

        public Integer getAutorizacion() {
            return autorizacion;
        }

        public void setAutorizacion(Integer autorizacion) {
            this.autorizacion = autorizacion;
        }

        public String getFechaEmision() {
            return fechaEmision;
        }

        public void setFechaEmision(String fechaEmision) {
            this.fechaEmision = fechaEmision;
        }
//</editor-fold>
    }
}
