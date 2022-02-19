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
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "cuenta_pago")
@NamedQueries({
    @NamedQuery(name = "CuentaPago.findAll", query = "SELECT c FROM CuentaPago c"),
    @NamedQuery(name = "CuentaPago.findById", query = "SELECT c FROM CuentaPago c WHERE c.id = :id"),
    @NamedQuery(name = "CuentaPago.findByNumeroCuenta", query = "SELECT c FROM CuentaPago c WHERE c.numeroCuenta = :numeroCuenta"),
    @NamedQuery(name = "CuentaPago.findByFechaAprovacion", query = "SELECT c FROM CuentaPago c WHERE c.fechaAprovacion = :fechaAprovacion"),
    @NamedQuery(name = "CuentaPago.findByFechaDesactivacion", query = "SELECT c FROM CuentaPago c WHERE c.fechaDesactivacion = :fechaDesactivacion"),
    @NamedQuery(name = "CuentaPago.findByEstado", query = "SELECT c FROM CuentaPago c WHERE c.estado = :estado"),
    @NamedQuery(name = "CuentaPago.findByRestrictiva", query = "SELECT c FROM CuentaPago c WHERE c.restrictiva = :restrictiva"),
    @NamedQuery(name = "CuentaPago.findByTransferencia", query = "SELECT c FROM CuentaPago c WHERE c.transferencia = :transferencia"),
    @NamedQuery(name = "CuentaPago.findByDescripcion", query = "SELECT c FROM CuentaPago c WHERE c.descripcion = :descripcion")})
public class CuentaPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 50)
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @Column(name = "fecha_aprovacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAprovacion;
    @Column(name = "fecha_desactivacion")
    @Temporal(TemporalType.DATE)
    private Date fechaDesactivacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "restrictiva")
    private Boolean restrictiva;
    @Column(name = "transferencia")
    private Boolean transferencia;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo_banco", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoBanco;
    @JoinColumn(name = "tipo_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoCuenta;
    @JoinColumn(name = "tipo_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoPago;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente beneficiario;

    public CuentaPago() {
    }

    public CuentaPago(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Date getFechaAprovacion() {
        return fechaAprovacion;
    }

    public void setFechaAprovacion(Date fechaAprovacion) {
        this.fechaAprovacion = fechaAprovacion;
    }

    public Date getFechaDesactivacion() {
        return fechaDesactivacion;
    }

    public void setFechaDesactivacion(Date fechaDesactivacion) {
        this.fechaDesactivacion = fechaDesactivacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getRestrictiva() {
        return restrictiva;
    }

    public void setRestrictiva(Boolean restrictiva) {
        this.restrictiva = restrictiva;
    }

    public Boolean getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Boolean transferencia) {
        this.transferencia = transferencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CatalogoItem getTipoBanco() {
        return tipoBanco;
    }

    public void setTipoBanco(CatalogoItem tipoBanco) {
        this.tipoBanco = tipoBanco;
    }

    public CatalogoItem getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(CatalogoItem tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public CatalogoItem getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(CatalogoItem tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
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
        if (!(object instanceof CuentaPago)) {
            return false;
        }
        CuentaPago other = (CuentaPago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CuentaPago[ id=" + id + " ]";
    }

}
