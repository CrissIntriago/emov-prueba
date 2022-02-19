/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "flujo_efectivo", schema = "contabilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlujoEfectivo.findAll", query = "SELECT f FROM FlujoEfectivo f")
    , @NamedQuery(name = "FlujoEfectivo.findById", query = "SELECT f FROM FlujoEfectivo f WHERE f.id = :id")
    , @NamedQuery(name = "FlujoEfectivo.findByAnioActual", query = "SELECT f FROM FlujoEfectivo f WHERE f.anioActual = :anioActual")
    , @NamedQuery(name = "FlujoEfectivo.findByAnioAnterior", query = "SELECT f FROM FlujoEfectivo f WHERE f.anioAnterior = :anioAnterior")})
public class FlujoEfectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "anio_actual", precision = 19, scale = 2)
    private BigDecimal anioActual;
    @Column(name = "anio_anterior", precision = 19, scale = 2)
    private BigDecimal anioAnterior;
    @JoinColumn(name = "asiento_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private AsientosContables asientoContable;

    public FlujoEfectivo() {
    }

    public FlujoEfectivo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAnioActual() {
        return anioActual;
    }

    public void setAnioActual(BigDecimal anioActual) {
        this.anioActual = anioActual;
    }

    public BigDecimal getAnioAnterior() {
        return anioAnterior;
    }

    public void setAnioAnterior(BigDecimal anioAnterior) {
        this.anioAnterior = anioAnterior;
    }

    public AsientosContables getAsientoContable() {
        return asientoContable;
    }

    public void setAsientoContable(AsientosContables asientoContable) {
        this.asientoContable = asientoContable;
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
        if (!(object instanceof FlujoEfectivo)) {
            return false;
        }
        FlujoEfectivo other = (FlujoEfectivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Reportes.FlujoEfectivo[ id=" + id + " ]";
    }
    
}
