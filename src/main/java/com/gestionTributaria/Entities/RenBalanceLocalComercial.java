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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_balance_local_comercial", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenBalanceLocalComercial.findAll", query = "SELECT r FROM RenBalanceLocalComercial r"),
    @NamedQuery(name = "RenBalanceLocalComercial.findById", query = "SELECT r FROM RenBalanceLocalComercial r WHERE r.id = :id"),
    @NamedQuery(name = "RenBalanceLocalComercial.findByAnioBalance", query = "SELECT r FROM RenBalanceLocalComercial r WHERE r.anioBalance = :anioBalance"),
    @NamedQuery(name = "RenBalanceLocalComercial.findByCapital", query = "SELECT r FROM RenBalanceLocalComercial r WHERE r.capital = :capital"),
    @NamedQuery(name = "RenBalanceLocalComercial.findByFechaBalance", query = "SELECT r FROM RenBalanceLocalComercial r WHERE r.fechaBalance = :fechaBalance"),
    @NamedQuery(name = "RenBalanceLocalComercial.findByEstado", query = "SELECT r FROM RenBalanceLocalComercial r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenBalanceLocalComercial.findByLocalComercial", query = "SELECT r FROM RenBalanceLocalComercial r WHERE r.localComercial = :localComercial"),
    @NamedQuery(name = "RenBalanceLocalComercial.findByNumLiquidacion", query = "SELECT r FROM RenBalanceLocalComercial r WHERE r.numLiquidacion = :numLiquidacion")})
public class RenBalanceLocalComercial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio_balance")
    private Integer anioBalance;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capital")
    private BigDecimal capital;
    @Column(name = "fecha_balance")
    @Temporal(TemporalType.DATE)
    private Date fechaBalance;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "local_comercial")
    private BigInteger localComercial;
    @Column(name = "num_liquidacion")
    private BigInteger numLiquidacion;

    public RenBalanceLocalComercial() {
    }

    public RenBalanceLocalComercial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioBalance() {
        return anioBalance;
    }

    public void setAnioBalance(Integer anioBalance) {
        this.anioBalance = anioBalance;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public Date getFechaBalance() {
        return fechaBalance;
    }

    public void setFechaBalance(Date fechaBalance) {
        this.fechaBalance = fechaBalance;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RenBalanceLocalComercial)) {
            return false;
        }
        RenBalanceLocalComercial other = (RenBalanceLocalComercial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenBalanceLocalComercial[ id=" + id + " ]";
    }
    
}
