/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_grupo_servidor", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThGrupoServidor.findAll", query = "SELECT t FROM ThGrupoServidor t"),
    @NamedQuery(name = "ThGrupoServidor.findById", query = "SELECT t FROM ThGrupoServidor t WHERE t.id = :id"),
    @NamedQuery(name = "ThGrupoServidor.findByDescripcion", query = "SELECT t FROM ThGrupoServidor t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "ThGrupoServidor.findByEstado", query = "SELECT t FROM ThGrupoServidor t WHERE t.estado = ?1 ORDER BY t.codigo ASC"),
    @NamedQuery(name = "ThGrupoServidor.findByFechaCreacion", query = "SELECT t FROM ThGrupoServidor t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThGrupoServidor.findByUsuarioCreacion", query = "SELECT t FROM ThGrupoServidor t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThGrupoServidor.findByFechaModificacion", query = "SELECT t FROM ThGrupoServidor t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThGrupoServidor.findByUsuarioModificacion", query = "SELECT t FROM ThGrupoServidor t WHERE t.usuarioModificacion = :usuarioModificacion")})
public class ThGrupoServidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "codigo")
    private String codigo;

    public ThGrupoServidor() {
        this.estado = Boolean.TRUE;
    }

    public ThGrupoServidor(Long id) {
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        if (!(object instanceof ThGrupoServidor)) {
            return false;
        }
        ThGrupoServidor other = (ThGrupoServidor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThGrupoServidor[ id=" + id + " ]";
    }

}
