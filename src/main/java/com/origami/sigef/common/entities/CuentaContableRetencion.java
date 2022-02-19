/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.tesoreria.entities.Rubro;
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
 * @author jesus
 */
@Entity
@Table(name = "cuenta_contable_retencion", schema = "tesoreria")
@NamedQueries({
    @NamedQuery(name = "CuentaContableRetencion.findAll", query = "SELECT c FROM CuentaContableRetencion c")})
public class CuentaContableRetencion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "cont_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas contContable;
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "periodo")
    private Short periodo;
    @JoinColumn(name = "retencion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Rubro retencion;

    public CuentaContableRetencion() {
    }

    public CuentaContableRetencion(Long id) {
        this.id = id;
    }

    public CuentaContableRetencion(Long id, ContCuentas cuentaContable, boolean estado) {
        this.id = id;
        this.contContable = cuentaContable;
        this.estado = estado;
    }

    public CuentaContableRetencion(Long id, ContCuentas cuentaContable, Rubro retencion) {
        this.id = id;
        this.retencion = retencion;
        this.contContable = cuentaContable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Rubro getRetencion() {
        return retencion;
    }

    public void setRetencion(Rubro retencion) {
        this.retencion = retencion;
    }

    public ContCuentas getContContable() {
        return contContable;
    }

    public void setContContable(ContCuentas contContable) {
        this.contContable = contContable;
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
        if (!(object instanceof CuentaContableRetencion)) {
            return false;
        }
        CuentaContableRetencion other = (CuentaContableRetencion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "package com.origami.sigef.common.entities.CuentaContableRetencion[ id=" + id + " ]";
    }

}
