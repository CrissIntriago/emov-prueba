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
@Table(name = "app_solicitud_departamento", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppSolicitudDepartamento.findAll", query = "SELECT a FROM AppSolicitudDepartamento a"),
    @NamedQuery(name = "AppSolicitudDepartamento.findById", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.id = :id"),
    @NamedQuery(name = "AppSolicitudDepartamento.findByEstado", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.estado = :estado"),
    @NamedQuery(name = "AppSolicitudDepartamento.findByDepartamento", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.departamento = :departamento"),
    @NamedQuery(name = "AppSolicitudDepartamento.findByFecha", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AppSolicitudDepartamento.findByResponsables", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.responsables = :responsables"),
    @NamedQuery(name = "AppSolicitudDepartamento.findByObservacion", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.observacion = :observacion"),
    @NamedQuery(name = "AppSolicitudDepartamento.findByInforme", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.informe = :informe"),
    @NamedQuery(name = "AppSolicitudDepartamento.findByUsuarioIngreso", query = "SELECT a FROM AppSolicitudDepartamento a WHERE a.usuarioIngreso = :usuarioIngreso")})
public class AppSolicitudDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "departamento")
    private BigInteger departamento;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 255)
    @Column(name = "responsables")
    private String responsables;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 255)
    @Column(name = "informe")
    private String informe;
    @Size(max = 255)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @JoinColumn(name = "solicitud", referencedColumnName = "id")
    @ManyToOne
    private AppSolicitudServicios solicitud;

    public AppSolicitudDepartamento() {
    }

    public AppSolicitudDepartamento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigInteger getDepartamento() {
        return departamento;
    }

    public void setDepartamento(BigInteger departamento) {
        this.departamento = departamento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getResponsables() {
        return responsables;
    }

    public void setResponsables(String responsables) {
        this.responsables = responsables;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
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
        if (!(object instanceof AppSolicitudDepartamento)) {
            return false;
        }
        AppSolicitudDepartamento other = (AppSolicitudDepartamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppSolicitudDepartamento[ id=" + id + " ]";
    }
    
}
