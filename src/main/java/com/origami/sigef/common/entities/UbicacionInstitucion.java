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
 * @author Luis Pozo Gonzabay
 */
@Entity
@Table(name = "ubicacion_institucion", schema = "public")
@NamedQueries({
    @NamedQuery(name = "UbicacionInstitucion.findAll", query = "SELECT u FROM UbicacionInstitucion u"),
    @NamedQuery(name = "UbicacionInstitucion.findById", query = "SELECT u FROM UbicacionInstitucion u WHERE u.id = :id"),
    @NamedQuery(name = "UbicacionInstitucion.findByDescripcion", query = "SELECT u FROM UbicacionInstitucion u WHERE u.descripcion = :descripcion"),
    @NamedQuery(name = "UbicacionInstitucion.findByDireccion", query = "SELECT u FROM UbicacionInstitucion u WHERE u.direccion = :direccion"),
    @NamedQuery(name = "UbicacionInstitucion.findByEstado", query = "SELECT u FROM UbicacionInstitucion u WHERE u.estado = :estado"),
    @NamedQuery(name = "UbicacionInstitucion.findByUserCreacion", query = "SELECT u FROM UbicacionInstitucion u WHERE u.userCreacion = :userCreacion"),
    @NamedQuery(name = "UbicacionInstitucion.findByUserModificacion", query = "SELECT u FROM UbicacionInstitucion u WHERE u.userModificacion = :userModificacion"),
    @NamedQuery(name = "UbicacionInstitucion.findByFechaCreacion", query = "SELECT u FROM UbicacionInstitucion u WHERE u.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "UbicacionInstitucion.findByFechaModificacion", query = "SELECT u FROM UbicacionInstitucion u WHERE u.fechaModificacion = :fechaModificacion")})
public class UbicacionInstitucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "user_creacion")
    private String userCreacion;
    @Column(name = "user_modificacion")
    private String userModificacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public UbicacionInstitucion() {
    }

    public UbicacionInstitucion(Long id) {
        this.id = id;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public String getUserModificacion() {
        return userModificacion;
    }

    public void setUserModificacion(String userModificacion) {
        this.userModificacion = userModificacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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
        if (!(object instanceof UbicacionInstitucion)) {
            return false;
        }
        UbicacionInstitucion other = (UbicacionInstitucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.UbicacionInstitucion[ id=" + id + " ]";
    }

}
