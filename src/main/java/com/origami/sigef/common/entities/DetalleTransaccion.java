/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
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
@Table(name = "detalle_transaccion", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "DetalleTransaccion.findAll", query = "SELECT d FROM DetalleTransaccion d")
    ,
    @NamedQuery(name = "DetalleTransaccion.findById", query = "SELECT d FROM DetalleTransaccion d WHERE d.id = :id")
    ,
    @NamedQuery(name = "DetalleTransaccion.findByDiarioGeneral", query = "SELECT d FROM DetalleTransaccion d WHERE d.diarioGeneral = :diarioGeneral")
    ,
    @NamedQuery(name = "DetalleTransaccion.findByDebe", query = "SELECT d FROM DetalleTransaccion d WHERE d.debe = :debe")
    ,
    @NamedQuery(name = "DetalleTransaccion.findByHaber", query = "SELECT d FROM DetalleTransaccion d WHERE d.haber = :haber")
    ,
    @NamedQuery(name = "DetalleTransaccion.findByComprometido", query = "SELECT d FROM DetalleTransaccion d WHERE d.comprometido = :comprometido")
    ,
    @NamedQuery(name = "DetalleTransaccion.findByDevengado", query = "SELECT d FROM DetalleTransaccion d WHERE d.devengado = :devengado")})
public class DetalleTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe")
    private BigDecimal debe;
    @Column(name = "haber")
    private BigDecimal haber;
    @Column(name = "comprometido")
    private BigDecimal comprometido;
    @Column(name = "devengado")
    private BigDecimal devengado;
    @Column(name = "ejecutado")
    private BigDecimal ejecutado;
    @JoinColumn(name = "tipo_transaccion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoTransaccion;
    @JoinColumn(name = "partida_presupuestaria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto partidaPresupuestaria;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaContable;
    @JoinColumn(name = "estructura_programatica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanProgramatico estructuraProgramatica;
    @Column(name = "contador")
    private BigInteger contador;
    @Column(name = "comprobante_pago")
    private Boolean comprobantePago;
    @Column(name = "cedula_presupuestaria")
    private String cedulaPresupuestaria;
    @JoinColumn(name = "fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem fuente;
    @JoinColumn(name = "tipo_devengado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoDevengado;
    @Column(name = "dato_cargado")
    private Boolean datoCargado;
    @JoinColumn(name = "id_detalle_reserva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleSolicitudCompromiso idDetalleReserva;
  
    @Transient
    private BigDecimal sumaAcuDeudor;
    @Transient
    private BigDecimal sumaAcuAcreedor;
    @Transient
    private BigDecimal saldoFinalDeudor;
    @Transient
    private BigDecimal saldoFinalAcreedor;

    @Transient
    private Integer cuentaMonetaria;

    @Transient
    private BigDecimal saldoAnterior;
    
    @Transient
    private List<CuentaContable> seleccionarCuentaContable;

    public DetalleTransaccion() {
        this.comprobantePago = Boolean.FALSE;
        this.datoCargado = Boolean.FALSE;
        this.devengado = BigDecimal.ZERO;
        this.comprometido = BigDecimal.ZERO;
        this.ejecutado = BigDecimal.ZERO;
        this.debe = BigDecimal.ZERO;
        this.haber = BigDecimal.ZERO;
    }

    public DetalleTransaccion(Long id) {
        this.id = id;
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

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
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

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public CatalogoItem getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(CatalogoItem tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
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

    public BigInteger getContador() {
        return contador;
    }

    public void setContador(BigInteger contador) {
        this.contador = contador;
    }

    public Boolean getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(Boolean comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public BigDecimal getSumaAcuDeudor() {
        return sumaAcuDeudor;
    }

    public void setSumaAcuDeudor(BigDecimal sumaAcuDeudor) {
        this.sumaAcuDeudor = sumaAcuDeudor;
    }

    public BigDecimal getSumaAcuAcreedor() {
        return sumaAcuAcreedor;
    }

    public void setSumaAcuAcreedor(BigDecimal sumaAcuAcreedor) {
        this.sumaAcuAcreedor = sumaAcuAcreedor;
    }

    public BigDecimal getSaldoFinalDeudor() {
        return saldoFinalDeudor;
    }

    public void setSaldoFinalDeudor(BigDecimal saldoFinalDeudor) {
        this.saldoFinalDeudor = saldoFinalDeudor;
    }

    public BigDecimal getSaldoFinalAcreedor() {
        return saldoFinalAcreedor;
    }

    public void setSaldoFinalAcreedor(BigDecimal saldoFinalAcreedor) {
        this.saldoFinalAcreedor = saldoFinalAcreedor;
    }

    public String getCedulaPresupuestaria() {
        return cedulaPresupuestaria;
    }

    public void setCedulaPresupuestaria(String cedulaPresupuestaria) {
        this.cedulaPresupuestaria = cedulaPresupuestaria;
    }

    public CatalogoItem getFuente() {
        return fuente;
    }

    public void setFuente(CatalogoItem fuente) {
        this.fuente = fuente;
    }

    public Integer getCuentaMonetaria() {
        return cuentaMonetaria;
    }

    public void setCuentaMonetaria(Integer cuentaMonetaria) {
        this.cuentaMonetaria = cuentaMonetaria;
    }

    public CatalogoItem getTipoDevengado() {
        return tipoDevengado;
    }

    public void setTipoDevengado(CatalogoItem tipoDevengado) {
        this.tipoDevengado = tipoDevengado;
    }

    public Boolean getDatoCargado() {
        return datoCargado;
    }

    public void setDatoCargado(Boolean datoCargado) {
        this.datoCargado = datoCargado;
    }

    public DetalleSolicitudCompromiso getIdDetalleReserva() {
        return idDetalleReserva;
    }

    public void setIdDetalleReserva(DetalleSolicitudCompromiso idDetalleReserva) {
        this.idDetalleReserva = idDetalleReserva;
    }

    public List<CuentaContable> getSeleccionarCuentaContable() {
        return seleccionarCuentaContable;
    }

    public void setSeleccionarCuentaContable(List<CuentaContable> seleccionarCuentaContable) {
        this.seleccionarCuentaContable = seleccionarCuentaContable;
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
        if (!(object instanceof DetalleTransaccion)) {
            return false;
        }
        DetalleTransaccion other = (DetalleTransaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DetalleTransaccion[ id=" + id + " ]";
    }

}
