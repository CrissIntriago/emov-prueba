/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fn_estado_exoneracion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnEstadoExoneracion.findAll", query = "SELECT f FROM FnEstadoExoneracion f"),
    @NamedQuery(name = "FnEstadoExoneracion.findById", query = "SELECT f FROM FnEstadoExoneracion f WHERE f.id = :id"),
    @NamedQuery(name = "FnEstadoExoneracion.findByDescripcion", query = "SELECT f FROM FnEstadoExoneracion f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FnEstadoExoneracion.findByEstado", query = "SELECT f FROM FnEstadoExoneracion f WHERE f.estado = :estado"),
    @NamedQuery(name = "FnEstadoExoneracion.findByFechaIngreso", query = "SELECT f FROM FnEstadoExoneracion f WHERE f.fechaIngreso = :fechaIngreso")})
public class FnEstadoExoneracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 25)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnSolicitudExoneracion> fnSolicitudExoneracionList;
    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnSolicitudExoneraciones> fnSolicitudExoneracionesList;

    public FnEstadoExoneracion() {
    }

    public FnEstadoExoneracion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FnEstadoExoneracion(String descripcion) {
        this.descripcion = descripcion;
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    
    public List<FnSolicitudExoneracion> getFnSolicitudExoneracionList() {
        return fnSolicitudExoneracionList;
    }

    public void setFnSolicitudExoneracionList(List<FnSolicitudExoneracion> fnSolicitudExoneracionList) {
        this.fnSolicitudExoneracionList = fnSolicitudExoneracionList;
    }

    
    public List<FnSolicitudExoneraciones> getFnSolicitudExoneracionesList() {
        return fnSolicitudExoneracionesList;
    }

    public void setFnSolicitudExoneracionesList(List<FnSolicitudExoneraciones> fnSolicitudExoneracionesList) {
        this.fnSolicitudExoneracionesList = fnSolicitudExoneracionesList;
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
        if (!(object instanceof FnEstadoExoneracion)) {
            return false;
        }
        FnEstadoExoneracion other = (FnEstadoExoneracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FnEstadoExoneracion[ id=" + id + " ]";
    }
    
}
