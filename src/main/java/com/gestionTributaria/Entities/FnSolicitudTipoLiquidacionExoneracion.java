/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author Administrator
 */
@Entity
@Table(name = "fn_solicitud_tipo_liquidacion_exoneracion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnSolicitudTipoLiquidacionExoneracion.findAll", query = "SELECT f FROM FnSolicitudTipoLiquidacionExoneracion f")})
public class FnSolicitudTipoLiquidacionExoneracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "solicitud_exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnSolicitudExoneracion solicitudExoneracion;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenTipoLiquidacion tipoLiquidacion;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenRubrosLiquidacion rubro;

    public FnSolicitudTipoLiquidacionExoneracion() {
    }

    public FnSolicitudTipoLiquidacionExoneracion(Long id) {
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

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }
}
