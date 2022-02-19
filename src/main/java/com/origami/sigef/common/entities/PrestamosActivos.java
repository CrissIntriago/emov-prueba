/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "prestamos_activos", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "PrestamosActivos.findAll", query = "SELECT p FROM PrestamosActivos p"),
    @NamedQuery(name = "PrestamosActivos.findById", query = "SELECT p FROM PrestamosActivos p WHERE p.id = :id"),
    @NamedQuery(name = "PrestamosActivos.findByMotivo", query = "SELECT p FROM PrestamosActivos p WHERE p.motivo = :motivo"),
    @NamedQuery(name = "PrestamosActivos.findByDescripcion", query = "SELECT p FROM PrestamosActivos p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PrestamosActivos.findByEstado", query = "SELECT p FROM PrestamosActivos p WHERE p.estado = :estado"),
    @NamedQuery(name = "PrestamosActivos.findByFechaCreacion", query = "SELECT p FROM PrestamosActivos p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PrestamosActivos.findByUsuarioCreacion", query = "SELECT p FROM PrestamosActivos p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PrestamosActivos.findByFechaModificacion", query = "SELECT p FROM PrestamosActivos p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "PrestamosActivos.findByUsuarioModificacion", query = "SELECT p FROM PrestamosActivos p WHERE p.usuarioModificacion = :usuarioModificacion")})
public class PrestamosActivos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prestamosActivos")
    private List<PrestamosActivosBienes> prestamosActivosBienesList;
    @JoinColumn(name = "custodio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ActivoFijoCustodio custodio;
    @Column(name = "descripcion_devolucion")
    private String descripcionDevolucion;
    @Column(name = "estado_prestamo")
    private Boolean estadoPrestamo;
    @Column(name = "fecha_devolucion_comodato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucionComodato;

    public PrestamosActivos() {
        this.estado = Boolean.TRUE;
        this.estadoPrestamo = Boolean.FALSE;
    }

    public PrestamosActivos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public List<PrestamosActivosBienes> getPrestamosActivosBienesList() {
        return prestamosActivosBienesList;
    }

    public void setPrestamosActivosBienesList(List<PrestamosActivosBienes> prestamosActivosBienesList) {
        this.prestamosActivosBienesList = prestamosActivosBienesList;
    }

    public ActivoFijoCustodio getCustodio() {
        return custodio;
    }

    public void setCustodio(ActivoFijoCustodio custodio) {
        this.custodio = custodio;
    }

    public String getDescripcionDevolucion() {
        return descripcionDevolucion;
    }

    public void setDescripcionDevolucion(String descripcionDevolucion) {
        this.descripcionDevolucion = descripcionDevolucion;
    }

    public Boolean getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public void setEstadoPrestamo(Boolean estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
    }

    public Date getFechaDevolucionComodato() {
        return fechaDevolucionComodato;
    }

    public void setFechaDevolucionComodato(Date fechaDevolucionComodato) {
        this.fechaDevolucionComodato = fechaDevolucionComodato;
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
        if (!(object instanceof PrestamosActivos)) {
            return false;
        }
        PrestamosActivos other = (PrestamosActivos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.PrestamosActivos[ id=" + id + " ]";
    }

}
