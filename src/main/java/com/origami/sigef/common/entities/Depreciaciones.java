/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "depreciaciones", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "Depreciaciones.findAll", query = "SELECT d FROM Depreciaciones d"),
    @NamedQuery(name = "Depreciaciones.findById", query = "SELECT d FROM Depreciaciones d WHERE d.id = :id"),
    @NamedQuery(name = "Depreciaciones.findByValorResidual", query = "SELECT d FROM Depreciaciones d WHERE d.valorResidual = :valorResidual"),
    @NamedQuery(name = "Depreciaciones.findByDepreciacionAcumulada", query = "SELECT d FROM Depreciaciones d WHERE d.depreciacionAcumulada = :depreciacionAcumulada"),
    @NamedQuery(name = "Depreciaciones.findByFechaDepreciacion", query = "SELECT d FROM Depreciaciones d WHERE d.fechaDepreciacion = :fechaDepreciacion"),
    @NamedQuery(name = "Depreciaciones.findByEstado", query = "SELECT d FROM Depreciaciones d WHERE d.estado = :estado"),
    @NamedQuery(name = "Depreciaciones.findByTotalValorContable", query = "SELECT d FROM Depreciaciones d WHERE d.totalValorContable = :totalValorContable"),
    @NamedQuery(name = "Depreciaciones.findByTotalValorLibro", query = "SELECT d FROM Depreciaciones d WHERE d.totalValorLibro = :totalValorLibro")})
public class Depreciaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_residual")
    private BigDecimal valorResidual;
    @Column(name = "depreciacion_acumulada")
    private BigDecimal depreciacionAcumulada;
    @Column(name = "fecha_depreciacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDepreciacion;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem bien;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;
    @Column(name = "total_valor_contable")
    private BigDecimal totalValorContable;
    @Column(name = "total_valor_libro")
    private BigDecimal totalValorLibro;

    public Depreciaciones() {
        this.estado = Boolean.TRUE;
    }

    public Depreciaciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorResidual() {
        return valorResidual;
    }

    public void setValorResidual(BigDecimal valorResidual) {
        this.valorResidual = valorResidual;
    }

    public BigDecimal getDepreciacionAcumulada() {
        return depreciacionAcumulada;
    }

    public void setDepreciacionAcumulada(BigDecimal depreciacionAcumulada) {
        this.depreciacionAcumulada = depreciacionAcumulada;
    }

    public Date getFechaDepreciacion() {
        return fechaDepreciacion;
    }

    public void setFechaDepreciacion(Date fechaDepreciacion) {
        this.fechaDepreciacion = fechaDepreciacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BienesItem getBien() {
        return bien;
    }

    public void setBien(BienesItem bien) {
        this.bien = bien;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public BigDecimal getTotalValorContable() {
        return totalValorContable;
    }

    public void setTotalValorContable(BigDecimal totalValorContable) {
        this.totalValorContable = totalValorContable;
    }

    public BigDecimal getTotalValorLibro() {
        return totalValorLibro;
    }

    public void setTotalValorLibro(BigDecimal totalValorLibro) {
        this.totalValorLibro = totalValorLibro;
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
        if (!(object instanceof Depreciaciones)) {
            return false;
        }
        Depreciaciones other = (Depreciaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Depreciaciones[ id=" + id + " ]";
    }

}
