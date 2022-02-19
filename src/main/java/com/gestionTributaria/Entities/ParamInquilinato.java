/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "param_inquilinato", schema = Utils.SCHEMA_COMISARIA)
@XmlRootElement
public class ParamInquilinato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "avaluo_desde")
    private BigDecimal avaluoDesde;
    @Column(name = "avaluo_hasta")
    private BigDecimal avaluoHasta;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "porc_sbu")
    private BigDecimal porcSbu;

    public ParamInquilinato() {
        estado = true;
    }

    public ParamInquilinato(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAvaluoDesde() {
        return avaluoDesde;
    }

    public void setAvaluoDesde(BigDecimal avaluoDesde) {
        this.avaluoDesde = avaluoDesde;
    }

    public BigDecimal getAvaluoHasta() {
        return avaluoHasta;
    }

    public void setAvaluoHasta(BigDecimal avaluoHasta) {
        this.avaluoHasta = avaluoHasta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getPorcSbu() {
        return porcSbu;
    }

    public void setPorcSbu(BigDecimal porcSbu) {
        this.porcSbu = porcSbu;
    }

}
