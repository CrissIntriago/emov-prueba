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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_valor_inicial_emision", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenValorInicialEmision.findAll", query = "SELECT r FROM RenValorInicialEmision r"),
    @NamedQuery(name = "RenValorInicialEmision.findById", query = "SELECT r FROM RenValorInicialEmision r WHERE r.id = :id"),
    @NamedQuery(name = "RenValorInicialEmision.findByAnio", query = "SELECT r FROM RenValorInicialEmision r WHERE r.anio = :anio"),
    @NamedQuery(name = "RenValorInicialEmision.findByEstado", query = "SELECT r FROM RenValorInicialEmision r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenValorInicialEmision.findByRubroLiquidacion", query = "SELECT r FROM RenValorInicialEmision r WHERE r.rubroLiquidacion = :rubroLiquidacion"),
    @NamedQuery(name = "RenValorInicialEmision.findByValorInicial", query = "SELECT r FROM RenValorInicialEmision r WHERE r.valorInicial = :valorInicial")})
public class RenValorInicialEmision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private BigInteger anio;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "rubro_liquidacion")
    private BigInteger rubroLiquidacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_inicial")
    private BigDecimal valorInicial;

    public RenValorInicialEmision() {
    }

    public RenValorInicialEmision(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigInteger getRubroLiquidacion() {
        return rubroLiquidacion;
    }

    public void setRubroLiquidacion(BigInteger rubroLiquidacion) {
        this.rubroLiquidacion = rubroLiquidacion;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
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
        if (!(object instanceof RenValorInicialEmision)) {
            return false;
        }
        RenValorInicialEmision other = (RenValorInicialEmision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenValorInicialEmision[ id=" + id + " ]";
    }
    
}
