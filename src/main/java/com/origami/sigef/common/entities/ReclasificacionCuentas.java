/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.ats.modelAts.DetalleVentas;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "reclasificacion_cuentas", schema = "contabilidad")
@SqlResultSetMapping(name = "SaldoDebeHaberDTOMapping", 
        classes = @ConstructorResult(targetClass = SaldoDebeHaberDTO.class, 
            columns = {
                @ColumnResult(name = "saldoDebe", type = BigDecimal.class), 
                @ColumnResult(name = "saldoHaber", type = BigDecimal.class)
            })
)
@NamedQueries({
    @NamedQuery(name = "ReclasificacionCuentas.findAll", query = "SELECT r FROM ReclasificacionCuentas r")
    , @NamedQuery(name = "ReclasificacionCuentas.findById", query = "SELECT r FROM ReclasificacionCuentas r WHERE r.id = :id")
    , @NamedQuery(name = "ReclasificacionCuentas.findBySaldoDebe", query = "SELECT r FROM ReclasificacionCuentas r WHERE r.saldoDebe = :saldoDebe")
    , @NamedQuery(name = "ReclasificacionCuentas.findBySaldoHaber", query = "SELECT r FROM ReclasificacionCuentas r WHERE r.saldoHaber = :saldoHaber")
    , @NamedQuery(name = "ReclasificacionCuentas.findByTraspaso", query = "SELECT r FROM ReclasificacionCuentas r WHERE r.traspaso = :traspaso")})
public class ReclasificacionCuentas implements Serializable {

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
    @Column(name = "traspaso")
    private Boolean traspaso;
    @Column(name = "reclasificar")
    private Boolean reclasificar;
    @JoinColumn(name = "cuenta_contable_anterior", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContableAnterior;
    @JoinColumn(name = "cuenta_contable_nueva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContableNueva;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    public ReclasificacionCuentas() {
        this.traspaso = Boolean.FALSE;
        this.reclasificar = Boolean.FALSE;
    }

    public ReclasificacionCuentas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CuentaContable getCuentaContableAnterior() {
        return cuentaContableAnterior;
    }

    public void setCuentaContableAnterior(CuentaContable cuentaContableAnterior) {
        this.cuentaContableAnterior = cuentaContableAnterior;
    }

    public CuentaContable getCuentaContableNueva() {
        return cuentaContableNueva;
    }

    public void setCuentaContableNueva(CuentaContable cuentaContableNueva) {
        this.cuentaContableNueva = cuentaContableNueva;
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

    public Boolean getTraspaso() {
        return traspaso;
    }

    public void setTraspaso(Boolean traspaso) {
        this.traspaso = traspaso;
    }

    public Boolean getReclasificar() {
        return reclasificar;
    }

    public void setReclasificar(Boolean reclasificar) {
        this.reclasificar = reclasificar;
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

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
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
        if (!(object instanceof ReclasificacionCuentas)) {
            return false;
        }
        ReclasificacionCuentas other = (ReclasificacionCuentas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ReclasificacionCuentas[ id=" + id + " ]";
    }

}
