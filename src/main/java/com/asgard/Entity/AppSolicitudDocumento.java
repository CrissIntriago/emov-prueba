/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_solicitud_documento", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppSolicitudDocumento.findAll", query = "SELECT a FROM AppSolicitudDocumento a"),
    @NamedQuery(name = "AppSolicitudDocumento.findById", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.id = :id"),
    @NamedQuery(name = "AppSolicitudDocumento.findByEstado", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.estado = :estado"),
    @NamedQuery(name = "AppSolicitudDocumento.findByNombreArchivo", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "AppSolicitudDocumento.findByRutaArchivo", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.rutaArchivo = :rutaArchivo"),
    @NamedQuery(name = "AppSolicitudDocumento.findByTipoArchivo", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.tipoArchivo = :tipoArchivo"),
    @NamedQuery(name = "AppSolicitudDocumento.findByUsuario", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.usuario = :usuario"),
    @NamedQuery(name = "AppSolicitudDocumento.findByFechaCreacion", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "AppSolicitudDocumento.findByTieneFirmaElectronica", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.tieneFirmaElectronica = :tieneFirmaElectronica"),
    @NamedQuery(name = "AppSolicitudDocumento.findByDescripcion", query = "SELECT a FROM AppSolicitudDocumento a WHERE a.descripcion = :descripcion")})
public class AppSolicitudDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "estado")
    private String estado;
    @Size(max = 255)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Size(max = 255)
    @Column(name = "ruta_archivo")
    private String rutaArchivo;
    @Size(max = 255)
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Size(max = 255)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "tiene_firma_electronica")
    private Boolean tieneFirmaElectronica;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "requisito", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamentoRequisitos requisito;
    @JoinColumn(name = "solicitud", referencedColumnName = "id")
    @ManyToOne
    private AppSolicitudServicios solicitud;

    public AppSolicitudDocumento() {
    }

    public AppSolicitudDocumento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getTieneFirmaElectronica() {
        return tieneFirmaElectronica;
    }

    public void setTieneFirmaElectronica(Boolean tieneFirmaElectronica) {
        this.tieneFirmaElectronica = tieneFirmaElectronica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public AppServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(AppServiciosDepartamentoRequisitos requisito) {
        this.requisito = requisito;
    }

    public AppSolicitudServicios getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(AppSolicitudServicios solicitud) {
        this.solicitud = solicitud;
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
        if (!(object instanceof AppSolicitudDocumento)) {
            return false;
        }
        AppSolicitudDocumento other = (AppSolicitudDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppSolicitudDocumento[ id=" + id + " ]";
    }
    
}
