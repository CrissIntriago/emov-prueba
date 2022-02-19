/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "ejecucion_presupuestaria", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EjecucionPresupuestaria.findAll", query = "SELECT e FROM EjecucionPresupuestaria e"),
    @NamedQuery(name = "EjecucionPresupuestaria.findById", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.id = :id"),
    @NamedQuery(name = "EjecucionPresupuestaria.findByAsientoContable", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.asientoContable = :asientoContable"),
    @NamedQuery(name = "EjecucionPresupuestaria.findByPeriodo", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.periodo = :periodo"),
    @NamedQuery(name = "EjecucionPresupuestaria.findByCatalogoPresupuesto", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.catalogoPresupuesto = :catalogoPresupuesto"),
    @NamedQuery(name = "EjecucionPresupuestaria.findByCodificado", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.codificado = :codificado"),
    @NamedQuery(name = "EjecucionPresupuestaria.findByDevengado", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.devengado = :devengado"),
    @NamedQuery(name = "EjecucionPresupuestaria.findByDiferencia", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.diferencia = :diferencia"),
    @NamedQuery(name = "EjecucionPresupuestaria.findByCuentaContable", query = "SELECT e FROM EjecucionPresupuestaria e WHERE e.cuentaContable = :cuentaContable")})
public class EjecucionPresupuestaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "asiento_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private AsientosContables asientoContable;
    @Column(name = "periodo")
    private Short periodo;
    @JoinColumn(name = "catalogo_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario catalogoPresupuesto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "codificado")
    private BigDecimal codificado;
    @Column(name = "devengado")
    private BigDecimal devengado;
    @Column(name = "diferencia")
    private BigDecimal diferencia;
    @Size(max = 10)
    @Column(name = "cuenta_contable")
    private String cuentaContable;
    @Size(max = 5)
    @Column(name = "codigo")
    private String codigo;

    public EjecucionPresupuestaria() {
    }

    public EjecucionPresupuestaria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public AsientosContables getAsientoContable() {
        return asientoContable;
    }

    public void setAsientoContable(AsientosContables asientoContable) {
        this.asientoContable = asientoContable;
    }

    public PresCatalogoPresupuestario getCatalogoPresupuesto() {
        return catalogoPresupuesto;
    }

    public void setCatalogoPresupuesto(PresCatalogoPresupuestario catalogoPresupuesto) {
        this.catalogoPresupuesto = catalogoPresupuesto;
    }

    public BigDecimal getCodificado() {
        return codificado;
    }

    public void setCodificado(BigDecimal codificado) {
        this.codificado = codificado;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        if (!(object instanceof EjecucionPresupuestaria)) {
            return false;
        }
        EjecucionPresupuestaria other = (EjecucionPresupuestaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.entities.EjecucionPresupuestaria[ id=" + id + " ]";
    }

}
