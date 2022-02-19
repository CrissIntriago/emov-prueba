/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "detalle_comprobante_pago", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "DetalleComprobantePago.findAll", query = "SELECT d FROM DetalleComprobantePago d"),
    @NamedQuery(name = "DetalleComprobantePago.findById", query = "SELECT d FROM DetalleComprobantePago d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleComprobantePago.findByCuentaContable", query = "SELECT d FROM DetalleComprobantePago d WHERE d.cuentaContable = :cuentaContable"),
    @NamedQuery(name = "DetalleComprobantePago.findByDebe", query = "SELECT d FROM DetalleComprobantePago d WHERE d.debe = :debe"),
    @NamedQuery(name = "DetalleComprobantePago.findByHaber", query = "SELECT d FROM DetalleComprobantePago d WHERE d.haber = :haber"),
    @NamedQuery(name = "DetalleComprobantePago.findByTipoPago", query = "SELECT d FROM DetalleComprobantePago d WHERE d.tipoPago = :tipoPago"),
    @NamedQuery(name = "DetalleComprobantePago.findByPartidaPresupuestaria", query = "SELECT d FROM DetalleComprobantePago d WHERE d.partidaPresupuestaria = :partidaPresupuestaria"),
    @NamedQuery(name = "DetalleComprobantePago.findByEstructuraProgramatica", query = "SELECT d FROM DetalleComprobantePago d WHERE d.estructuraProgramatica = :estructuraProgramatica")
})
public class DetalleComprobantePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe")
    private BigDecimal debe;
    @Column(name = "haber")
    private BigDecimal haber;
    @Column(name = "ejecutado")
    private BigDecimal ejecutado;
    @JoinColumn(name = "comprobante_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ComprobantePago comprobantePago;
    @JoinColumn(name = "tipo_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoPago;
    @JoinColumn(name = "partida_presupuestaria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto partidaPresupuestaria;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;
    @JoinColumn(name = "estructura_programatica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico estructuraProgramatica;
    @Column(name = "linea")
    private BigInteger linea;
    @JoinColumn(name = "fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuente;
    @Column(name = "cedula_presupuestaria")
    private String cedulaPresupuestaria;
    @JoinColumn(name = "id_detalle_reserva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleSolicitudCompromiso idDetalleReserva;

    @Transient
    private BigDecimal saldoAnterior;
    @Transient
    private String nombreBeneficiario;
    @Transient
    private boolean datoCargado;

    public DetalleComprobantePago() {
        this.debe = BigDecimal.ZERO;
        this.haber = BigDecimal.ZERO;
        this.ejecutado = BigDecimal.ZERO;
        this.datoCargado = false;
    }

    public DetalleComprobantePago(Long id) {
        this.id = id;
    }

    public String getNombreBeneficiario() {
        if (this.comprobantePago.getBeneficiarioComprobantePagos() != null && !this.comprobantePago.getBeneficiarioComprobantePagos().isEmpty()) {
            this.nombreBeneficiario = "";
            for (int i = 0; i < this.comprobantePago.getBeneficiarioComprobantePagos().size(); i++) {
                if (i == 0) {
                    this.nombreBeneficiario = this.comprobantePago.getBeneficiarioComprobantePagos().get(i).getBeneficiario().getNombreCompleto();
                } else {
                    this.nombreBeneficiario = this.nombreBeneficiario + ", " + this.comprobantePago.getBeneficiarioComprobantePagos().get(i).getBeneficiario().getNombreCompleto();
                }
            }
            return this.nombreBeneficiario.toUpperCase();
        }
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getLinea() {
        return linea;
    }

    public void setLinea(BigInteger linea) {
        this.linea = linea;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public CatalogoItem getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(CatalogoItem tipoPago) {
        this.tipoPago = tipoPago;
    }

    public CatalogoPresupuesto getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(CatalogoPresupuesto partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public PlanProgramatico getEstructuraProgramatica() {
        return estructuraProgramatica;
    }

    public void setEstructuraProgramatica(PlanProgramatico estructuraProgramatica) {
        this.estructuraProgramatica = estructuraProgramatica;
    }

    public BigDecimal getEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(BigDecimal ejecutado) {
        this.ejecutado = ejecutado;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public CatalogoItem getFuente() {
        return fuente;
    }

    public void setFuente(CatalogoItem fuente) {
        this.fuente = fuente;
    }

    public String getCedulaPresupuestaria() {
        return cedulaPresupuestaria;
    }

    public void setCedulaPresupuestaria(String cedulaPresupuestaria) {
        this.cedulaPresupuestaria = cedulaPresupuestaria;
    }

    public boolean isDatoCargado() {
        return datoCargado;
    }

    public void setDatoCargado(boolean datoCargado) {
        this.datoCargado = datoCargado;
    }

    public DetalleSolicitudCompromiso getIdDetalleReserva() {
        return idDetalleReserva;
    }

    public void setIdDetalleReserva(DetalleSolicitudCompromiso idDetalleReserva) {
        this.idDetalleReserva = idDetalleReserva;
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
        if (!(object instanceof DetalleComprobantePago)) {
            return false;
        }
        DetalleComprobantePago other = (DetalleComprobantePago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DetalleComprobantePago[ id=" + id + " ]";
    }

}
