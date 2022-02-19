/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "control_cuenta_contable")
@NamedQueries({
    @NamedQuery(name = "ControlCuentaContable.findAll", query = "SELECT c FROM ControlCuentaContable c"),
    @NamedQuery(name = "ControlCuentaContable.findById", query = "SELECT c FROM ControlCuentaContable c WHERE c.id = :id"),
    @NamedQuery(name = "ControlCuentaContable.findByOrden", query = "SELECT c FROM ControlCuentaContable c WHERE c.orden = :orden"),
    @NamedQuery(name = "ControlCuentaContable.findByNombreMes", query = "SELECT c FROM ControlCuentaContable c WHERE c.nombreMes = :nombreMes"),
    @NamedQuery(name = "ControlCuentaContable.findByFechaCierre", query = "SELECT c FROM ControlCuentaContable c WHERE c.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "ControlCuentaContable.findByEstado", query = "SELECT c FROM ControlCuentaContable c WHERE c.estado = :estado"),
    @NamedQuery(name = "ControlCuentaContable.findByPeriodo", query = "SELECT c FROM ControlCuentaContable c WHERE c.periodo = :periodo"),
    @NamedQuery(name = "ControlCuentaContable.findByUsuarioCreacion", query = "SELECT c FROM ControlCuentaContable c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ControlCuentaContable.findByFechaCreacion", query = "SELECT c FROM ControlCuentaContable c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ControlCuentaContable.findByUsuarioModificacion", query = "SELECT c FROM ControlCuentaContable c WHERE c.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ControlCuentaContable.findByFechaModificacion", query = "SELECT c FROM ControlCuentaContable c WHERE c.fechaModificacion = :fechaModificacion")})
public class ControlCuentaContable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "orden")
    private Short orden;
    @Size(max = 2147483647)
    @Column(name = "nombre_mes")
    private String nombreMes;
    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.DATE)
    private Date fechaCierre;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "estado_a", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoAprobado;

    public ControlCuentaContable() {
        this.estado = Boolean.TRUE;
    }

    public ControlCuentaContable(Long id) {
        this.id = id;
    }

    public ControlCuentaContable(Long id, String usuarioCreacion, Date fechaCreacion) {
        this.id = id;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
    }

    public String getNombreMes() {
        return nombreMes;
    }

    public void setNombreMes(String nombreMes) {
        this.nombreMes = nombreMes;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public CatalogoItem getEstadoAprobado() {
        return estadoAprobado;
    }

    public void setEstadoAprobado(CatalogoItem estadoAprobado) {
        this.estadoAprobado = estadoAprobado;
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
        if (!(object instanceof ControlCuentaContable)) {
            return false;
        }
        ControlCuentaContable other = (ControlCuentaContable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ControlCuentaContable[ id=" + id + " ]";
    }

}
