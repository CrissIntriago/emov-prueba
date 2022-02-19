/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 *
 * @author Luis Pozo
 */
@Entity
@Table(name = "escala_salarial", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "EscalaSalarial.findAll", query = "SELECT e FROM EscalaSalarial e"),
    @NamedQuery(name = "EscalaSalarial.findById", query = "SELECT e FROM EscalaSalarial e WHERE e.id = :id"),
    @NamedQuery(name = "EscalaSalarial.findByGrupoOrganizacional", query = "SELECT e FROM EscalaSalarial e WHERE e.grupoOrganizacional = :grupoOrganizacional"),
    @NamedQuery(name = "EscalaSalarial.findByRemuneracionDolares", query = "SELECT e FROM EscalaSalarial e WHERE e.remuneracionDolares = :remuneracionDolares"),
    @NamedQuery(name = "EscalaSalarial.findByGrado", query = "SELECT e FROM EscalaSalarial e WHERE e.grado = :grado"),
    @NamedQuery(name = "EscalaSalarial.findByEstado", query = "SELECT e FROM EscalaSalarial e WHERE e.estado = TRUE"),
    @NamedQuery(name = "EscalaSalarial.findByFechaCreacion", query = "SELECT e FROM EscalaSalarial e WHERE e.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "EscalaSalarial.findByUsuarioCreacion", query = "SELECT e FROM EscalaSalarial e WHERE e.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "EscalaSalarial.findByFechaModificacion", query = "SELECT e FROM EscalaSalarial e WHERE e.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "EscalaSalarial.findByEscalasVigentes", query = "SELECT e FROM EscalaSalarial e WHERE e.estado = true AND ( e.hasta >= CURRENT_DATE OR e.hasta IS NULL ) "),
    @NamedQuery(name = "EscalaSalarial.findByUsuarioModifica", query = "SELECT e FROM EscalaSalarial e WHERE e.usuarioModifica = :usuarioModifica")})
public class EscalaSalarial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Size(min = 1, max = 250)
    @Column(name = "grupo_organizacional")
    private String grupoOrganizacional;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "remuneracion_dolares")
    private BigDecimal remuneracionDolares;
    @Column(name = "grado")
    private BigInteger grado;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date desde;
    @Column(name = "hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hasta;

    public EscalaSalarial() {
        this.estado = Boolean.TRUE;
    }

    public EscalaSalarial(Long id) {
        this.id = id;
    }

    public EscalaSalarial(Long id, String grupoOrganizacional, BigDecimal remuneracionDolares, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica, Date desde, Date hasta) {
        this.hasta = hasta;
        this.desde = desde;
        this.id = id;
        this.grupoOrganizacional = grupoOrganizacional;
        this.remuneracionDolares = remuneracionDolares;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupoOrganizacional() {
        return grupoOrganizacional;
    }

    public void setGrupoOrganizacional(String grupoOrganizacional) {
        this.grupoOrganizacional = grupoOrganizacional;
    }

    public BigDecimal getRemuneracionDolares() {
        return remuneracionDolares;
    }

    public void setRemuneracionDolares(BigDecimal remuneracionDolares) {
        this.remuneracionDolares = remuneracionDolares;
    }

    public BigInteger getGrado() {
        return grado;
    }

    public void setGrado(BigInteger grado) {
        this.grado = grado;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
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

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
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
        if (!(object instanceof EscalaSalarial)) {
            return false;
        }
        EscalaSalarial other = (EscalaSalarial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.talentohumano.lazy.EscalaSalarial[ id=" + id + " ]";
    }

}
