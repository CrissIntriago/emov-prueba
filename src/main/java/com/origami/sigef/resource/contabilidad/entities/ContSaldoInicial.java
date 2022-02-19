/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

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
import org.hibernate.annotations.Formula;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_saldo_inicial", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContSaldoInicial.findAll", query = "SELECT c FROM ContSaldoInicial c"),
    @NamedQuery(name = "ContSaldoInicial.findById", query = "SELECT c FROM ContSaldoInicial c WHERE c.id = :id"),
    @NamedQuery(name = "ContSaldoInicial.findByIdCuenta", query = "SELECT c FROM ContSaldoInicial c WHERE c.idCuenta = ?1 ORDER BY c.periodo ASC"),
    @NamedQuery(name = "ContSaldoInicial.findBySaldoDebe", query = "SELECT c FROM ContSaldoInicial c WHERE c.saldoDebe = :saldoDebe"),
    @NamedQuery(name = "ContSaldoInicial.findBySaldoHaber", query = "SELECT c FROM ContSaldoInicial c WHERE c.saldoHaber = :saldoHaber"),
    @NamedQuery(name = "ContSaldoInicial.findByPeriodo", query = "SELECT c FROM ContSaldoInicial c WHERE c.periodo = ?1 AND c.idCuenta = ?2")})
public class ContSaldoInicial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_debe")
    private BigDecimal saldoDebe;
    @Column(name = "saldo_haber")
    private BigDecimal saldoHaber;
    @Column(name = "periodo")
    private Short periodo;
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuenta;
    @Formula("(SELECT (CASE WHEN COUNT(c.id) = 12 THEN true ELSE false END) FROM public.control_cuenta_contable c WHERE c.periodo = periodo AND c.estado = true)")
    private Boolean periodoCerrado;

    public ContSaldoInicial() {
        saldoDebe = BigDecimal.ZERO;
        saldoHaber = BigDecimal.ZERO;
    }

    public ContSaldoInicial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSaldoDebe() {
        return saldoDebe;
    }

    public void setSaldoDebe(BigDecimal saldoDebe) {
        this.saldoDebe = saldoDebe;
    }

    public BigDecimal getSaldoHaber() {
        return saldoHaber;
    }

    public void setSaldoHaber(BigDecimal saldoHaber) {
        this.saldoHaber = saldoHaber;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public ContCuentas getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(ContCuentas idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Boolean getPeriodoCerrado() {
        return periodoCerrado;
    }

    public void setPeriodoCerrado(Boolean periodoCerrado) {
        this.periodoCerrado = periodoCerrado;
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
        if (!(object instanceof ContSaldoInicial)) {
            return false;
        }
        ContSaldoInicial other = (ContSaldoInicial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial[ id=" + id + " ]";
    }

}
