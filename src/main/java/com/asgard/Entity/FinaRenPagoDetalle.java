/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_pago_detalle", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenPagoDetalle.findAll", query = "SELECT f FROM FinaRenPagoDetalle f"),
    @NamedQuery(name = "FinaRenPagoDetalle.findById", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTipoPago", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.tipoPago = :tipoPago"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByValor", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.valor = :valor"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTcNumTarjeta", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.tcNumTarjeta = :tcNumTarjeta"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTcFechaCaducidad", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.tcFechaCaducidad = :tcFechaCaducidad"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTcAutorizacion", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.tcAutorizacion = :tcAutorizacion"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTcBaucher", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.tcBaucher = :tcBaucher"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTcTitular", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.tcTitular = :tcTitular"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByNcNumCredito", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.ncNumCredito = :ncNumCredito"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByNcFecha", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.ncFecha = :ncFecha"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByChNumCheque", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.chNumCheque = :chNumCheque"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByChNumCuenta", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.chNumCuenta = :chNumCuenta"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTrNumTransferencia", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.trNumTransferencia = :trNumTransferencia"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByTrFecha", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.trFecha = :trFecha"),
    @NamedQuery(name = "FinaRenPagoDetalle.findByEstado", query = "SELECT f FROM FinaRenPagoDetalle f WHERE f.estado = :estado")})
public class FinaRenPagoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_pago")
    private long tipoPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Size(max = 80)
    @Column(name = "tc_num_tarjeta")
    private String tcNumTarjeta;
    @Column(name = "tc_fecha_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tcFechaCaducidad;
    @Size(max = 100)
    @Column(name = "tc_autorizacion")
    private String tcAutorizacion;
    @Size(max = 60)
    @Column(name = "tc_baucher")
    private String tcBaucher;
    @Column(name = "tc_titular")
    private String tcTitular;
    @Size(max = 50)
    @Column(name = "nc_num_credito")
    private String ncNumCredito;
    @Column(name = "nc_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ncFecha;
    @Size(max = 50)
    @Column(name = "ch_num_cheque")
    private String chNumCheque;
    @Size(max = 50)
    @Column(name = "ch_num_cuenta")
    private String chNumCuenta;
    @Size(max = 50)
    @Column(name = "tr_num_transferencia")
    private String trNumTransferencia;
    @Column(name = "tr_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trFecha;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "tr_banco", referencedColumnName = "id")
    @ManyToOne
    private Banco trBanco;
    @JoinColumn(name = "banco", referencedColumnName = "id")
    @ManyToOne
    private Banco banco;
    @JoinColumn(name = "tc_banco", referencedColumnName = "id")
    @ManyToOne
    private Banco tcBanco;
    @JoinColumn(name = "ch_banco", referencedColumnName = "id")
    @ManyToOne
    private Banco chBanco;
    @JoinColumn(name = "pago", referencedColumnName = "id")
    @ManyToOne
    private FinaRenPago pago;

    public FinaRenPagoDetalle() {
    }

    public FinaRenPagoDetalle(Long id) {
        this.id = id;
    }

    public FinaRenPagoDetalle(Long id, long tipoPago) {
        this.id = id;
        this.tipoPago = tipoPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(long tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTcNumTarjeta() {
        return tcNumTarjeta;
    }

    public void setTcNumTarjeta(String tcNumTarjeta) {
        this.tcNumTarjeta = tcNumTarjeta;
    }

    public Date getTcFechaCaducidad() {
        return tcFechaCaducidad;
    }

    public void setTcFechaCaducidad(Date tcFechaCaducidad) {
        this.tcFechaCaducidad = tcFechaCaducidad;
    }

    public String getTcAutorizacion() {
        return tcAutorizacion;
    }

    public void setTcAutorizacion(String tcAutorizacion) {
        this.tcAutorizacion = tcAutorizacion;
    }

    public String getTcBaucher() {
        return tcBaucher;
    }

    public void setTcBaucher(String tcBaucher) {
        this.tcBaucher = tcBaucher;
    }

    public String getTcTitular() {
        return tcTitular;
    }

    public void setTcTitular(String tcTitular) {
        this.tcTitular = tcTitular;
    }

    public String getNcNumCredito() {
        return ncNumCredito;
    }

    public void setNcNumCredito(String ncNumCredito) {
        this.ncNumCredito = ncNumCredito;
    }

    public Date getNcFecha() {
        return ncFecha;
    }

    public void setNcFecha(Date ncFecha) {
        this.ncFecha = ncFecha;
    }

    public String getChNumCheque() {
        return chNumCheque;
    }

    public void setChNumCheque(String chNumCheque) {
        this.chNumCheque = chNumCheque;
    }

    public String getChNumCuenta() {
        return chNumCuenta;
    }

    public void setChNumCuenta(String chNumCuenta) {
        this.chNumCuenta = chNumCuenta;
    }

    public String getTrNumTransferencia() {
        return trNumTransferencia;
    }

    public void setTrNumTransferencia(String trNumTransferencia) {
        this.trNumTransferencia = trNumTransferencia;
    }

    public Date getTrFecha() {
        return trFecha;
    }

    public void setTrFecha(Date trFecha) {
        this.trFecha = trFecha;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Banco getTrBanco() {
        return trBanco;
    }

    public void setTrBanco(Banco trBanco) {
        this.trBanco = trBanco;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Banco getTcBanco() {
        return tcBanco;
    }

    public void setTcBanco(Banco tcBanco) {
        this.tcBanco = tcBanco;
    }

    public Banco getChBanco() {
        return chBanco;
    }

    public void setChBanco(Banco chBanco) {
        this.chBanco = chBanco;
    }

    public FinaRenPago getPago() {
        return pago;
    }

    public void setPago(FinaRenPago pago) {
        this.pago = pago;
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
        if (!(object instanceof FinaRenPagoDetalle)) {
            return false;
        }
        FinaRenPagoDetalle other = (FinaRenPagoDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FinaRenPagoDetalle{" + "id=" + id + ", tipoPago=" + tipoPago + ", valor=" + valor + ", tcNumTarjeta=" + tcNumTarjeta + ", tcFechaCaducidad=" + tcFechaCaducidad + ", tcAutorizacion=" + tcAutorizacion + ", tcBaucher=" + tcBaucher + ", tcTitular=" + tcTitular + ", ncNumCredito=" + ncNumCredito + ", ncFecha=" + ncFecha + ", chNumCheque=" + chNumCheque + ", chNumCuenta=" + chNumCuenta + ", trNumTransferencia=" + trNumTransferencia + ", trFecha=" + trFecha + ", estado=" + estado + ", trBanco=" + trBanco + ", banco=" + banco + ", tcBanco=" + tcBanco + ", chBanco=" + chBanco + ", pago=" + pago + '}';
    }

}
