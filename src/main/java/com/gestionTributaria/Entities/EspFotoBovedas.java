/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "esp_foto_bovedas", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspFotoBovedas.findAll", query = "SELECT e FROM EspFotoBovedas e"),
    @NamedQuery(name = "EspFotoBovedas.findById", query = "SELECT e FROM EspFotoBovedas e WHERE e.id = :id"),
    @NamedQuery(name = "EspFotoBovedas.findByCementerioBoveda", query = "SELECT e FROM EspFotoBovedas e WHERE e.cementerioBoveda = :cementerioBoveda"),
    @NamedQuery(name = "EspFotoBovedas.findByDescripcion", query = "SELECT e FROM EspFotoBovedas e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EspFotoBovedas.findByContentType", query = "SELECT e FROM EspFotoBovedas e WHERE e.contentType = :contentType"),
    @NamedQuery(name = "EspFotoBovedas.findByFechaCreacion", query = "SELECT e FROM EspFotoBovedas e WHERE e.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "EspFotoBovedas.findByFileOid", query = "SELECT e FROM EspFotoBovedas e WHERE e.fileOid = :fileOid"),
    @NamedQuery(name = "EspFotoBovedas.findByNombre", query = "SELECT e FROM EspFotoBovedas e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EspFotoBovedas.findBySisEnabled", query = "SELECT e FROM EspFotoBovedas e WHERE e.sisEnabled = :sisEnabled"),
    @NamedQuery(name = "EspFotoBovedas.findByUsuario", query = "SELECT e FROM EspFotoBovedas e WHERE e.usuario = :usuario")})
public class EspFotoBovedas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "cementerio_boveda", referencedColumnName = "id")
    @ManyToOne
    private EspCementerioBoveda cementerioBoveda;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 255)
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "file_oid")
    private long fileOid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "sis_enabled")
    private Boolean sisEnabled;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;

    @Column(name = "ruta")
    private String ruta;

    @Transient
    private InputStream inputStream;

    public EspFotoBovedas() {
    }

    public EspFotoBovedas(Long id) {
        this.id = id;
    }

    public EspFotoBovedas(Long id, long fileOid, String nombre) {
        this.id = id;
        this.fileOid = fileOid;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EspCementerioBoveda getCementerioBoveda() {
        return cementerioBoveda;
    }

    public void setCementerioBoveda(EspCementerioBoveda cementerioBoveda) {
        this.cementerioBoveda = cementerioBoveda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getFileOid() {
        return fileOid;
    }

    public void setFileOid(long fileOid) {
        this.fileOid = fileOid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getSisEnabled() {
        return sisEnabled;
    }

    public void setSisEnabled(Boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
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
        if (!(object instanceof EspFotoBovedas)) {
            return false;
        }
        EspFotoBovedas other = (EspFotoBovedas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.EspFotoBovedas[ id=" + id + " ]";
    }

}
