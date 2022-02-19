/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "detalle_reforma_ingreso_suplemento", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallereformaIngresosuplemento.findAll", query = "SELECT d FROM DetalleReformaIngresoSuplemento d"),
    @NamedQuery(name = "DetallereformaIngresosuplemento.findById", query = "SELECT d FROM DetalleReformaIngresoSuplemento d WHERE d.id = :id"),
    @NamedQuery(name = "DetallereformaIngresosuplemento.findByCatalogoPresupuesto", query = "SELECT d FROM DetalleReformaIngresoSuplemento d WHERE d.catalogoPresupuesto = :catalogoPresupuesto"),
    @NamedQuery(name = "DetallereformaIngresosuplemento.findBySuplemento", query = "SELECT d FROM DetalleReformaIngresoSuplemento d WHERE d.suplemento = :suplemento"),
    @NamedQuery(name = "DetallereformaIngresosuplemento.findByReducido", query = "SELECT d FROM DetalleReformaIngresoSuplemento d WHERE d.reducido = :reducido"),
    @NamedQuery(name = "DetallereformaIngresosuplemento.findByCodificado", query = "SELECT d FROM DetalleReformaIngresoSuplemento d WHERE d.codificado = :codificado")})
public class DetalleReformaIngresoSuplemento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "catalogo_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto catalogoPresupuesto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "suplemento")
    private BigDecimal suplemento;
    @Column(name = "reducido")
    private BigDecimal reducido;
    @Column(name = "codificado")
    private BigDecimal codificado;
    @Column(name = "codigo_referencia")
    private BigInteger codigoReferencia;
    @JoinColumn(name = "reforma", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ReformaIngresoSuplemento reforma;
    @JoinColumn(name = "proforma_ingreso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ProformaIngreso proformaIngreso;

    public DetalleReformaIngresoSuplemento() {
        this.reducido = BigDecimal.ZERO;
        this.suplemento = BigDecimal.ZERO;
        this.codificado = BigDecimal.ZERO;
    }

    public DetalleReformaIngresoSuplemento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoPresupuesto getCatalogoPresupuesto() {
        return catalogoPresupuesto;
    }

    public void setCatalogoPresupuesto(CatalogoPresupuesto catalogoPresupuesto) {
        this.catalogoPresupuesto = catalogoPresupuesto;
    }

    public BigDecimal getSuplemento() {
        return suplemento;
    }

    public void setSuplemento(BigDecimal suplemento) {
        this.suplemento = suplemento;
    }

    public BigDecimal getReducido() {
        return reducido;
    }

    public void setReducido(BigDecimal reducido) {
        this.reducido = reducido;
    }

    public BigDecimal getCodificado() {
        return codificado;
    }

    public void setCodificado(BigDecimal codificado) {
        this.codificado = codificado;
    }

    public ReformaIngresoSuplemento getReforma() {
        return reforma;
    }

    public void setReforma(ReformaIngresoSuplemento reforma) {
        this.reforma = reforma;
    }

    public BigInteger getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(BigInteger codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public ProformaIngreso getProformaIngreso() {
        return proformaIngreso;
    }

    public void setProformaIngreso(ProformaIngreso proformaIngreso) {
        this.proformaIngreso = proformaIngreso;
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
        if (!(object instanceof DetalleReformaIngresoSuplemento)) {
            return false;
        }
        DetalleReformaIngresoSuplemento other = (DetalleReformaIngresoSuplemento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Presupuesto.Entity.DetallereformaIngresosuplemento[ id=" + id + " ]";
    }

}
