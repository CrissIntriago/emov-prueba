/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "predio_autorizar", schema = Utils.SCHEMA_VENTANILLA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PredioAutorizar.findAll", query = "SELECT p FROM PredioAutorizar p WHERE p.autorizado=true"),
    @NamedQuery(name = "PredioAutorizar.findById", query = "SELECT p FROM PredioAutorizar p WHERE p.autorizado=true")})
public class PredioAutorizar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "dato")
    private String dato;
    @Column(name = "autorizado")
    private Boolean autorizado;
    @Column(name = "fecha_creacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "fecha_autorizacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAutorizacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "usuario_autoriza")
    private String usuarioAutoriza;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "cancelado")
    private Boolean cancelado;
    @Column(name = "observacion_autoriza")
    private String observacion_autoriza;

    public PredioAutorizar() {
    }

    public PredioAutorizar(String dato, Date fechaCreacion, String usuarioCreacion, String observacion, Boolean cancelado,
            Boolean autorizado) {
        this.dato = dato;
        this.autorizado = autorizado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.observacion = observacion;
        this.cancelado = cancelado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Boolean autorizado) {
        this.autorizado = autorizado;
    }

    public PredioAutorizar(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }


    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioAutoriza() {
        return usuarioAutoriza;
    }

    public void setUsuarioAutoriza(String usuarioAutoriza) {
        this.usuarioAutoriza = usuarioAutoriza;
    }

    public String getObservacion_autoriza() {
        return observacion_autoriza;
    }

    public void setObservacion_autoriza(String observacion_autoriza) {
        this.observacion_autoriza = observacion_autoriza;
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
        if (!(object instanceof PredioAutorizar)) {
            return false;
        }
        PredioAutorizar other = (PredioAutorizar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PredioAutorizar{" + "id=" + id + ", dato=" + dato + ", fechaCreacion=" + fechaCreacion
                + ", fechaAutorizacion=" + fechaAutorizacion + ", usuarioCreacion=" + usuarioCreacion
                + ", usuarioAutoriza=" + usuarioAutoriza + ", observacion=" + observacion
                + ", cancelado=" + cancelado + ",autorizado=" + autorizado + '}';
    }

}
