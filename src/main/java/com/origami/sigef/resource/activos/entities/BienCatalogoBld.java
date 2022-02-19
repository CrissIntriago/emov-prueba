/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.entities;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "bien_catalogo_bld", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BienCatalogoBld.findAll", query = "SELECT c FROM BienCatalogoBld c"),
    @NamedQuery(name = "BienCatalogoBld.findById", query = "SELECT c FROM BienCatalogoBld c WHERE c.id = :id"),
    @NamedQuery(name = "BienCatalogoBld.findByItemPresupuestario", query = "SELECT c FROM BienCatalogoBld c WHERE c.itemPresupuestario = :itemPresupuestario"),
    @NamedQuery(name = "BienCatalogoBld.findByIdBien", query = "SELECT c FROM BienCatalogoBld c WHERE c.idBien = :idBien"),
    @NamedQuery(name = "BienCatalogoBld.findByDescripcion", query = "SELECT c FROM BienCatalogoBld c WHERE c.descripcion = :descripcion")})
public class BienCatalogoBld implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "item_presupuestario")
    private String itemPresupuestario;
    @Column(name = "id_bien")
    private String idBien;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    public BienCatalogoBld() {
    }

    public BienCatalogoBld(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemPresupuestario() {
        return itemPresupuestario;
    }

    public void setItemPresupuestario(String itemPresupuestario) {
        this.itemPresupuestario = itemPresupuestario;
    }

    public String getIdBien() {
        return idBien;
    }

    public void setIdBien(String idBien) {
        this.idBien = idBien;
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

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
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
        if (!(object instanceof BienCatalogoBld)) {
            return false;
        }
        BienCatalogoBld other = (BienCatalogoBld) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.activos.entities.BienCatalogoBld[ id=" + id + " ]";
    }

}
