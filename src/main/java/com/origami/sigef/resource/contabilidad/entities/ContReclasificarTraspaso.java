/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_reclasificar_traspaso", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContReclasificarTraspaso.findAll", query = "SELECT c FROM ContReclasificarTraspaso c"),
    @NamedQuery(name = "ContReclasificarTraspaso.findById", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.id = :id"),
    @NamedQuery(name = "ContReclasificarTraspaso.findByPeriodos", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.periodoInicial = ?1 AND c.periodoFinal = ?2 AND c.traspaso = true"),
    @NamedQuery(name = "ContReclasificarTraspaso.findBySaldoDebe", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.saldoDebe = :saldoDebe"),
    @NamedQuery(name = "ContReclasificarTraspaso.findBySaldoHaber", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.saldoHaber = :saldoHaber"),
    @NamedQuery(name = "ContReclasificarTraspaso.findByTraspaso", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.traspaso = :traspaso"),
    @NamedQuery(name = "ContReclasificarTraspaso.findByReclasificar", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.reclasificar = :reclasificar"),
    @NamedQuery(name = "ContReclasificarTraspaso.findByUsuarioCreacion", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ContReclasificarTraspaso.findByFechaCreacion", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ContReclasificarTraspaso.findByUsuarioModificacion", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ContReclasificarTraspaso.findByFechaModificacion", query = "SELECT c FROM ContReclasificarTraspaso c WHERE c.fechaModificacion = :fechaModificacion")})
public class ContReclasificarTraspaso implements Serializable {

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
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @JoinColumn(name = "id_cuenta_anterior", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuentaAnterior;
    @JoinColumn(name = "id_cuenta_nueva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuentaNueva;
    @Column(name = "periodo_inicial")
    private Short periodoInicial;
    @Column(name = "periodo_final")
    private Short periodoFinal;

    @Transient
    private String codCuentaInsert;

    public ContReclasificarTraspaso() {
        traspaso = Boolean.FALSE;
        reclasificar = Boolean.FALSE;
        saldoDebe = BigDecimal.ZERO;
        saldoHaber = BigDecimal.ZERO;
    }

    public ContReclasificarTraspaso(Long id) {
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

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public ContCuentas getIdCuentaAnterior() {
        return idCuentaAnterior;
    }

    public void setIdCuentaAnterior(ContCuentas idCuentaAnterior) {
        this.idCuentaAnterior = idCuentaAnterior;
    }

    public ContCuentas getIdCuentaNueva() {
        return idCuentaNueva;
    }

    public void setIdCuentaNueva(ContCuentas idCuentaNueva) {
        this.idCuentaNueva = idCuentaNueva;
    }

    public Short getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Short periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public Short getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Short periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getCodCuentaInsert() {
        return codCuentaInsert;
    }

    public void setCodCuentaInsert(String codCuentaInsert) {
        this.codCuentaInsert = codCuentaInsert;
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
        if (!(object instanceof ContReclasificarTraspaso)) {
            return false;
        }
        ContReclasificarTraspaso other = (ContReclasificarTraspaso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContReclasificarTraspaso[ id=" + id + " ]";
    }

}
