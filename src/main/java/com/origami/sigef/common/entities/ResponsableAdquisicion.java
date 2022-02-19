/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "responsable_adquisicion", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "ResponsableAdquisicion.findAll", query = "SELECT r FROM ResponsableAdquisicion r"),
    @NamedQuery(name = "ResponsableAdquisicion.findById", query = "SELECT r FROM ResponsableAdquisicion r WHERE r.id = :id"),
    @NamedQuery(name = "ResponsableAdquisicion.findByFechaAsignacion", query = "SELECT r FROM ResponsableAdquisicion r WHERE r.fechaAsignacion = :fechaAsignacion"),
    @NamedQuery(name = "ResponsableAdquisicion.findByFechaFinalizacion", query = "SELECT r FROM ResponsableAdquisicion r WHERE r.fechaFinalizacion = :fechaFinalizacion"),
    @NamedQuery(name = "ResponsableAdquisicion.findByEstado", query = "SELECT r FROM ResponsableAdquisicion r WHERE r.estado = :estado")})
public class ResponsableAdquisicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignacion;
    @Column(name = "fecha_finalizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinalizacion;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "adquisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Adquisiciones adquisicion;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor responsable;

    public ResponsableAdquisicion() {
        this.estado = Boolean.TRUE;
    }

    public ResponsableAdquisicion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public Servidor getResponsable() {
        return responsable;
    }

    public void setResponsable(Servidor responsable) {
        this.responsable = responsable;
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
        if (!(object instanceof ResponsableAdquisicion)) {
            return false;
        }
        ResponsableAdquisicion other = (ResponsableAdquisicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ResponsableAdquisicion[ id=" + id + " ]";
    }

}
