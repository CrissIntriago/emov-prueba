/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

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
@Table(name = "coa_juicio_notificacion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoaJuicioNotificacion.findAll", query = "SELECT c FROM CoaJuicioNotificacion c"),
    @NamedQuery(name = "CoaJuicioNotificacion.findById", query = "SELECT c FROM CoaJuicioNotificacion c WHERE c.id = :id"),
    @NamedQuery(name = "CoaJuicioNotificacion.findByEstado", query = "SELECT c FROM CoaJuicioNotificacion c WHERE c.estado = :estado"),
    @NamedQuery(name = "CoaJuicioNotificacion.findByFechaIngreso", query = "SELECT c FROM CoaJuicioNotificacion c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CoaJuicioNotificacion.findByNumOficio", query = "SELECT c FROM CoaJuicioNotificacion c WHERE c.numOficio = :numOficio"),
    @NamedQuery(name = "CoaJuicioNotificacion.findByUsuarioIngreso", query = "SELECT c FROM CoaJuicioNotificacion c WHERE c.usuarioIngreso = :usuarioIngreso")})
public class CoaJuicioNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "num_oficio")
    private Integer numOficio;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @JoinColumn(name = "juicio", referencedColumnName = "id")
    @ManyToOne
    private CoaJuicio juicio;

    public CoaJuicioNotificacion() {
    }

    public CoaJuicioNotificacion(Long id) {
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getNumOficio() {
        return numOficio;
    }

    public void setNumOficio(Integer numOficio) {
        this.numOficio = numOficio;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public CoaJuicio getJuicio() {
        return juicio;
    }

    public void setJuicio(CoaJuicio juicio) {
        this.juicio = juicio;
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
        if (!(object instanceof CoaJuicioNotificacion)) {
            return false;
        }
        CoaJuicioNotificacion other = (CoaJuicioNotificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CoaJuicioNotificacion[ id=" + id + " ]";
    }
    
}
