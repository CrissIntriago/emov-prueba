/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Administrator
 */
@Entity
@Table(name = "ren_factor_por_capital", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenFactorPorCapital.findAll", query = "SELECT r FROM RenFactorPorCapital r"),
    @NamedQuery(name = "RenFactorPorCapital.findById", query = "SELECT r FROM RenFactorPorCapital r WHERE r.id = :id"),
    @NamedQuery(name = "RenFactorPorCapital.findByDesde", query = "SELECT r FROM RenFactorPorCapital r WHERE r.desde = :desde"),
    @NamedQuery(name = "RenFactorPorCapital.findByHasta", query = "SELECT r FROM RenFactorPorCapital r WHERE r.hasta = :hasta"),
    @NamedQuery(name = "RenFactorPorCapital.findByValor", query = "SELECT r FROM RenFactorPorCapital r WHERE r.valor = :valor"),
    @NamedQuery(name = "RenFactorPorCapital.findByExcedente", query = "SELECT r FROM RenFactorPorCapital r WHERE r.excedente = :excedente"),
    @NamedQuery(name = "RenFactorPorCapital.findByEstado", query = "SELECT r FROM RenFactorPorCapital r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenFactorPorCapital.findByAplicaPorcentaje", query = "SELECT r FROM RenFactorPorCapital r WHERE r.aplicaPorcentaje = :aplicaPorcentaje")})
public class RenFactorPorCapital implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "desde")
    private BigDecimal desde;
    @Column(name = "hasta")
    private BigDecimal hasta;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "excedente")
    private BigDecimal excedente;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "aplica_porcentaje")
    private Boolean aplicaPorcentaje;

    public RenFactorPorCapital() {
    }

    public RenFactorPorCapital(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDesde() {
        return desde;
    }

    public void setDesde(BigDecimal desde) {
        this.desde = desde;
    }

    public BigDecimal getHasta() {
        return hasta;
    }

    public void setHasta(BigDecimal hasta) {
        this.hasta = hasta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getExcedente() {
        return excedente;
    }

    public void setExcedente(BigDecimal excedente) {
        this.excedente = excedente;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getAplicaPorcentaje() {
        return aplicaPorcentaje;
    }

    public void setAplicaPorcentaje(Boolean aplicaPorcentaje) {
        this.aplicaPorcentaje = aplicaPorcentaje;
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
        if (!(object instanceof RenFactorPorCapital)) {
            return false;
        }
        RenFactorPorCapital other = (RenFactorPorCapital) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.RenFactorPorCapital[ id=" + id + " ]";
    }

}
