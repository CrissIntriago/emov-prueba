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
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_valor_inicial_det_liquidacion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenValorInicialDetLiquidacion.findAll", query = "SELECT r FROM RenValorInicialDetLiquidacion r"),
    @NamedQuery(name = "RenValorInicialDetLiquidacion.findById", query = "SELECT r FROM RenValorInicialDetLiquidacion r WHERE r.id = :id"),
    @NamedQuery(name = "RenValorInicialDetLiquidacion.findByRubro", query = "SELECT r FROM RenValorInicialDetLiquidacion r WHERE r.rubro = :rubro"),
    @NamedQuery(name = "RenValorInicialDetLiquidacion.findByValor", query = "SELECT r FROM RenValorInicialDetLiquidacion r WHERE r.valor = :valor")})
public class RenValorInicialDetLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "rubro")
    private BigInteger rubro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private RenValorInicialLiquidacion liquidacion;

    public RenValorInicialDetLiquidacion() {
    }

    public RenValorInicialDetLiquidacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getRubro() {
        return rubro;
    }

    public void setRubro(BigInteger rubro) {
        this.rubro = rubro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public RenValorInicialLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RenValorInicialLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
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
        if (!(object instanceof RenValorInicialDetLiquidacion)) {
            return false;
        }
        RenValorInicialDetLiquidacion other = (RenValorInicialDetLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenValorInicialDetLiquidacion[ id=" + id + " ]";
    }
    
}
