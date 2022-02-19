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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "activo_fijo_servidor", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "ActivoFijoServidor.findAll", query = "SELECT a FROM ActivoFijoServidor a"),
    @NamedQuery(name = "ActivoFijoServidor.findById", query = "SELECT a FROM ActivoFijoServidor a WHERE a.id = :id"),
    @NamedQuery(name = "ActivoFijoServidor.findByAsignado", query = "SELECT a FROM ActivoFijoServidor a WHERE a.asignado = :asignado"),
    @NamedQuery(name = "ActivoFijoServidor.findByFechaAsignacion", query = "SELECT a FROM ActivoFijoServidor a WHERE a.fechaAsignacion = :fechaAsignacion"),
    @NamedQuery(name = "ActivoFijoServidor.findByFechaDevolucion", query = "SELECT a FROM ActivoFijoServidor a WHERE a.fechaDevolucion = :fechaDevolucion")})
public class ActivoFijoServidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "asignado")
    private Boolean asignado;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "observacion_inicial")
    private String observacionInicial;
    @Column(name = "observacion_final")
    private String observacionFinal;
    @Column(name = "estado_bien")
    private String estadoBien;
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignacion;
    @Column(name = "fecha_devolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucion;
    @JoinColumn(name = "activo_fijo_custodio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ActivoFijoCustodio activoFijoCustodio;
    @JoinColumn(name = "bienes_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem bienesItem;

    public ActivoFijoServidor() {
    }

    public ActivoFijoServidor(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public String getObservacionInicial() {
        return observacionInicial;
    }

    public void setObservacionInicial(String observacionInicial) {
        this.observacionInicial = observacionInicial;
    }

    public String getObservacionFinal() {
        return observacionFinal;
    }

    public void setObservacionFinal(String observacionFinal) {
        this.observacionFinal = observacionFinal;
    }

    public String getEstadoBien() {
        return estadoBien;
    }

    public void setEstadoBien(String estadoBien) {
        this.estadoBien = estadoBien;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public ActivoFijoCustodio getActivoFijoCustodio() {
        return activoFijoCustodio;
    }

    public void setActivoFijoCustodio(ActivoFijoCustodio activoFijoCustodio) {
        this.activoFijoCustodio = activoFijoCustodio;
    }

    public BienesItem getBienesItem() {
        return bienesItem;
    }

    public void setBienesItem(BienesItem bienesItem) {
        this.bienesItem = bienesItem;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof ActivoFijoServidor)) {
            return false;
        }
        ActivoFijoServidor other = (ActivoFijoServidor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ActivoFijoServidor[ id=" + id + " ]";
    }

}
