/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "ren_activos_local_comercial", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenActivosLocalComercial.findAll", query = "SELECT r FROM RenActivosLocalComercial r"),
    @NamedQuery(name = "RenActivosLocalComercial.findById", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.id = :id"),
    @NamedQuery(name = "RenActivosLocalComercial.findByActivoContingente", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.activoContingente = :activoContingente"),
    @NamedQuery(name = "RenActivosLocalComercial.findByActivoTotal", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.activoTotal = :activoTotal"),
    @NamedQuery(name = "RenActivosLocalComercial.findByAnioBalance", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.anioBalance = :anioBalance"),
    @NamedQuery(name = "RenActivosLocalComercial.findByEstado", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenActivosLocalComercial.findByFechaIngreso", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RenActivosLocalComercial.findByLocalComercial", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.localComercial = :localComercial"),
    @NamedQuery(name = "RenActivosLocalComercial.findByNumLiquidacion", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.numLiquidacion = :numLiquidacion"),
    @NamedQuery(name = "RenActivosLocalComercial.findByPasivoContingente", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.pasivoContingente = :pasivoContingente"),
    @NamedQuery(name = "RenActivosLocalComercial.findByPasivoTotal", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.pasivoTotal = :pasivoTotal"),
    @NamedQuery(name = "RenActivosLocalComercial.findByPermiso", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.permiso = :permiso"),
    @NamedQuery(name = "RenActivosLocalComercial.findByPorcentajeIngreso", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.porcentajeIngreso = :porcentajeIngreso"),
    @NamedQuery(name = "RenActivosLocalComercial.findByUsuarioIngreso", query = "SELECT r FROM RenActivosLocalComercial r WHERE r.usuarioIngreso = :usuarioIngreso")})
public class RenActivosLocalComercial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "activo_contingente")
    private BigDecimal activoContingente;
    @Column(name = "activo_total")
    private BigDecimal activoTotal;
    @Column(name = "anio_balance")
    private Integer anioBalance;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "local_comercial")
    private BigInteger localComercial;
    @Column(name = "num_liquidacion")
    private BigInteger numLiquidacion;
    @Size(max = 2147483647)
    @Column(name = "pasivo_contingente")
    private BigDecimal pasivoContingente;
    @Column(name = "pasivo_total")
    private BigDecimal pasivoTotal;
    @Column(name = "permiso")
    private BigInteger permiso;
    @Column(name = "porcentaje_ingreso")
    private BigDecimal porcentajeIngreso;
    @Size(max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;

    public RenActivosLocalComercial() {
    }

    public RenActivosLocalComercial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getActivoContingente() {
        return activoContingente;
    }

    public void setActivoContingente(BigDecimal activoContingente) {
        this.activoContingente = activoContingente;
    }

    public BigDecimal getActivoTotal() {
        return activoTotal;
    }

    public void setActivoTotal(BigDecimal activoTotal) {
        this.activoTotal = activoTotal;
    }

    public Integer getAnioBalance() {
        return anioBalance;
    }

    public void setAnioBalance(Integer anioBalance) {
        this.anioBalance = anioBalance;
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

    public BigInteger getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(BigInteger localComercial) {
        this.localComercial = localComercial;
    }

    public BigInteger getNumLiquidacion() {
        return numLiquidacion;
    }

    public void setNumLiquidacion(BigInteger numLiquidacion) {
        this.numLiquidacion = numLiquidacion;
    }

    public BigDecimal getPasivoContingente() {
        return pasivoContingente;
    }

    public void setPasivoContingente(BigDecimal pasivoContingente) {
        this.pasivoContingente = pasivoContingente;
    }

    public BigDecimal getPasivoTotal() {
        return pasivoTotal;
    }

    public void setPasivoTotal(BigDecimal pasivoTotal) {
        this.pasivoTotal = pasivoTotal;
    }

    public BigInteger getPermiso() {
        return permiso;
    }

    public void setPermiso(BigInteger permiso) {
        this.permiso = permiso;
    }

    public BigDecimal getPorcentajeIngreso() {
        return porcentajeIngreso;
    }

    public void setPorcentajeIngreso(BigDecimal porcentajeIngreso) {
        this.porcentajeIngreso = porcentajeIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
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
        if (!(object instanceof RenActivosLocalComercial)) {
            return false;
        }
        RenActivosLocalComercial other = (RenActivosLocalComercial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenActivosLocalComercial[ id=" + id + " ]";
    }
    
}
