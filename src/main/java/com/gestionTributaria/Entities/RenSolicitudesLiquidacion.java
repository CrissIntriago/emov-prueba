/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "ren_solicitudes_liquidacion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenSolicitudesLiquidacion.findAll", query = "SELECT r FROM RenSolicitudesLiquidacion r"),
    @NamedQuery(name = "RenSolicitudesLiquidacion.findById", query = "SELECT r FROM RenSolicitudesLiquidacion r WHERE r.id = :id"),
    @NamedQuery(name = "RenSolicitudesLiquidacion.findByEstado", query = "SELECT r FROM RenSolicitudesLiquidacion r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenSolicitudesLiquidacion.findByLiquidacion", query = "SELECT r FROM RenSolicitudesLiquidacion r WHERE r.liquidacion = :liquidacion")})
public class RenSolicitudesLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "liquidacion",referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "sol_exoneracion", referencedColumnName = "id")
    @ManyToOne
    private FnSolicitudExoneracion solExoneracion;

    public RenSolicitudesLiquidacion() {
    }

    public RenSolicitudesLiquidacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public FnSolicitudExoneracion getSolExoneracion() {
        return solExoneracion;
    }

    public void setSolExoneracion(FnSolicitudExoneracion solExoneracion) {
        this.solExoneracion = solExoneracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RenSolicitudesLiquidacion)) {
            return false;
        }
        RenSolicitudesLiquidacion other = (RenSolicitudesLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenSolicitudesLiquidacion[ id=" + id + " ]";
    }
    
}
