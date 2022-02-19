/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_clase_local", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenClaseLocal.findAll", query = "SELECT r FROM RenClaseLocal r"),
    @NamedQuery(name = "RenClaseLocal.findById", query = "SELECT r FROM RenClaseLocal r WHERE r.id = :id"),
    @NamedQuery(name = "RenClaseLocal.findByDescripcion", query = "SELECT r FROM RenClaseLocal r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "RenClaseLocal.findByEstado", query = "SELECT r FROM RenClaseLocal r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenClaseLocal.findByFechaIngreso", query = "SELECT r FROM RenClaseLocal r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RenClaseLocal.findByUsuarioIngreso", query = "SELECT r FROM RenClaseLocal r WHERE r.usuarioIngreso = :usuarioIngreso")})
public class RenClaseLocal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;

    public RenClaseLocal() {
    }

    public RenClaseLocal(Long id) {
        this.id = id;
    }

    public RenClaseLocal(Long id, boolean estado, String usuarioIngreso) {
        this.id = id;
        this.estado = estado;
        this.usuarioIngreso = usuarioIngreso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
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
        if (!(object instanceof RenClaseLocal)) {
            return false;
        }
        RenClaseLocal other = (RenClaseLocal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenClaseLocal[ id=" + id + " ]";
    }
    
}
