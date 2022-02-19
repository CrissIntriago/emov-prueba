/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "requisito", schema = "certificacion_presupuestaria_anual")
@NamedQueries({
    @NamedQuery(name = "Requisito.findAll", query = "SELECT r FROM Requisito r"),
    @NamedQuery(name = "Requisito.findById", query = "SELECT r FROM Requisito r WHERE r.id = :id"),
    @NamedQuery(name = "Requisito.findByNombre", query = "SELECT r FROM Requisito r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Requisito.findByDescripcion", query = "SELECT r FROM Requisito r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "Requisito.findByEstado", query = "SELECT r FROM Requisito r WHERE r.estado = :estado"),
    @NamedQuery(name = "Requisito.findByFechaCreacion", query = "SELECT r FROM Requisito r WHERE r.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Requisito.findByUsuarioCreacion", query = "SELECT r FROM Requisito r WHERE r.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Requisito.findByFechaModificacion", query = "SELECT r FROM Requisito r WHERE r.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Requisito.findByUsuarioModificacion", query = "SELECT r FROM Requisito r WHERE r.usuarioModificacion = :usuarioModificacion")})
public class Requisito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    public Requisito() {
    }

    public Requisito(Long id) {
        this.id = id;
    }

    public Requisito(Long id, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModificacion) {
        this.id = id;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModificacion = usuarioModificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
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
        if (!(object instanceof Requisito)) {
            return false;
        }
        Requisito other = (Requisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Requisito[ id=" + id + " ]";
    }

}
