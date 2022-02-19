/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "cuenta_contable_presupuesto")
@NamedQueries({
    @NamedQuery(name = "CuentaContablePresupuesto.findAll", query = "SELECT c FROM CuentaContablePresupuesto c"),
    @NamedQuery(name = "CuentaContablePresupuesto.findById", query = "SELECT c FROM CuentaContablePresupuesto c WHERE c.id = :id"),
    @NamedQuery(name = "CuentaContablePresupuesto.findByFechaCreacion", query = "SELECT c FROM CuentaContablePresupuesto c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "CuentaContablePresupuesto.findByUsuarioCreacion", query = "SELECT c FROM CuentaContablePresupuesto c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "CuentaContablePresupuesto.findByFechaModificacion", query = "SELECT c FROM CuentaContablePresupuesto c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "CuentaContablePresupuesto.findByUsuarioModifica", query = "SELECT c FROM CuentaContablePresupuesto c WHERE c.usuarioModifica = :usuarioModifica")})
public class CuentaContablePresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @JoinColumn(name = "presupuesto_debito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto presupuestoDebito;
    @JoinColumn(name = "presupuesto_credito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto presupuestoCredito;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;

    public CuentaContablePresupuesto() {
    }

    public CuentaContablePresupuesto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public CatalogoPresupuesto getPresupuestoDebito() {
        return presupuestoDebito;
    }

    public void setPresupuestoDebito(CatalogoPresupuesto presupuestoDebito) {
        this.presupuestoDebito = presupuestoDebito;
    }

    public CatalogoPresupuesto getPresupuestoCredito() {
        return presupuestoCredito;
    }

    public void setPresupuestoCredito(CatalogoPresupuesto presupuestoCredito) {
        this.presupuestoCredito = presupuestoCredito;
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
        if (!(object instanceof CuentaContablePresupuesto)) {
            return false;
        }
        CuentaContablePresupuesto other = (CuentaContablePresupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.entities.CuentaContablePresupuesto[ id=" + id + " ]";
    }

}
