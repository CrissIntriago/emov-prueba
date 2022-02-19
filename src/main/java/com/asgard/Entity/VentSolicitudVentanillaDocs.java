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
@Table(name = "vent_solicitud_ventanilla_docs", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VentSolicitudVentanillaDocs.findAll", query = "SELECT v FROM VentSolicitudVentanillaDocs v"),
    @NamedQuery(name = "VentSolicitudVentanillaDocs.findById", query = "SELECT v FROM VentSolicitudVentanillaDocs v WHERE v.id = :id"),
    @NamedQuery(name = "VentSolicitudVentanillaDocs.findByFechaCreacion", query = "SELECT v FROM VentSolicitudVentanillaDocs v WHERE v.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "VentSolicitudVentanillaDocs.findByNombreArchivo", query = "SELECT v FROM VentSolicitudVentanillaDocs v WHERE v.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "VentSolicitudVentanillaDocs.findByFecha", query = "SELECT v FROM VentSolicitudVentanillaDocs v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "VentSolicitudVentanillaDocs.findByRutaArchivo", query = "SELECT v FROM VentSolicitudVentanillaDocs v WHERE v.rutaArchivo = :rutaArchivo"),
    @NamedQuery(name = "VentSolicitudVentanillaDocs.findByFormato", query = "SELECT v FROM VentSolicitudVentanillaDocs v WHERE v.formato = :formato")})
public class VentSolicitudVentanillaDocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 255)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 255)
    @Column(name = "ruta_archivo")
    private String rutaArchivo;
    @Size(max = 255)
    @Column(name = "formato")
    private String formato;
    @JoinColumn(name = "requisito", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamentoRequisitos requisito;
    @JoinColumn(name = "solicitud", referencedColumnName = "id")
    @ManyToOne
    private VentSolicitudVentanilla solicitud;

    public VentSolicitudVentanillaDocs() {
    }

    public VentSolicitudVentanillaDocs(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public AppServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(AppServiciosDepartamentoRequisitos requisito) {
        this.requisito = requisito;
    }

    public VentSolicitudVentanilla getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(VentSolicitudVentanilla solicitud) {
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
        if (!(object instanceof VentSolicitudVentanillaDocs)) {
            return false;
        }
        VentSolicitudVentanillaDocs other = (VentSolicitudVentanillaDocs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.VentSolicitudVentanillaDocs[ id=" + id + " ]";
    }
    
}
