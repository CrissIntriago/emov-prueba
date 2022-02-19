/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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
import org.hibernate.annotations.Formula;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_servidor_cargo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThServidorCargo.findAll", query = "SELECT t FROM ThServidorCargo t"),
    @NamedQuery(name = "ThServidorCargo.findById", query = "SELECT t FROM ThServidorCargo t WHERE t.id = :id"),
    @NamedQuery(name = "ThServidorCargo.findByIdServidor", query = "SELECT t FROM ThServidorCargo t WHERE t.idServidor = ?1 AND t.activo=TRUE"),
    @NamedQuery(name = "ThServidorCargo.findByFechaAsignacion", query = "SELECT t FROM ThServidorCargo t WHERE t.fechaAsignacion = :fechaAsignacion"),
    @NamedQuery(name = "ThServidorCargo.findByFechaFinalizacion", query = "SELECT t FROM ThServidorCargo t WHERE t.fechaFinalizacion = :fechaFinalizacion")})
public class ThServidorCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_cargo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThCargo idCargo;
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;
    @Column(name = "fecha_finalizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaFinalizacion;
    @JoinColumn(name = "id_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor idServidor;
    @Column(name = "activo")
    private Boolean activo;

    @Formula("(SELECT COUNT(*) = 0 FROM talento_humano.th_conf_liquidacion_rol crl WHERE crl.estado AND crl.id_cuenta is null AND crl.id_servidor_cargo = id)")
    private Boolean falta;

    public ThServidorCargo() {
        this.activo = Boolean.TRUE;
    }

    public ThServidorCargo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servidor getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Servidor idServidor) {
        this.idServidor = idServidor;
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

    public ThCargo getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(ThCargo idCargo) {
        this.idCargo = idCargo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getFalta() {
        return falta;
    }

    public void setFalta(Boolean falta) {
        this.falta = falta;
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
        if (!(object instanceof ThServidorCargo)) {
            return false;
        }
        ThServidorCargo other = (ThServidorCargo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThServidorCargo[ id=" + id + " ]";
    }

}
