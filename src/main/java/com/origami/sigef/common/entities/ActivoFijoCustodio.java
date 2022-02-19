/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import javax.persistence.FetchType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "activo_fijo_custodio", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "ActivoFijoCustodio.findAll", query = "SELECT a FROM ActivoFijoCustodio a"),
    @NamedQuery(name = "ActivoFijoCustodio.findById", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.id = :id"),
    @NamedQuery(name = "ActivoFijoCustodio.findByDescripcion", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "ActivoFijoCustodio.findByCantidadBienes", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.cantidadBienes = :cantidadBienes"),
    @NamedQuery(name = "ActivoFijoCustodio.findByFechaCreacion", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ActivoFijoCustodio.findByUsuarioCreacion", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ActivoFijoCustodio.findByFechaModificacion", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ActivoFijoCustodio.findByUsuarioModificacion", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ActivoFijoCustodio.findByEstado", query = "SELECT a FROM ActivoFijoCustodio a WHERE a.estado = :estado")})
public class ActivoFijoCustodio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "cantidad_bienes")
    private short cantidadBienes;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @OneToMany(mappedBy = "activoFijoCustodio", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<ActivoFijoServidor> activoFijoServidorList;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "numero_tramite")
    private BigInteger numeroTramite;
    @Column(name = "estado_proceso")
    private String estadoProceso;
    @Column(name = "numero_acta")
    private BigInteger numeroActa;
    @Column(name = "acta_guardalmacen")
    private Boolean actaGuardalmacen;

    public ActivoFijoCustodio() {
        this.estado = Boolean.TRUE;
    }

    public ActivoFijoCustodio(Long id) {
        this.id = id;
    }

    public ActivoFijoCustodio(Long id, short cantidadBienes, Date fechaCreacion, String usuarioCreacion, boolean estado) {
        this.id = id;
        this.cantidadBienes = cantidadBienes;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getCantidadBienes() {
        return cantidadBienes;
    }

    public void setCantidadBienes(short cantidadBienes) {
        this.cantidadBienes = cantidadBienes;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<ActivoFijoServidor> getActivoFijoServidorList() {
        return activoFijoServidorList;
    }

    public void setActivoFijoServidorList(List<ActivoFijoServidor> activoFijoServidorList) {
        this.activoFijoServidorList = activoFijoServidorList;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public BigInteger getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(BigInteger numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(String estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public BigInteger getNumeroActa() {
        return numeroActa;
    }

    public void setNumeroActa(BigInteger numeroActa) {
        this.numeroActa = numeroActa;
    }

    public Boolean getActaGuardalmacen() {
        return actaGuardalmacen;
    }

    public void setActaGuardalmacen(Boolean actaGuardalmacen) {
        this.actaGuardalmacen = actaGuardalmacen;
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
        if (!(object instanceof ActivoFijoCustodio)) {
            return false;
        }
        ActivoFijoCustodio other = (ActivoFijoCustodio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ActivoFijoCustodio[ id=" + id + " ]";
    }
}
