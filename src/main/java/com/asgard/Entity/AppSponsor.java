/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_sponsor", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppSponsor.findAll", query = "SELECT a FROM AppSponsor a"),
    @NamedQuery(name = "AppSponsor.findById", query = "SELECT a FROM AppSponsor a WHERE a.id = :id"),
    @NamedQuery(name = "AppSponsor.findByArchivo", query = "SELECT a FROM AppSponsor a WHERE a.archivo = :archivo"),
    @NamedQuery(name = "AppSponsor.findByDescripcion", query = "SELECT a FROM AppSponsor a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AppSponsor.findByEstado", query = "SELECT a FROM AppSponsor a WHERE a.estado = :estado"),
    @NamedQuery(name = "AppSponsor.findByFechaCreacion", query = "SELECT a FROM AppSponsor a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "AppSponsor.findByRutaVideo", query = "SELECT a FROM AppSponsor a WHERE a.rutaVideo = :rutaVideo"),
    @NamedQuery(name = "AppSponsor.findByTiempoDuracion", query = "SELECT a FROM AppSponsor a WHERE a.tiempoDuracion = :tiempoDuracion"),
    @NamedQuery(name = "AppSponsor.findByTipo", query = "SELECT a FROM AppSponsor a WHERE a.tipo = :tipo"),
    @NamedQuery(name = "AppSponsor.findByTitulo", query = "SELECT a FROM AppSponsor a WHERE a.titulo = :titulo")})
public class AppSponsor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "archivo")
    private String archivo;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 255)
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 255)
    @Column(name = "ruta_video")
    private String rutaVideo;
    @Column(name = "tiempo_duracion")
    private BigInteger tiempoDuracion;
    @Size(max = 255)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 255)
    @Column(name = "titulo")
    private String titulo;

    public AppSponsor() {
    }

    public AppSponsor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getRutaVideo() {
        return rutaVideo;
    }

    public void setRutaVideo(String rutaVideo) {
        this.rutaVideo = rutaVideo;
    }

    public BigInteger getTiempoDuracion() {
        return tiempoDuracion;
    }

    public void setTiempoDuracion(BigInteger tiempoDuracion) {
        this.tiempoDuracion = tiempoDuracion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
        if (!(object instanceof AppSponsor)) {
            return false;
        }
        AppSponsor other = (AppSponsor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppSponsor[ id=" + id + " ]";
    }
    
}
