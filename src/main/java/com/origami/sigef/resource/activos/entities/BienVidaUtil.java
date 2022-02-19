/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandra Arroba S
 */
@Entity
@Table(name = "bien_vida_util", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BienVidaUtil.findAll", query = "SELECT v FROM BienVidaUtil v"),
    @NamedQuery(name = "BienVidaUtil.findById", query = "SELECT v FROM BienVidaUtil v WHERE v.id = :id"),
    @NamedQuery(name = "BienVidaUtil.findByDescripcion", query = "SELECT v FROM BienVidaUtil v WHERE v.descripcion = :descripcion"),
    @NamedQuery(name = "BienVidaUtil.findByVidaUtil", query = "SELECT v FROM BienVidaUtil v WHERE v.vidaUtil = :vidaUtil"),
    @NamedQuery(name = "BienVidaUtil.findByPeriodo", query = "SELECT v FROM BienVidaUtil v WHERE v.periodo = :periodo"),
    @NamedQuery(name = "BienVidaUtil.findByFechaCreacion", query = "SELECT v FROM BienVidaUtil v WHERE v.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "BienVidaUtil.findByUsuarioCreador", query = "SELECT v FROM BienVidaUtil v WHERE v.usuarioCreador = :usuarioCreador"),
    @NamedQuery(name = "BienVidaUtil.findByFechaModificacion", query = "SELECT v FROM BienVidaUtil v WHERE v.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "BienVidaUtil.findByUsuarioModifica", query = "SELECT v FROM BienVidaUtil v WHERE v.usuarioModifica = :usuarioModifica")})
public class BienVidaUtil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "cuentas_contables", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaContable;
    @JoinColumn(name = "cta_depreciacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas ctaDepreciacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "vida_util")
    private Short vidaUtil;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creador")
    private String usuarioCreador;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;

    public BienVidaUtil() {
    }

    public BienVidaUtil(Long id) {
        this.id = id;
    }

    public BienVidaUtil(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public ContCuentas getCtaDepreciacion() {
        return ctaDepreciacion;
    }

    public void setCtaDepreciacion(ContCuentas ctaDepreciacion) {
        this.ctaDepreciacion = ctaDepreciacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(Short vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
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
        if (!(object instanceof BienVidaUtil)) {
            return false;
        }
        BienVidaUtil other = (BienVidaUtil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.activos.entities.BienVidaUtil[ id=" + id + " ]";
    }

}
