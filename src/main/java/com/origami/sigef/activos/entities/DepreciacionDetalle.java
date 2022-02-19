/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.entities;

import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;

/**
 *
 * @author jintr
 */
@Entity
@Table(name = "depreciacion_detalle", schema = "activos")
@NamedQueries({
    @NamedQuery(name = "DepreciacionDetalle.findAll", query = "SELECT d FROM DepreciacionDetalle d"),
    @NamedQuery(name = "DepreciacionDetalle.findById", query = "SELECT d FROM DepreciacionDetalle d WHERE d.id = :id"),
//    @NamedQuery(name = "DepreciacionDetalle.findByCuentaContable", query = "SELECT d FROM DepreciacionDetalle d WHERE d.cuentaContable = :cuentaContable"),
    @NamedQuery(name = "DepreciacionDetalle.findByEstado", query = "SELECT d FROM DepreciacionDetalle d WHERE d.estado = :estado"),
    @NamedQuery(name = "DepreciacionDetalle.findByValorResidual", query = "SELECT d FROM DepreciacionDetalle d WHERE d.valorResidual = :valorResidual"),
    @NamedQuery(name = "DepreciacionDetalle.findByDepreciacionAcumulada", query = "SELECT d FROM DepreciacionDetalle d WHERE d.depreciacionAcumulada = :depreciacionAcumulada"),
    @NamedQuery(name = "DepreciacionDetalle.findByValorContable", query = "SELECT d FROM DepreciacionDetalle d WHERE d.valorContable = :valorContable"),
    @NamedQuery(name = "DepreciacionDetalle.findByEstadoBien", query = "SELECT d FROM DepreciacionDetalle d WHERE d.estadoBien = :estadoBien"),
    @NamedQuery(name = "DepreciacionDetalle.findBySecuencial", query = "SELECT d FROM DepreciacionDetalle d WHERE d.secuencial = :secuencial")})
public class DepreciacionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaContable;
    @Column(name = "estado")
    private Boolean estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_residual")
    private BigDecimal valorResidual;
    @Column(name = "depreciacion_acumulada")
    private BigDecimal depreciacionAcumulada;
    @Column(name = "valor_contable")
    private BigDecimal valorContable;
    @Size(max = 2147483647)
    @Column(name = "estado_bien")
    private String estadoBien;
    @Column(name = "secuencial")
    private Integer secuencial;
    @JoinColumn(name = "id_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem idBien;
    @JoinColumn(name = "depreciacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Depreciacion depreciacion;

    public DepreciacionDetalle() {
        this.estado = Boolean.TRUE;
    }

    public DepreciacionDetalle(Long id) {
        this.id = id;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public BigDecimal getValorContable() {
        return valorContable;
    }

    public void setValorContable(BigDecimal valorContable) {
        this.valorContable = valorContable;
    }

    public String getEstadoBien() {
        return estadoBien;
    }

    public void setEstadoBien(String estadoBien) {
        this.estadoBien = estadoBien;
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public BienesItem getIdBien() {
        return idBien;
    }

    public void setIdBien(BienesItem idBien) {
        this.idBien = idBien;
    }

    public Depreciacion getDepreciacion() {
        return depreciacion;
    }

    public void setDepreciacion(Depreciacion depreciacion) {
        this.depreciacion = depreciacion;
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
        if (!(object instanceof DepreciacionDetalle)) {
            return false;
        }
        DepreciacionDetalle other = (DepreciacionDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.activos.entities.DepreciacionDetalle[ id=" + id + " ]";
    }

}
