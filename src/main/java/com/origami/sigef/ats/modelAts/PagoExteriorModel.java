/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagoExterior", propOrder = {"pagoLocExt", "tipoRegi", "denopagoRegFis",
    "paisEfecPagoGen", "paisEfecPagoParFis", "denopago", "paisEfecPago", "aplicConvDobTrib", "pagExtSujRetNorLeg"})
@XmlRootElement
public class PagoExteriorModel implements Serializable{

    private String pagoLocExt;
    private String tipoRegi;
    private String denopagoRegFis;
    private String paisEfecPagoGen;
    private String paisEfecPagoParFis;
    private String paisEfecPago = "NA";
    private String aplicConvDobTrib = "NA";
    private String pagExtSujRetNorLeg = "NA";
    private String denopago;

    public PagoExteriorModel() {
    }

    public PagoExteriorModel(String pagoLocExt, String tipoRegi, String denopagoRegFis, String paisEfecPagoGen,
            String paisEfecPagoParFis, String denopago, String paisEfecPago, String aplicConvDobTrib, String pagExtSujRetNorLeg) {
        this.pagoLocExt = pagoLocExt;
        this.tipoRegi = tipoRegi;
        this.denopagoRegFis = denopagoRegFis;
        this.paisEfecPagoGen = paisEfecPagoGen;
        this.paisEfecPagoParFis = paisEfecPagoParFis;
        this.paisEfecPago = paisEfecPago;
        this.aplicConvDobTrib = aplicConvDobTrib;
        this.pagExtSujRetNorLeg = pagExtSujRetNorLeg;
        this.denopago = denopago;
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public String getDenopago() {
        return denopago;
    }

    public void setDenopago(String denopago) {
        this.denopago = denopago;
    }

    public String getPaisEfecPagoParFis() {
        return paisEfecPagoParFis;
    }

    public void setPaisEfecPagoParFis(String paisEfecPagoParFis) {
        this.paisEfecPagoParFis = paisEfecPagoParFis;
    }

    public String getPagoLocExt() {
        return pagoLocExt;
    }

    public void setPagoLocExt(String pagoLocExt) {
        this.pagoLocExt = pagoLocExt;
    }

    public String getTipoRegi() {
        return tipoRegi;
    }

    public void setTipoRegi(String tipoRegi) {
        this.tipoRegi = tipoRegi;
    }

    public String getPaisEfecPagoGen() {
        return paisEfecPagoGen;
    }

    public void setPaisEfecPagoGen(String paisEfecPagoGen) {
        this.paisEfecPagoGen = paisEfecPagoGen;
    }

    public String getPaisEfecPago() {
        return paisEfecPago;
    }

    public void setPaisEfecPago(String paisEfecPago) {
        this.paisEfecPago = paisEfecPago;
    }

    public String getAplicConvDobTrib() {
        return aplicConvDobTrib;
    }

    public void setAplicConvDobTrib(String aplicConvDobTrib) {
        this.aplicConvDobTrib = aplicConvDobTrib;
    }

    public String getPagExtSujRetNorLeg() {
        return pagExtSujRetNorLeg;
    }

    public void setPagExtSujRetNorLeg(String pagExtSujRetNorLeg) {
        this.pagExtSujRetNorLeg = pagExtSujRetNorLeg;
    }

    public String getDenopagoRegFis() {
        return denopagoRegFis;
    }

    public void setDenopagoRegFis(String denopagoRegFis) {
        this.denopagoRegFis = denopagoRegFis;
    }
//</editor-fold>
}
