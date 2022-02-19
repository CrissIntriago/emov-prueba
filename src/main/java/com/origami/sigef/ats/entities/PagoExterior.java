/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author jesus
 */
@Entity
@Table(schema = "ats", name = "pago_exterior")
public class PagoExterior implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Column(name = "pago_loc_ext")
    private String pagoLocExt;
    @Column(name = "tipo_regi")
    private String tipoRegi;
    @Column(name = "deno_pago_reg_fis")
    private String denopagoRegFis;
    @Column(name = "pais_efec_pago_gen")
    private String paisEfecPagoGen;
    @Column(name = "pais_efec_pago_par_fis")
    private String paisEfecPagoParFis;
    @Column(name = "pais_efec_pago")
    private String paisEfecPago = "NA";
    @Column(name = "aplic_conv_dob_trib")
    private String aplicConvDobTrib = "NA";
    @Column(name = "pag_ext_suj_ret_nor_leg")
    private String pagExtSujRetNorLeg = "NA";
    @Column(name = "deno_pago")
    private String denopago;

    public PagoExterior() {
    }

    public PagoExterior(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDenopagoRegFis() {
        return denopagoRegFis;
    }

    public void setDenopagoRegFis(String denopagoRegFis) {
        this.denopagoRegFis = denopagoRegFis;
    }

    public String getPaisEfecPagoGen() {
        return paisEfecPagoGen;
    }

    public void setPaisEfecPagoGen(String paisEfecPagoGen) {
        this.paisEfecPagoGen = paisEfecPagoGen;
    }

    public String getPaisEfecPagoParFis() {
        return paisEfecPagoParFis;
    }

    public void setPaisEfecPagoParFis(String paisEfecPagoParFis) {
        this.paisEfecPagoParFis = paisEfecPagoParFis;
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

    public String getDenopago() {
        return denopago;
    }

    public void setDenopago(String denopago) {
        this.denopago = denopago;
    }

}
