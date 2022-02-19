/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_solicitud_exoneracion_predios", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnSolicitudExoneracionPredios.findAll", query = "SELECT f FROM FnSolicitudExoneracionPredios f"),
    @NamedQuery(name = "FnSolicitudExoneracionPredios.findById", query = "SELECT f FROM FnSolicitudExoneracionPredios f WHERE f.id = :id")})
public class FnSolicitudExoneracionPredios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "solicitud_exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnSolicitudExoneracion solicitudExoneracion;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @Column(name = "cod_local")
    private String codLocal;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;

    public FnSolicitudExoneracionPredios() {
    }

    public FnSolicitudExoneracionPredios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FnSolicitudExoneracion getSolicitudExoneracion() {
        return solicitudExoneracion;
    }

    public void setSolicitudExoneracion(FnSolicitudExoneracion solicitudExoneracion) {
        this.solicitudExoneracion = solicitudExoneracion;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getCodLocal() {
        return codLocal;
    }

    public void setCodLocal(String codLocal) {
        this.codLocal = codLocal;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    @Override
    public String toString() {
        return "FnSolicitudExoneracionPredios{" + "id=" + id + '}';
    }

}
