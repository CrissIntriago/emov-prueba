/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.entities;

import com.origami.sigef.ats.modelAts.Compras;
import com.origami.sigef.ats.modelAts.DetalleVentas;
import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.ats.modelAts.Ventas;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.contabilidad.model.TalonResumenModel;
import com.origami.sigef.contabilidad.model.VentaEst;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author gutya
 */
@Entity
@Table(schema = "tesoreria", name = "liquidacion")
@SqlResultSetMapping(name = "DetalleVentasValueMapping",
        classes = @ConstructorResult(targetClass = DetalleVentas.class,
                columns = {
                    @ColumnResult(name = "tpIdCliente", type = String.class)
                    ,@ColumnResult(name = "idCliente", type = String.class)
                    ,@ColumnResult(name = "tipoComprobante", type = String.class)
                    ,@ColumnResult(name = "tipoEmision", type = String.class)
                    ,@ColumnResult(name = "numeroComprobantes", type = BigInteger.class)
                    ,@ColumnResult(name = "baseImponible", type = BigDecimal.class)
                    ,@ColumnResult(name = "baseImpGrav", type = BigDecimal.class)
                    ,@ColumnResult(name = "montoIva", type = BigDecimal.class)
                })
)
@SqlResultSetMapping(name = "TalonResumenMapping",
        classes = @ConstructorResult(targetClass = TalonResumenModel.class,
                columns = {
                    @ColumnResult(name = "liquidacionId", type = Long.class)
                    ,@ColumnResult(name = "facturaId", type = Long.class)
                    ,@ColumnResult(name = "codCompra", type = String.class)
                    ,@ColumnResult(name = "transaccion", type = String.class)
                    ,@ColumnResult(name = "bi_tarifa0", type = BigDecimal.class)
                    ,@ColumnResult(name = "bi_tarifa_diferente0", type = BigDecimal.class)
                    ,@ColumnResult(name = "bi_no_objetivoIva", type = BigDecimal.class)
                    ,@ColumnResult(name = "valor_iva", type = BigDecimal.class)
                })
)
@SqlResultSetMapping(name = "EstablecimientoValueMapping",
        classes = @ConstructorResult(targetClass = VentaEst.class,
                columns = {
                    @ColumnResult(name = "codEstab", type = String.class)
                    ,@ColumnResult(name = "ventasEstab", type = BigDecimal.class)
                    ,@ColumnResult(name = "ivaComp", type = BigDecimal.class)
                })
)
public class Liquidacion implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    private List<LiquidacionDetalle> liquidacionDetalles;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    private List<LiquidacionMotivo> liquidacionMotivos;
    @JoinColumn(name = "historico_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private HistoricoTramites historicoTramite;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private LiquidacionTipo tipoLiquidacion;
    @JoinColumn(name = "solicitante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Proveedor solicitante;
    @JoinColumn(name = "beneficiario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Proveedor beneficiario;
    @JoinColumn(name = "estado_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPago;
    @JoinColumn(name = "estado_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoLiquidacion;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "numero_comprobante")
    private BigInteger numeroComprobante;
    @Column(name = "codigo_comprobante")
    private String codigoComprobante;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @Column(name = "total_pagar")
    private BigDecimal totalPagar;
    @Column(name = "descuento_valor")
    private BigDecimal descuentoValor;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "periodo")
    private String periodo;
    @Column(name = "clave_acceso")
    private String claveAcceso;
    @Column(name = "numero_autorizacion")
    private String numeroAutorizacion;
    @Column(name = "estado_ws")
    private String estadoWs;
    @Column(name = "mensaje_ws")
    private String mensajeWs;
    @Column(name = "ride_enviado")
    private Boolean rideEnviado;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @Column(name = "fecha_comprobante_retenido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaComprobanteRetenido;
    @Column(name = "codigo_comprobante_retenido")
    private String codigoComprobanteRetenido;
    @Column(name = "genera_factura")
    private Boolean generaFactura;
    @Column(name = "tramite_online")
    private Boolean tramiteOnline;
    @Column(name = "considera_iva")
    private Boolean consideraIva;
    @Column(name = "reingreso")
    private Boolean reingreso;
    @Column(name = "ingresado")
    private Boolean ingresado;
    @Column(name = "peso_tramite")
    private Integer pesoTramite;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "user_anula")
    private Long userAnula;
    @Column(name = "user_creador")
    private Long userCreador;
    @Column(name = "user_edicion")
    private Long userEdicion;
    @Column(name = "user_ingreso")
    private Long userIngreso;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Column(name = "id_liquidacion_referencia")
    private Long idLiquidacionRefenrencia;
    @Column(name = "verificada_ws")
    private String verificadaWs;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "tipo_emision")
    private String tipoEmision;
    @JoinColumn(name = "comprobante", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Comprobantes comprobante;
    @JoinColumn(name = "comprobante_modifica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Comprobantes comprobanteModifica;
    @JoinColumn(name = "diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral diarioGeneral;
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente cliente;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "estado_verificado")
    private Boolean estadoVerificado;
    @Column(name = "monto_iva")
    private BigDecimal montoIva = BigDecimal.ZERO;
    @Column(name = "detalle_compras")
    private String detalleCompras;

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

    public Liquidacion(Long id) {
        this.id = id;
    }

    public Liquidacion() {
        this.ingresado = Boolean.FALSE;
        this.reingreso = Boolean.FALSE;
        this.consideraIva = Boolean.FALSE;
        this.tramiteOnline = Boolean.FALSE;
        this.generaFactura = Boolean.TRUE;
        this.numeroComprobante = BigInteger.ZERO;
        this.rideEnviado = Boolean.FALSE;
        this.pesoTramite = 0;
        this.estado = Boolean.FALSE;
        this.estadoVerificado = Boolean.FALSE;
    }

    public Liquidacion(LiquidacionTipo tipoLiquidacion, Proveedor solicitante,
            Proveedor beneficiario, CatalogoItem estadoPago, CatalogoItem estadoLiquidacion,
            BigInteger numeroComprobante, String codigoComprobante, BigDecimal subTotal, BigDecimal totalPagar,
            BigDecimal descuentoValor, Integer anio, Integer mes, String periodo, Boolean rideEnviado, Boolean generaFactura,
            Boolean tramiteOnline, Boolean reingreso, Boolean ingresado, Integer pesoTramite, String tipoEmision,
            Boolean consideraIva, Comprobantes comprobante, Comprobantes comprobanteModifica, ContDiarioGeneral diarioGeneral) {
        this.tipoLiquidacion = tipoLiquidacion;
        this.solicitante = solicitante;
        this.beneficiario = beneficiario;
        this.estadoPago = estadoPago;
        this.estadoLiquidacion = estadoLiquidacion;
        this.numeroComprobante = numeroComprobante;
        this.codigoComprobante = codigoComprobante;
        this.subTotal = subTotal;
        this.totalPagar = totalPagar;
        this.descuentoValor = descuentoValor;
        this.anio = anio;
        this.mes = mes;
        this.periodo = periodo;
        this.rideEnviado = rideEnviado;
        this.generaFactura = generaFactura;
        this.tramiteOnline = tramiteOnline;
        this.reingreso = reingreso;
        this.ingresado = ingresado;
        this.pesoTramite = pesoTramite;
        this.tipoEmision = tipoEmision;
        this.consideraIva = consideraIva;
        this.comprobante = comprobante;
        this.comprobanteModifica = comprobanteModifica;
        this.diarioGeneral = diarioGeneral;
        // this.solicitanteAuxiliar = solicitanteAux;
    }

    public Date getFechaEmisionCabecera() {
        return fechaEmisionCabecera;
    }

    public void setFechaEmisionCabecera(Date fechaEmisionCabecera) {
        this.fechaEmisionCabecera = fechaEmisionCabecera;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(BigDecimal montoIva) {
        this.montoIva = montoIva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }

    public Ventas getVentas() {
        return ventas;
    }

    public void setVentas(Ventas ventas) {
        this.ventas = ventas;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ContDiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(ContDiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public HistoricoTramites getHistoricoTramite() {
        return historicoTramite;
    }

    public void setHistoricoTramite(HistoricoTramites historicoTramite) {
        this.historicoTramite = historicoTramite;
    }

    public LiquidacionTipo getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(LiquidacionTipo tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public Proveedor getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Proveedor solicitante) {
        this.solicitante = solicitante;
    }

    public List<LiquidacionMotivo> getLiquidacionMotivos() {
        return liquidacionMotivos;
    }

    public void setLiquidacionMotivos(List<LiquidacionMotivo> liquidacionMotivos) {
        this.liquidacionMotivos = liquidacionMotivos;
    }

    public Proveedor getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Proveedor beneficiario) {
        this.beneficiario = beneficiario;
    }

    public CatalogoItem getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(CatalogoItem estadoPago) {
        this.estadoPago = estadoPago;
    }

    public CatalogoItem getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(CatalogoItem estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public BigInteger getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(BigInteger numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getDescuentoValor() {
        return descuentoValor;
    }

    public void setDescuentoValor(BigDecimal descuentoValor) {
        this.descuentoValor = descuentoValor;
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

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public Boolean getRideEnviado() {
        return rideEnviado;
    }

    public void setRideEnviado(Boolean rideEnviado) {
        this.rideEnviado = rideEnviado;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public Boolean getGeneraFactura() {
        return generaFactura;
    }

    public void setGeneraFactura(Boolean generaFactura) {
        this.generaFactura = generaFactura;
    }

    public Boolean getTramiteOnline() {
        return tramiteOnline;
    }

    public void setTramiteOnline(Boolean tramiteOnline) {
        this.tramiteOnline = tramiteOnline;
    }

    public Boolean getReingreso() {
        return reingreso;
    }

    public void setReingreso(Boolean reingreso) {
        this.reingreso = reingreso;
    }

    public Boolean getIngresado() {
        return ingresado;
    }

    public void setIngresado(Boolean ingresado) {
        this.ingresado = ingresado;
    }

    public Integer getPesoTramite() {
        return pesoTramite;
    }

    public void setPesoTramite(Integer pesoTramite) {
        this.pesoTramite = pesoTramite;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getUserAnula() {
        return userAnula;
    }

    public void setUserAnula(Long userAnula) {
        this.userAnula = userAnula;
    }

    public Long getUserCreador() {
        return userCreador;
    }

    public void setUserCreador(Long userCreador) {
        this.userCreador = userCreador;
    }

    public Long getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(Long userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Long getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(Long userIngreso) {
        this.userIngreso = userIngreso;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public Long getIdLiquidacionRefenrencia() {
        return idLiquidacionRefenrencia;
    }

    public void setIdLiquidacionRefenrencia(Long idLiquidacionRefenrencia) {
        this.idLiquidacionRefenrencia = idLiquidacionRefenrencia;
    }

    public String getVerificadaWs() {
        return verificadaWs;
    }

    public void setVerificadaWs(String verificadaWs) {
        this.verificadaWs = verificadaWs;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public Comprobantes getComprobante() {
        if (comprobante == null) {
            comprobante = new Comprobantes();
        }
        return comprobante;
    }

    public void setComprobante(Comprobantes comprobante) {
        this.comprobante = comprobante;
    }

    public Boolean getConsideraIva() {
        return consideraIva;
    }

    public void setConsideraIva(Boolean consideraIva) {
        this.consideraIva = consideraIva;
    }

    public Boolean getEstadoVerificado() {
        return estadoVerificado;
    }

    public void setEstadoVerificado(Boolean estadoVerificado) {
        this.estadoVerificado = estadoVerificado;
    }

//    public Liquidacion getLiquidacionReferencia() {
//        return liquidacionReferencia;
//    }
//
//    public void setLiquidacionReferencia(Liquidacion liquidacionReferencia) {
//        this.liquidacionReferencia = liquidacionReferencia;
//    }
    public List<LiquidacionDetalle> getLiquidacionDetalles() {
        return liquidacionDetalles;
    }

    public void setLiquidacionDetalles(List<LiquidacionDetalle> liquidacionDetalles) {
        this.liquidacionDetalles = liquidacionDetalles;
    }

    public Comprobantes getComprobanteModifica() {
        return comprobanteModifica;
    }

    public void setComprobanteModifica(Comprobantes comprobanteModifica) {
        this.comprobanteModifica = comprobanteModifica;
    }

    public String getIdentificacionProveedor() {
        if (solicitante != null && solicitante.getCliente() != null) {
            return identificacionProveedor = solicitante.getCliente().getIdentificacionCompleta();
        }
        return identificacionProveedor;
    }

    public void setIdentificacionProveedor(String identificacionProveedor) {
        this.identificacionProveedor = identificacionProveedor;
    }

    public Date getFechaComprobanteRetenido() {
        return fechaComprobanteRetenido;
    }

    public void setFechaComprobanteRetenido(Date fechaComprobanteRetenido) {
        this.fechaComprobanteRetenido = fechaComprobanteRetenido;
    }

    public String getCodigoComprobanteRetenido() {
        return codigoComprobanteRetenido;
    }

    public void setCodigoComprobanteRetenido(String codigoComprobanteRetenido) {
        this.codigoComprobanteRetenido = codigoComprobanteRetenido;
    }

    public String getDetalleCompras() {
        return detalleCompras;
    }

    public void setDetalleCompras(String detalleCompras) {
        this.detalleCompras = detalleCompras;
    }

    public IVA getAts() {
        return ats;
    }

    public void setAts(IVA ats) {
        this.ats = ats;
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
        if (!(object instanceof Liquidacion)) {
            return false;
        }
        Liquidacion other = (Liquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Liquidacion[ id=" + id + " ]";
    }

}
