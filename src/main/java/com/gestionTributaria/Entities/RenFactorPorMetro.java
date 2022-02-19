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
@Table(name = "ren_factor_por_metro",schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenFactorPorMetro.findAll", query = "SELECT r FROM RenFactorPorMetro r"),
    @NamedQuery(name = "RenFactorPorMetro.findById", query = "SELECT r FROM RenFactorPorMetro r WHERE r.id = :id"),
    @NamedQuery(name = "RenFactorPorMetro.findByAnioDesde", query = "SELECT r FROM RenFactorPorMetro r WHERE r.anioDesde = :anioDesde"),
    @NamedQuery(name = "RenFactorPorMetro.findByAnioHasta", query = "SELECT r FROM RenFactorPorMetro r WHERE r.anioHasta = :anioHasta"),
    @NamedQuery(name = "RenFactorPorMetro.findByDesde", query = "SELECT r FROM RenFactorPorMetro r WHERE r.desde = :desde"),
    @NamedQuery(name = "RenFactorPorMetro.findByHasta", query = "SELECT r FROM RenFactorPorMetro r WHERE r.hasta = :hasta"),
    @NamedQuery(name = "RenFactorPorMetro.findByFraccion", query = "SELECT r FROM RenFactorPorMetro r WHERE r.fraccion = :fraccion"),
    @NamedQuery(name = "RenFactorPorMetro.findByValor", query = "SELECT r FROM RenFactorPorMetro r WHERE r.valor = :valor"),
    @NamedQuery(name = "RenFactorPorMetro.findBySalarioBasico", query = "SELECT r FROM RenFactorPorMetro r WHERE r.salarioBasico = :salarioBasico"),
    @NamedQuery(name = "RenFactorPorMetro.findByPorcentajeSalarioBasico", query = "SELECT r FROM RenFactorPorMetro r WHERE r.porcentajeSalarioBasico = :porcentajeSalarioBasico"),
    @NamedQuery(name = "RenFactorPorMetro.findByEstado", query = "SELECT r FROM RenFactorPorMetro r WHERE r.estado = :estado")})
public class RenFactorPorMetro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio_desde")
    private Integer anioDesde;
    @Column(name = "anio_hasta")
    private Integer anioHasta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "desde")
    private BigDecimal desde;
    @Column(name = "hasta")
    private BigDecimal hasta;
    @Column(name = "fraccion")
    private BigDecimal fraccion;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "salario_basico")
    private BigDecimal salarioBasico;
    @Column(name = "porcentaje_salario_basico")
    private BigDecimal porcentajeSalarioBasico;
    @Column(name = "estado")
    private Boolean estado;

    public RenFactorPorMetro() {
    }

    public RenFactorPorMetro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer anioHasta) {
        this.anioHasta = anioHasta;
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

    public BigDecimal getFraccion() {
        return fraccion;
    }

    public void setFraccion(BigDecimal fraccion) {
        this.fraccion = fraccion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSalarioBasico() {
        return salarioBasico;
    }

    public void setSalarioBasico(BigDecimal salarioBasico) {
        this.salarioBasico = salarioBasico;
    }

    public BigDecimal getPorcentajeSalarioBasico() {
        return porcentajeSalarioBasico;
    }

    public void setPorcentajeSalarioBasico(BigDecimal porcentajeSalarioBasico) {
        this.porcentajeSalarioBasico = porcentajeSalarioBasico;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof RenFactorPorMetro)) {
            return false;
        }
        RenFactorPorMetro other = (RenFactorPorMetro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.RenFactorPorMetro[ id=" + id + " ]";
    }
    
}
