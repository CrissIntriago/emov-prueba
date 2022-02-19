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
@Table(name = "prestamos_activos_bienes", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "PrestamosActivosBienes.findAll", query = "SELECT p FROM PrestamosActivosBienes p"),
    @NamedQuery(name = "PrestamosActivosBienes.findById", query = "SELECT p FROM PrestamosActivosBienes p WHERE p.id = :id"),
    @NamedQuery(name = "PrestamosActivosBienes.findByComponente", query = "SELECT p FROM PrestamosActivosBienes p WHERE p.componente = :componente"),
    @NamedQuery(name = "PrestamosActivosBienes.findByEstado", query = "SELECT p FROM PrestamosActivosBienes p WHERE p.estado = :estado"),
    @NamedQuery(name = "PrestamosActivosBienes.findByFechaPrestamo", query = "SELECT p FROM PrestamosActivosBienes p WHERE p.fechaPrestamo = :fechaPrestamo")})
public class PrestamosActivosBienes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "componente")
    private Boolean componente;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_prestamo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrestamo;
    @JoinColumn(name = "activo_fijo_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ActivoFijoServidor activoFijoServidor;
    @JoinColumn(name = "bienes_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem bienesItem;
    @JoinColumn(name = "grupo_padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem grupoPadre;
    @JoinColumn(name = "prestamos_activos", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PrestamosActivos prestamosActivos;

    public PrestamosActivosBienes() {
        this.estado = Boolean.TRUE;
    }

    public PrestamosActivosBienes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getComponente() {
        return componente;
    }

    public void setComponente(Boolean componente) {
        this.componente = componente;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public ActivoFijoServidor getActivoFijoServidor() {
        return activoFijoServidor;
    }

    public void setActivoFijoServidor(ActivoFijoServidor activoFijoServidor) {
        this.activoFijoServidor = activoFijoServidor;
    }

    public BienesItem getBienesItem() {
        return bienesItem;
    }

    public void setBienesItem(BienesItem bienesItem) {
        this.bienesItem = bienesItem;
    }

    public BienesItem getGrupoPadre() {
        return grupoPadre;
    }

    public void setGrupoPadre(BienesItem grupoPadre) {
        this.grupoPadre = grupoPadre;
    }

    public PrestamosActivos getPrestamosActivos() {
        return prestamosActivos;
    }

    public void setPrestamosActivos(PrestamosActivos prestamosActivos) {
        this.prestamosActivos = prestamosActivos;
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
        if (!(object instanceof PrestamosActivosBienes)) {
            return false;
        }
        PrestamosActivosBienes other = (PrestamosActivosBienes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.PrestamosActivosBienes[ id=" + id + " ]";
    }

}
