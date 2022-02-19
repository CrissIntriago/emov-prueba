/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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
@Table(name = "cuentacontable_catalogopresupuesto")
@NamedQueries({
    @NamedQuery(name = "CuentaContablecatalogoPresupuesto.findAll", query = "SELECT c FROM CuentaContablecatalogoPresupuesto c")})
public class CuentaContablecatalogoPresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "catalogo_presupuesto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto catalogoPresupuesto;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;

    public CuentaContablecatalogoPresupuesto() {
    }

    public CuentaContablecatalogoPresupuesto(Long id) {
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

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
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
        if (!(object instanceof CuentaContablecatalogoPresupuesto)) {
            return false;
        }
        CuentaContablecatalogoPresupuesto other = (CuentaContablecatalogoPresupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CuentaContablecatalogoPresupuesto[ id=" + id + " ]";
    }

}
