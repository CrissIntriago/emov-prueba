/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "remision_interes", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
public class RemisionInteresMulta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoLiquidacion tipoLliquidacion;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes_inicio")
    private Integer mesInicio;
    @Column(name = "mes_fin")
    private Integer mesFin;
    @Column(name = "multa")
    private Boolean multa;
    @Column(name = "interes")
    private Boolean interes;

    public RemisionInteresMulta() {
        interes = false;
        multa = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinaRenTipoLiquidacion getTipoLliquidacion() {
        return tipoLliquidacion;
    }

    public void setTipoLliquidacion(FinaRenTipoLiquidacion tipoLliquidacion) {
        this.tipoLliquidacion = tipoLliquidacion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(Integer mesInicio) {
        this.mesInicio = mesInicio;
    }

    public Integer getMesFin() {
        return mesFin;
    }

    public void setMesFin(Integer mesFin) {
        this.mesFin = mesFin;
    }

    public Boolean getMulta() {
        return multa;
    }

    public void setMulta(Boolean multa) {
        this.multa = multa;
    }

    public Boolean getInteres() {
        return interes;
    }

    public void setInteres(Boolean interes) {
        this.interes = interes;
    }

}
