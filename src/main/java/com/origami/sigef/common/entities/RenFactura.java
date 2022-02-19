/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.origami.sigef.ats.modelAts.Compras;
import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.ats.modelAts.Ventas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "ren_factura", schema = "tesoreria")
public class RenFactura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "num_tramite")
    private Long numTramite;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_pagar", precision = 14, scale = 2)
    private BigDecimal totalPagar;
    @Column(name = "sub_total", precision = 14, scale = 2)
    private BigDecimal subTotal;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente solicitante;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Column(name = "user_anulacion")
    private BigInteger userAnulacion;
    @Column(name = "numero_comprobante")
    private BigInteger numeroComprobante;
    @Size(max = 100)
    @Column(name = "codigo_comprobante", length = 100)
    private String codigoComprobante;
    @Size(max = 100)
    @Column(name = "clave_acceso", length = 100)
    private String claveAcceso;
    @Size(max = 100)
    @Column(name = "numero_autorizacion", length = 100)
    private String numeroAutorizacion;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @Size(max = 2147483647)
    @Column(name = "observacion", length = 2147483647)
    private String observacion;
    @JoinColumn(name = "caja", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cajero caja;
    @Size(max = 2147483647)
    @Column(name = "forma_pago", length = 2147483647)
    private String formaPago;
    @Size(max = 2147483647)
    @Column(name = "estado_ws", length = 2147483647)
    private String estadoWs;
    @Size(max = 2147483647)
    @Column(name = "mensaje_ws", length = 2147483647)
    private String mensajeWs;
    @Column(name = "codigo_comprobante_retenido")
    private String codigoComprobanteRetenido;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "retenido")
    private Boolean retenido;
    @Column(name = "factura_sin_tramite")
    private Boolean facturaSinTramite;
    @Column(name = "num_comprobante")
    private Long numComprobante;
    @Column(name = "id_facrura_refenrencia")
    private Long idFacruraFefenrencia;
    @JoinColumn(name = "estado_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoLiquidacion;
    @JoinColumn(name = "estado_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPago;
    @JoinColumn(name = "comprobante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Comprobantes comprobante;
    @Column(name = "verificada_ws")
    private String verificadaWs;
    @JoinColumn(name = "diario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral diario;
    @OneToMany(mappedBy = "factura", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLiquidacion> liquidacionesList;
    @OneToMany(mappedBy = "factura", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPago> pagosList;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "tipo_emision")
    private String tipoEmision;
    @JoinColumn(name = "comprobante_modifica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Comprobantes comprobanteModifica;
    @Column(name = "periodo")
    private String periodo;
    @Column(name = "detalle_compras")
    private String detalleCompras;
    @OneToMany(mappedBy = "renFactura", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenFacturaDetalle> renFacturaDetalleList;
    @Column(name = "considera_iva")
    private Boolean consideraIva;
    @Column(name = "ride_enviado")
    private Boolean rideEnviado;
    @Column(name = "genera_factura")
    private Boolean generaFactura;
    @Column(name = "valor_retenido")
    private BigDecimal valorRetenido;
    @JoinColumn(name = "id_factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Factura idFactura;
    @Column(name = "monto_iva")
    private BigDecimal montoIva;

    @Transient
    private IVA ats;
    @Transient
    private Compras compras;
    @Transient
    private Ventas ventas;
    @Transient
    private String cod;
    @Transient
    private String transaccion;
    @Transient
    private Integer numRegistros;
    @Transient
    private BigDecimal biTarifa0;
    @Transient
    private BigDecimal biTarifaDiferente0;
    @Transient
    private BigDecimal biNoObjetoIva;
    @Transient
    private BigDecimal valorIva;
    @Transient
    private String identificacionProveedor;
    @Transient
    private Date fechaEmisionCabecera;
    @Transient
    private BigDecimal valorDebitado;
    @Transient
    private FinaRenPago pago;

    public RenFactura() {
        this.totalPagar = BigDecimal.ZERO;
        this.subTotal = BigDecimal.ZERO;
        this.estado = Boolean.TRUE;
        this.valorRetenido = BigDecimal.ZERO;
        this.montoIva = BigDecimal.ZERO;
    }

    public RenFactura(Cliente solicitante,
            Proveedor beneficiario, CatalogoItem estadoPago, CatalogoItem estadoLiquidacion,
            BigInteger numeroComprobante, String codigoComprobante, BigDecimal subTotal, BigDecimal totalPagar,
            Integer anio, Integer mes, String periodo, Boolean rideEnviado, Boolean generaFactura,
            Boolean tramiteOnline, String tipoEmision,
            Boolean consideraIva, Comprobantes comprobante, Comprobantes comprobanteModifica, ContDiarioGeneral diarioGeneral) {
        this.solicitante = solicitante;
        this.proveedor = beneficiario;
        this.estadoPago = estadoPago;
        this.estadoLiquidacion = estadoLiquidacion;
        this.numeroComprobante = numeroComprobante;
        this.codigoComprobante = codigoComprobante;
        this.subTotal = subTotal;
        this.totalPagar = totalPagar;
        this.anio = anio;
        this.mes = mes;
        this.periodo = periodo;
        this.rideEnviado = rideEnviado;
        this.generaFactura = generaFactura;
        this.facturaSinTramite = tramiteOnline;
        this.tipoEmision = tipoEmision;
        this.consideraIva = consideraIva;
        this.comprobante = comprobante;
        this.comprobanteModifica = comprobanteModifica;
        this.diario = diarioGeneral;
        this.montoIva = BigDecimal.ZERO;
    }

    public String getDetalleCompras() {
        return detalleCompras;
    }

    public void setDetalleCompras(String detalleCompras) {
        this.detalleCompras = detalleCompras;
    }

    public Boolean getConsideraIva() {
        return consideraIva;
    }

    public void setConsideraIva(Boolean consideraIva) {
        this.consideraIva = consideraIva;
    }

    public Boolean getRideEnviado() {
        return rideEnviado;
    }

    public void setRideEnviado(Boolean rideEnviado) {
        this.rideEnviado = rideEnviado;
    }

    public Boolean getGeneraFactura() {
        return generaFactura;
    }

    public void setGeneraFactura(Boolean generaFactura) {
        this.generaFactura = generaFactura;
    }

    public List<RenFacturaDetalle> getRenFacturaDetalleList() {
        return renFacturaDetalleList;
    }

    public void setRenFacturaDetalleList(List<RenFacturaDetalle> renFacturaDetalleList) {
        this.renFacturaDetalleList = renFacturaDetalleList;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Comprobantes getComprobanteModifica() {
        return comprobanteModifica;
    }

    public void setComprobanteModifica(Comprobantes comprobanteModifica) {
        this.comprobanteModifica = comprobanteModifica;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public RenFactura(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public BigInteger getUserAnulacion() {
        return userAnulacion;
    }

    public void setUserAnulacion(BigInteger userAnulacion) {
        this.userAnulacion = userAnulacion;
    }

    public BigInteger getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(BigInteger numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getCodigoComprobanteRetenido() {
        return codigoComprobanteRetenido;
    }

    public void setCodigoComprobanteRetenido(String codigoComprobanteRetenido) {
        this.codigoComprobanteRetenido = codigoComprobanteRetenido;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public Long getIdFacruraFefenrencia() {
        return idFacruraFefenrencia;
    }

    public void setIdFacruraFefenrencia(Long idFacruraFefenrencia) {
        this.idFacruraFefenrencia = idFacruraFefenrencia;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Cliente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Cliente solicitante) {
        this.solicitante = solicitante;
    }

    public Cajero getCaja() {
        return caja;
    }

    public void setCaja(Cajero caja) {
        this.caja = caja;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public CatalogoItem getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(CatalogoItem estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getEstadoWs() {
        return estadoWs;
    }

    public void setEstadoWs(String estadoWs) {
        this.estadoWs = estadoWs;
    }

    public String getMensajeWs() {
        return mensajeWs;
    }

    public void setMensajeWs(String mensajeWs) {
        this.mensajeWs = mensajeWs;
    }

    public ContDiarioGeneral getDiario() {
        return diario;
    }

    public void setDiario(ContDiarioGeneral diario) {
        this.diario = diario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getFacturaSinTramite() {
        return facturaSinTramite;
    }

    public void setFacturaSinTramite(Boolean facturaSinTramite) {
        this.facturaSinTramite = facturaSinTramite;
    }

    public Long getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(Long numComprobante) {
        this.numComprobante = numComprobante;
    }

    public Comprobantes getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobantes comprobante) {
        this.comprobante = comprobante;
    }

    public List<FinaRenLiquidacion> getLiquidacionesList() {
        return liquidacionesList;
    }

    public void setLiquidacionesList(List<FinaRenLiquidacion> liquidacionesList) {
        this.liquidacionesList = liquidacionesList;
    }

    public List<FinaRenPago> getPagosList() {
        return pagosList;
    }

    public void setPagosList(List<FinaRenPago> pagosList) {
        this.pagosList = pagosList;
    }

    public CatalogoItem getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(CatalogoItem estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public String getVerificadaWs() {
        return verificadaWs;
    }

    public void setVerificadaWs(String verificadaWs) {
        this.verificadaWs = verificadaWs;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public IVA getAts() {
        return ats;
    }

    public void setAts(IVA ats) {
        this.ats = ats;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }

    public Boolean getRetenido() {
        return retenido;
    }

    public void setRetenido(Boolean retenido) {
        this.retenido = retenido;
    }

    public Ventas getVentas() {
        return ventas;
    }

    public void setVentas(Ventas ventas) {
        this.ventas = ventas;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public Integer getNumRegistros() {
        return numRegistros;
    }

    public void setNumRegistros(Integer numRegistros) {
        this.numRegistros = numRegistros;
    }

    public BigDecimal getBiTarifa0() {
        return biTarifa0;
    }

    public void setBiTarifa0(BigDecimal biTarifa0) {
        this.biTarifa0 = biTarifa0;
    }

    public BigDecimal getBiTarifaDiferente0() {
        return biTarifaDiferente0;
    }

    public void setBiTarifaDiferente0(BigDecimal biTarifaDiferente0) {
        this.biTarifaDiferente0 = biTarifaDiferente0;
    }

    public BigDecimal getBiNoObjetoIva() {
        return biNoObjetoIva;
    }

    public void setBiNoObjetoIva(BigDecimal biNoObjetoIva) {
        this.biNoObjetoIva = biNoObjetoIva;
    }

    public BigDecimal getValorIva() {
        return valorIva;
    }

    public void setValorIva(BigDecimal valorIva) {
        this.valorIva = valorIva;
    }

    public String getIdentificacionProveedor() {
        return identificacionProveedor;
    }

    public void setIdentificacionProveedor(String identificacionProveedor) {
        this.identificacionProveedor = identificacionProveedor;
    }

    public Date getFechaEmisionCabecera() {
        return fechaEmisionCabecera;
    }

    public void setFechaEmisionCabecera(Date fechaEmisionCabecera) {
        this.fechaEmisionCabecera = fechaEmisionCabecera;
    }

    public BigDecimal getValorRetenido() {
        return valorRetenido;
    }

    public BigDecimal getValorDebitado() {
        return valorDebitado;
    }

    public void setValorDebitado(BigDecimal valorDebitado) {
        this.valorDebitado = valorDebitado;
    }

    public void setValorRetenido(BigDecimal valorRetenido) {
        this.valorRetenido = valorRetenido;
    }

    public FinaRenPago getPago() {
        return pago;
    }

    public void setPago(FinaRenPago pago) {
        this.pago = pago;
    }

    public Factura getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Factura idFactura) {
        this.idFactura = idFactura;
    }

    public BigDecimal getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(BigDecimal montoIva) {
        this.montoIva = montoIva;
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
        if (!(object instanceof RenFactura)) {
            return false;
        }
        RenFactura other = (RenFactura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.RenFactura[ id=" + id + " ]";
    }

}
