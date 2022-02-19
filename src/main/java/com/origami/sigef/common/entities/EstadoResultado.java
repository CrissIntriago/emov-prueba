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
@Table(name = "estado_resultado", schema = "contabilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoResultado.findAll", query = "SELECT e FROM EstadoResultado e")
    , @NamedQuery(name = "EstadoResultado.findById", query = "SELECT e FROM EstadoResultado e WHERE e.id = :id")
    , @NamedQuery(name = "EstadoResultado.findByValorAnioActual", query = "SELECT e FROM EstadoResultado e WHERE e.valorAnioActual = :valorAnioActual")
    , @NamedQuery(name = "EstadoResultado.findByValorAnioAnterior", query = "SELECT e FROM EstadoResultado e WHERE e.valorAnioAnterior = :valorAnioAnterior")})
public class EstadoResultado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_anio_actual", precision = 19, scale = 2)
    private BigDecimal valorAnioActual;
    @Column(name = "valor_anio_anterior", precision = 19, scale = 2)
    private BigDecimal valorAnioAnterior;
    @JoinColumn(name = "asiento_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private AsientosContables asientoContable;

    public EstadoResultado() {
    }

    public EstadoResultado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorAnioActual() {
        return valorAnioActual;
    }

    public void setValorAnioActual(BigDecimal valorAnioActual) {
        this.valorAnioActual = valorAnioActual;
    }

    public BigDecimal getValorAnioAnterior() {
        return valorAnioAnterior;
    }

    public void setValorAnioAnterior(BigDecimal valorAnioAnterior) {
        this.valorAnioAnterior = valorAnioAnterior;
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
        if (!(object instanceof EstadoResultado)) {
            return false;
        }
        EstadoResultado other = (EstadoResultado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Reportes.EstadoSituacionFinanciera[ id=" + id + " ]";
    }

}

