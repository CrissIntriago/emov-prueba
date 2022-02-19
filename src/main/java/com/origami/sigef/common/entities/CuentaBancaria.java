/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "cuenta_bancaria", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "CuentaBancaria.findAll", query = "SELECT c FROM CuentaBancaria c"),
    @NamedQuery(name = "CuentaBancaria.findById", query = "SELECT c FROM CuentaBancaria c WHERE c.id = :id"),
    @NamedQuery(name = "CuentaBancaria.findByCuentaPatrimonial", query = "SELECT c FROM CuentaBancaria c WHERE c.cuentaPatrimonial = :cuentaPatrimonial"),
    @NamedQuery(name = "CuentaBancaria.findByCuentaMovimiento", query = "SELECT c FROM CuentaBancaria c WHERE c.cuentaMovimiento = :cuentaMovimiento"),
    @NamedQuery(name = "CuentaBancaria.findBySaldoInicial", query = "SELECT c FROM CuentaBancaria c WHERE c.saldoInicial = :saldoInicial")})
public class CuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Size(max = 2147483647)
    @Column(name = "nombre_cuenta_bancaria")
    private String nombreCuentaBancaria;
    @Size(max = 2147483647)
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @Column(name = "tipo_cuenta")
    private Boolean tipoCuenta;    
    
    @JoinColumn(name = "cuenta_patrimonial", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaPatrimonial;

    @JoinColumn(name = "cuenta_movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaMovimiento;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;

    @JoinColumn(name = "nombre_banco", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Banco nombreBanco;

    public CuentaBancaria() {
        this.estado = Boolean.TRUE;
    }

    public CuentaBancaria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public CuentaContable getCuentaPatrimonial() {
        return cuentaPatrimonial;
    }

    public void setCuentaPatrimonial(CuentaContable cuentaPatrimonial) {
        this.cuentaPatrimonial = cuentaPatrimonial;
    }

    public CuentaContable getCuentaMovimiento() {
        return cuentaMovimiento;
    }

    public void setCuentaMovimiento(CuentaContable cuentaMovimiento) {
        this.cuentaMovimiento = cuentaMovimiento;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Banco getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(Banco nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public Boolean getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(Boolean tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
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

    public String getNombreCuentaBancaria() {
        return nombreCuentaBancaria;
    }

    public void setNombreCuentaBancaria(String nombreCuentaBancaria) {
        this.nombreCuentaBancaria = nombreCuentaBancaria;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentaBancaria)) {
            return false;
        }
        CuentaBancaria other = (CuentaBancaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.curso.entities.CuentaBancaria[ id=" + id + " ]";
    }

}
