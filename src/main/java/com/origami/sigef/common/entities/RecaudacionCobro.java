/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
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

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "recaudacion_cobro", schema = "tesoreria")
@NamedQueries({
    @NamedQuery(name = "RecaudacionCobro.findAll", query = "SELECT r FROM RecaudacionCobro r")
    , @NamedQuery(name = "RecaudacionCobro.findById", query = "SELECT r FROM RecaudacionCobro r WHERE r.id = :id")
    , @NamedQuery(name = "RecaudacionCobro.findByRecaudacion", query = "SELECT r FROM RecaudacionCobro r WHERE r.recaudacion = :recaudacion")
    , @NamedQuery(name = "RecaudacionCobro.findByCorte", query = "SELECT r FROM RecaudacionCobro r WHERE r.corte = :corte")
    , @NamedQuery(name = "RecaudacionCobro.findBySaldoAfectar", query = "SELECT r FROM RecaudacionCobro r WHERE r.saldoAfectar = :saldoAfectar")
    , @NamedQuery(name = "RecaudacionCobro.findByNumAfectado", query = "SELECT r FROM RecaudacionCobro r WHERE r.numAfectado = :numAfectado")})
public class RecaudacionCobro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @JoinColumn(name = "recaudacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Recaudacion recaudacion;
    @JoinColumn(name = "corte", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CorteOrdenCobro corte;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_afectar")
    private BigDecimal saldoAfectar;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "num_afectado")
    private Short numAfectado;

    public RecaudacionCobro() {
    }

    public RecaudacionCobro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recaudacion getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(Recaudacion recaudacion) {
        this.recaudacion = recaudacion;
    }

    public CorteOrdenCobro getCorte() {
        return corte;
    }

    public void setCorte(CorteOrdenCobro corte) {
        this.corte = corte;
    }


    public BigDecimal getSaldoAfectar() {
        return saldoAfectar;
    }

    public void setSaldoAfectar(BigDecimal saldoAfectar) {
        this.saldoAfectar = saldoAfectar;
    }

    public Short getNumAfectado() {
        return numAfectado;
    }

    public void setNumAfectado(Short numAfectado) {
        this.numAfectado = numAfectado;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        if (!(object instanceof RecaudacionCobro)) {
            return false;
        }
        RecaudacionCobro other = (RecaudacionCobro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.RecaudacionCobro[ id=" + id + " ]";
    }
    
}
