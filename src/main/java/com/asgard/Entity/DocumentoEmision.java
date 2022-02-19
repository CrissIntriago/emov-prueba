/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import java.io.Serializable;
import java.math.BigInteger;
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

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "documento_emision")
@NamedQueries({
    @NamedQuery(name = "DocumentoEmision.findAll", query = "SELECT d FROM DocumentoEmision d")
    , @NamedQuery(name = "DocumentoEmision.findById", query = "SELECT d FROM DocumentoEmision d WHERE d.id = :id")
    , @NamedQuery(name = "DocumentoEmision.findByNombre", query = "SELECT d FROM DocumentoEmision d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "DocumentoEmision.findByDescripcion", query = "SELECT d FROM DocumentoEmision d WHERE d.descripcion = :descripcion")
    , @NamedQuery(name = "DocumentoEmision.findByEstado", query = "SELECT d FROM DocumentoEmision d WHERE d.estado = :estado")
    , @NamedQuery(name = "DocumentoEmision.findByTipo", query = "SELECT d FROM DocumentoEmision d WHERE d.tipo = :tipo")
    , @NamedQuery(name = "DocumentoEmision.findByDiaValidez", query = "SELECT d FROM DocumentoEmision d WHERE d.diaValidez = :diaValidez")
    , @NamedQuery(name = "DocumentoEmision.findByDirectorio", query = "SELECT d FROM DocumentoEmision d WHERE d.directorio = :directorio")
    , @NamedQuery(name = "DocumentoEmision.findByFecha", query = "SELECT d FROM DocumentoEmision d WHERE d.fecha = :fecha")
    , @NamedQuery(name = "DocumentoEmision.findByUsuarioCreacion", query = "SELECT d FROM DocumentoEmision d WHERE d.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "DocumentoEmision.findByUsuarioModificacion", query = "SELECT d FROM DocumentoEmision d WHERE d.usuarioModificacion = :usuarioModificacion")
    , @NamedQuery(name = "DocumentoEmision.findByFechaCreacion", query = "SELECT d FROM DocumentoEmision d WHERE d.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "DocumentoEmision.findByFechaModificacion", query = "SELECT d FROM DocumentoEmision d WHERE d.fechaModificacion = :fechaModificacion")})
public class DocumentoEmision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 255)
    @Column(name = "nombre", length = 255)
    private String nombre;
    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;
    @Size(max = 3)
    @Column(name = "estado", length = 3)
    private String estado;
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "dia_validez")
    private BigInteger diaValidez;
    @Size(max = 500)
    @Column(name = "directorio", length = 500)
    private String directorio;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 250)
    @Column(name = "usuario_creacion", length = 250)
    private String usuarioCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion", length = 2147483647)
    private String usuarioModificacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public DocumentoEmision() {
    }

    public DocumentoEmision(Long id) {
        this.id = id;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public BigInteger getDiaValidez() {
        return diaValidez;
    }

    public void setDiaValidez(BigInteger diaValidez) {
        this.diaValidez = diaValidez;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
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
        if (!(object instanceof DocumentoEmision)) {
            return false;
        }
        DocumentoEmision other = (DocumentoEmision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.asgard.Entity.DocumentoEmision[ id=" + id + " ]";
    }
    
}
