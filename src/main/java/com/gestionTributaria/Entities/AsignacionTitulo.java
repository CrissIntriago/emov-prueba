/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.entities.Usuarios;
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

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "asignacion_titulo", schema = "comprobantes_electronicos")
@NamedQueries({
    @NamedQuery(name = "AsignacionTitulo.findAll", query = "SELECT a FROM AsignacionTitulo a")
    , @NamedQuery(name = "AsignacionTitulo.findById", query = "SELECT a FROM AsignacionTitulo a WHERE a.id = :id")
    , @NamedQuery(name = "AsignacionTitulo.findByDescripcion", query = "SELECT a FROM AsignacionTitulo a WHERE a.descripcion = :descripcion")
    , @NamedQuery(name = "AsignacionTitulo.findBySecInicio", query = "SELECT a FROM AsignacionTitulo a WHERE a.secInicio = :secInicio")
    , @NamedQuery(name = "AsignacionTitulo.findBySecFin", query = "SELECT a FROM AsignacionTitulo a WHERE a.secFin = :secFin")
    , @NamedQuery(name = "AsignacionTitulo.findByTotalTitulo", query = "SELECT a FROM AsignacionTitulo a WHERE a.totalTitulo = :totalTitulo")
    , @NamedQuery(name = "AsignacionTitulo.findByTotalTituloUsado", query = "SELECT a FROM AsignacionTitulo a WHERE a.totalTituloUsado = :totalTituloUsado")
    , @NamedQuery(name = "AsignacionTitulo.findByUsuario", query = "SELECT a FROM AsignacionTitulo a WHERE a.usuario = :usuario")
    , @NamedQuery(name = "AsignacionTitulo.findByEstado", query = "SELECT a FROM AsignacionTitulo a WHERE a.estado = :estado")
    , @NamedQuery(name = "AsignacionTitulo.findByPuntoEmision", query = "SELECT a FROM AsignacionTitulo a WHERE a.puntoEmision = :puntoEmision")
    , @NamedQuery(name = "AsignacionTitulo.findByFechaCreacion", query = "SELECT a FROM AsignacionTitulo a WHERE a.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "AsignacionTitulo.findByFechaModificacion", query = "SELECT a FROM AsignacionTitulo a WHERE a.fechaModificacion = :fechaModificacion")
    , @NamedQuery(name = "AsignacionTitulo.findByUsuarioCreacion", query = "SELECT a FROM AsignacionTitulo a WHERE a.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "AsignacionTitulo.findByUsuarioModificacion", query = "SELECT a FROM AsignacionTitulo a WHERE a.usuarioModificacion = :usuarioModificacion")})
public class AsignacionTitulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "descripcion", length = 2147483647)
    private String descripcion;
    @Column(name = "sec_inicio")
    private BigInteger secInicio;
    @Column(name = "sec_fin")
    private BigInteger secFin;
    @Column(name = "total_titulo")
    private BigInteger totalTitulo;
    @Column(name = "total_titulo_usado")
    private BigInteger totalTituloUsado;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuario;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "punto_emision", length = 2147483647)
    private String puntoEmision;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion", length = 2147483647)
    private String usuarioCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion", length = 2147483647)
    private String usuarioModificacion;

    public AsignacionTitulo() {
        this.estado = Boolean.TRUE;
        this.totalTitulo = BigInteger.ZERO;
        this.totalTituloUsado = BigInteger.ZERO;
    }

    public AsignacionTitulo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getSecInicio() {
        return secInicio;
    }

    public void setSecInicio(BigInteger secInicio) {
        this.secInicio = secInicio;
    }

    public BigInteger getSecFin() {
        return secFin;
    }

    public void setSecFin(BigInteger secFin) {
        this.secFin = secFin;
    }

    public BigInteger getTotalTitulo() {
        return totalTitulo;
    }

    public void setTotalTitulo(BigInteger totalTitulo) {
        this.totalTitulo = totalTitulo;
    }

    public BigInteger getTotalTituloUsado() {
        return totalTituloUsado;
    }

    public void setTotalTituloUsado(BigInteger totalTituloUsado) {
        this.totalTituloUsado = totalTituloUsado;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
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
        if (!(object instanceof AsignacionTitulo)) {
            return false;
        }
        AsignacionTitulo other = (AsignacionTitulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mapEntity.AsignacionTitulo[ id=" + id + " ]";
    }

}
