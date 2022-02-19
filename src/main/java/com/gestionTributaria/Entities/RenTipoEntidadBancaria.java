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
@Table(name = "ren_tipo_entidad_bancaria", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenTipoEntidadBancaria.findAll", query = "SELECT r FROM RenTipoEntidadBancaria r"),
    @NamedQuery(name = "RenTipoEntidadBancaria.findById", query = "SELECT r FROM RenTipoEntidadBancaria r WHERE r.id = :id"),
    @NamedQuery(name = "RenTipoEntidadBancaria.findByDescripcion", query = "SELECT r FROM RenTipoEntidadBancaria r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "RenTipoEntidadBancaria.findByFechaIngreso", query = "SELECT r FROM RenTipoEntidadBancaria r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RenTipoEntidadBancaria.findByEstado", query = "SELECT r FROM RenTipoEntidadBancaria r WHERE r.estado = :estado")})
public class RenTipoEntidadBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;

    public RenTipoEntidadBancaria() {
    }

    public RenTipoEntidadBancaria(Long id) {
        this.id = id;
    }

    public RenTipoEntidadBancaria(Long id, Date fechaIngreso, boolean estado) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof RenTipoEntidadBancaria)) {
            return false;
        }
        RenTipoEntidadBancaria other = (RenTipoEntidadBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenTipoEntidadBancaria[ id=" + id + " ]";
    }
    
}
