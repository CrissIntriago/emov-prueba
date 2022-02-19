/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.origami.sigef.common.entities.Banco;
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
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_cuenta_entidad", schema = "contabilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContCuentaEntidad.findAll", query = "SELECT c FROM ContCuentaEntidad c"),
    @NamedQuery(name = "ContCuentaEntidad.findById", query = "SELECT c FROM ContCuentaEntidad c WHERE c.id = :id"),
    @NamedQuery(name = "ContCuentaEntidad.findByIdBanco", query = "SELECT c FROM ContCuentaEntidad c WHERE c.idBanco = :idBanco"),
    @NamedQuery(name = "ContCuentaEntidad.findBySaldoInicial", query = "SELECT c FROM ContCuentaEntidad c WHERE c.saldoInicial = :saldoInicial"),
    @NamedQuery(name = "ContCuentaEntidad.findByEstado", query = "SELECT c FROM ContCuentaEntidad c WHERE c.estado = :estado"),
    @NamedQuery(name = "ContCuentaEntidad.findByNombre", query = "SELECT c FROM ContCuentaEntidad c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "ContCuentaEntidad.findByNumeroCuenta", query = "SELECT c FROM ContCuentaEntidad c WHERE c.numeroCuenta = :numeroCuenta"),
    @NamedQuery(name = "ContCuentaEntidad.findByTipoCuenta", query = "SELECT c FROM ContCuentaEntidad c WHERE c.tipoCuenta = :tipoCuenta")})
public class ContCuentaEntidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @Column(name = "tipo_cuenta")
    private Boolean tipoCuenta;
    @JoinColumn(name = "id_cuenta_movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuentaMovimiento;
    @JoinColumn(name = "id_cuenta_patrimonial", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuentaPatrimonial;
    @JoinColumn(name = "id_banco", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Banco idBanco;

    public ContCuentaEntidad() {
        this.estado = true;
        this.saldoInicial = BigDecimal.ZERO;
    }

    public ContCuentaEntidad(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Banco getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Banco idBanco) {
        this.idBanco = idBanco;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Boolean getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(Boolean tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public ContCuentas getIdCuentaMovimiento() {
        return idCuentaMovimiento;
    }

    public void setIdCuentaMovimiento(ContCuentas idCuentaMovimiento) {
        this.idCuentaMovimiento = idCuentaMovimiento;
    }

    public ContCuentas getIdCuentaPatrimonial() {
        return idCuentaPatrimonial;
    }

    public void setIdCuentaPatrimonial(ContCuentas idCuentaPatrimonial) {
        this.idCuentaPatrimonial = idCuentaPatrimonial;
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
        if (!(object instanceof ContCuentaEntidad)) {
            return false;
        }
        ContCuentaEntidad other = (ContCuentaEntidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContCuentaEntidad[ id=" + id + " ]";
    }

}
