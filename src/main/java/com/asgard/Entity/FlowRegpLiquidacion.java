/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "flow_regp_liquidacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowRegpLiquidacion.findAll", query = "SELECT f FROM FlowRegpLiquidacion f"),
    @NamedQuery(name = "FlowRegpLiquidacion.findById", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.id = :id"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByCodigoVerificacion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.codigoVerificacion = :codigoVerificacion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByTramite", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.tramite = :tramite"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByObservacion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByIsRegistroPropiedad", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.isRegistroPropiedad = :isRegistroPropiedad"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByGastosGenerales", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.gastosGenerales = :gastosGenerales"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByDescuentoPorc", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.descuentoPorc = :descuentoPorc"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByDescuentoValor", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.descuentoValor = :descuentoValor"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByNumTramiteRp", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.numTramiteRp = :numTramiteRp"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByTotalPagar", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.totalPagar = :totalPagar"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByReingreso", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.reingreso = :reingreso"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByInscripcion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.inscripcion = :inscripcion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByCertificado", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.certificado = :certificado"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByNumeroComprobante", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.numeroComprobante = :numeroComprobante"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByValorActos", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.valorActos = :valorActos"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByIngresado", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.ingresado = :ingresado"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByDescLimitCobro", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.descLimitCobro = :descLimitCobro"),
    @NamedQuery(name = "FlowRegpLiquidacion.findBySubTotal", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.subTotal = :subTotal"),
    @NamedQuery(name = "FlowRegpLiquidacion.findBySolicitante", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.solicitante = :solicitante"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByUsoDocumento", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.usoDocumento = :usoDocumento"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByUserCreacion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.userCreacion = :userCreacion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByFechaCreacion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByUserEdicion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.userEdicion = :userEdicion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByFechaEdicion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.fechaEdicion = :fechaEdicion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByUserIngreso", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.userIngreso = :userIngreso"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByFechaIngreso", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByClaveAcceso", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.claveAcceso = :claveAcceso"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByCodigoComprobante", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.codigoComprobante = :codigoComprobante"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByEscritJuicProvResolucion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.escritJuicProvResolucion = :escritJuicProvResolucion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByRepertorio", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.repertorio = :repertorio"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByInscriptor", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.inscriptor = :inscriptor"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByFechaAutorizacion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.fechaAutorizacion = :fechaAutorizacion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByRideEnviado", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.rideEnviado = :rideEnviado"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByNumeroAutorizacion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.numeroAutorizacion = :numeroAutorizacion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByUserAnular", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.userAnular = :userAnular"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByFechaAnulacion", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.fechaAnulacion = :fechaAnulacion"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByInfAdicionalProf", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.infAdicionalProf = :infAdicionalProf"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByEstadoWs", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.estadoWs = :estadoWs"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByMensajeWs", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.mensajeWs = :mensajeWs"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByOficioMemoReferencia", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.oficioMemoReferencia = :oficioMemoReferencia"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByBeneficiario", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.beneficiario = :beneficiario"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByGeneraFactura", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.generaFactura = :generaFactura"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByEsJuridico", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.esJuridico = :esJuridico"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByAdicional", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.adicional = :adicional"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByPesoTramite", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.pesoTramite = :pesoTramite"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByTramiteInstitucional", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.tramiteInstitucional = :tramiteInstitucional"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByTramiteOnline", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.tramiteOnline = :tramiteOnline"),
    @NamedQuery(name = "FlowRegpLiquidacion.findByNumTramiteSd", query = "SELECT f FROM FlowRegpLiquidacion f WHERE f.numTramiteSd = :numTramiteSd")})
public class FlowRegpLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "codigo_verificacion")
    private String codigoVerificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tramite")
    private long tramite;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "is_registro_propiedad")
    private Boolean isRegistroPropiedad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "gastos_generales")
    private BigDecimal gastosGenerales;
    @Column(name = "descuento_porc")
    private BigDecimal descuentoPorc;
    @Column(name = "descuento_valor")
    private BigDecimal descuentoValor;
    @Column(name = "num_tramite_rp")
    private BigInteger numTramiteRp;
    @Column(name = "total_pagar")
    private BigDecimal totalPagar;
    @Column(name = "reingreso")
    private Boolean reingreso;
    @Column(name = "inscripcion")
    private Boolean inscripcion;
    @Column(name = "certificado")
    private Boolean certificado;
    @Column(name = "numero_comprobante")
    private BigInteger numeroComprobante;
    @Column(name = "valor_actos")
    private BigDecimal valorActos;
    @Column(name = "ingresado")
    private Boolean ingresado;
    @Column(name = "desc_limit_cobro")
    private BigDecimal descLimitCobro;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @Column(name = "solicitante")
    private BigInteger solicitante;
    @Column(name = "uso_documento")
    private BigInteger usoDocumento;
    @Column(name = "user_creacion")
    private BigInteger userCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "user_edicion")
    private BigInteger userEdicion;
    @Column(name = "fecha_edicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEdicion;
    @Column(name = "user_ingreso")
    private BigInteger userIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 100)
    @Column(name = "clave_acceso")
    private String claveAcceso;
    @Size(max = 100)
    @Column(name = "codigo_comprobante")
    private String codigoComprobante;
    @Size(max = 2147483647)
    @Column(name = "escrit_juic_prov_resolucion")
    private String escritJuicProvResolucion;
    @Column(name = "repertorio")
    private Integer repertorio;
    @Column(name = "inscriptor")
    private BigInteger inscriptor;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;
    @Column(name = "ride_enviado")
    private Boolean rideEnviado;
    @Size(max = 100)
    @Column(name = "numero_autorizacion")
    private String numeroAutorizacion;
    @Column(name = "user_anular")
    private BigInteger userAnular;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Size(max = 2147483647)
    @Column(name = "inf_adicional_prof")
    private String infAdicionalProf;
    @Size(max = 2147483647)
    @Column(name = "estado_ws")
    private String estadoWs;
    @Size(max = 2147483647)
    @Column(name = "mensaje_ws")
    private String mensajeWs;
    @Size(max = 500)
    @Column(name = "oficio_memo_referencia")
    private String oficioMemoReferencia;
    @Column(name = "beneficiario")
    private BigInteger beneficiario;
    @Column(name = "genera_factura")
    private Boolean generaFactura;
    @Column(name = "es_juridico")
    private Boolean esJuridico;
    @Column(name = "adicional")
    private BigDecimal adicional;
    @Column(name = "peso_tramite")
    private Integer pesoTramite;
    @Column(name = "tramite_institucional")
    private Boolean tramiteInstitucional;
    @Column(name = "tramite_online")
    private Boolean tramiteOnline;
    @Column(name = "num_tramite_sd")
    private BigInteger numTramiteSd;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FlowConvenioBanco> flowConvenioBancoList;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPago> finaRenPagoList;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FlowRegpObservacionesIngreso> flowRegpObservacionesIngresoList;
    @JoinColumn(name = "estado_liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FlowRegpEstadoLiquidacion estadoLiquidacion;
    @JoinColumn(name = "estado_pago", referencedColumnName = "id")
    @ManyToOne
    private FlowRegpEstadoPago estadoPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "liquidacion")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FlowRegpLiquidacionDetalles> flowRegpLiquidacionDetallesList;

    public FlowRegpLiquidacion() {
    }

    public FlowRegpLiquidacion(Long id) {
        this.id = id;
    }

    public FlowRegpLiquidacion(Long id, long tramite) {
        this.id = id;
        this.tramite = tramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public long getTramite() {
        return tramite;
    }

    public void setTramite(long tramite) {
        this.tramite = tramite;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getIsRegistroPropiedad() {
        return isRegistroPropiedad;
    }

    public void setIsRegistroPropiedad(Boolean isRegistroPropiedad) {
        this.isRegistroPropiedad = isRegistroPropiedad;
    }

    public BigDecimal getGastosGenerales() {
        return gastosGenerales;
    }

    public void setGastosGenerales(BigDecimal gastosGenerales) {
        this.gastosGenerales = gastosGenerales;
    }

    public BigDecimal getDescuentoPorc() {
        return descuentoPorc;
    }

    public void setDescuentoPorc(BigDecimal descuentoPorc) {
        this.descuentoPorc = descuentoPorc;
    }

    public BigDecimal getDescuentoValor() {
        return descuentoValor;
    }

    public void setDescuentoValor(BigDecimal descuentoValor) {
        this.descuentoValor = descuentoValor;
    }

    public BigInteger getNumTramiteRp() {
        return numTramiteRp;
    }

    public void setNumTramiteRp(BigInteger numTramiteRp) {
        this.numTramiteRp = numTramiteRp;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public Boolean getReingreso() {
        return reingreso;
    }

    public void setReingreso(Boolean reingreso) {
        this.reingreso = reingreso;
    }

    public Boolean getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Boolean inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Boolean getCertificado() {
        return certificado;
    }

    public void setCertificado(Boolean certificado) {
        this.certificado = certificado;
    }

    public BigInteger getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(BigInteger numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public BigDecimal getValorActos() {
        return valorActos;
    }

    public void setValorActos(BigDecimal valorActos) {
        this.valorActos = valorActos;
    }

    public Boolean getIngresado() {
        return ingresado;
    }

    public void setIngresado(Boolean ingresado) {
        this.ingresado = ingresado;
    }

    public BigDecimal getDescLimitCobro() {
        return descLimitCobro;
    }

    public void setDescLimitCobro(BigDecimal descLimitCobro) {
        this.descLimitCobro = descLimitCobro;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigInteger getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(BigInteger solicitante) {
        this.solicitante = solicitante;
    }

    public BigInteger getUsoDocumento() {
        return usoDocumento;
    }

    public void setUsoDocumento(BigInteger usoDocumento) {
        this.usoDocumento = usoDocumento;
    }

    public BigInteger getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(BigInteger userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigInteger getUserEdicion() {
        return userEdicion;
    }

    public void setUserEdicion(BigInteger userEdicion) {
        this.userEdicion = userEdicion;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public BigInteger getUserIngreso() {
        return userIngreso;
    }

    public void setUserIngreso(BigInteger userIngreso) {
        this.userIngreso = userIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public String getEscritJuicProvResolucion() {
        return escritJuicProvResolucion;
    }

    public void setEscritJuicProvResolucion(String escritJuicProvResolucion) {
        this.escritJuicProvResolucion = escritJuicProvResolucion;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public BigInteger getInscriptor() {
        return inscriptor;
    }

    public void setInscriptor(BigInteger inscriptor) {
        this.inscriptor = inscriptor;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public Boolean getRideEnviado() {
        return rideEnviado;
    }

    public void setRideEnviado(Boolean rideEnviado) {
        this.rideEnviado = rideEnviado;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public BigInteger getUserAnular() {
        return userAnular;
    }

    public void setUserAnular(BigInteger userAnular) {
        this.userAnular = userAnular;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public String getInfAdicionalProf() {
        return infAdicionalProf;
    }

    public void setInfAdicionalProf(String infAdicionalProf) {
        this.infAdicionalProf = infAdicionalProf;
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

    public String getOficioMemoReferencia() {
        return oficioMemoReferencia;
    }

    public void setOficioMemoReferencia(String oficioMemoReferencia) {
        this.oficioMemoReferencia = oficioMemoReferencia;
    }

    public BigInteger getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(BigInteger beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Boolean getGeneraFactura() {
        return generaFactura;
    }

    public void setGeneraFactura(Boolean generaFactura) {
        this.generaFactura = generaFactura;
    }

    public Boolean getEsJuridico() {
        return esJuridico;
    }

    public void setEsJuridico(Boolean esJuridico) {
        this.esJuridico = esJuridico;
    }

    public BigDecimal getAdicional() {
        return adicional;
    }

    public void setAdicional(BigDecimal adicional) {
        this.adicional = adicional;
    }

    public Integer getPesoTramite() {
        return pesoTramite;
    }

    public void setPesoTramite(Integer pesoTramite) {
        this.pesoTramite = pesoTramite;
    }

    public Boolean getTramiteInstitucional() {
        return tramiteInstitucional;
    }

    public void setTramiteInstitucional(Boolean tramiteInstitucional) {
        this.tramiteInstitucional = tramiteInstitucional;
    }

    public Boolean getTramiteOnline() {
        return tramiteOnline;
    }

    public void setTramiteOnline(Boolean tramiteOnline) {
        this.tramiteOnline = tramiteOnline;
    }

    public BigInteger getNumTramiteSd() {
        return numTramiteSd;
    }

    public void setNumTramiteSd(BigInteger numTramiteSd) {
        this.numTramiteSd = numTramiteSd;
    }

    
    public List<FlowConvenioBanco> getFlowConvenioBancoList() {
        return flowConvenioBancoList;
    }

    public void setFlowConvenioBancoList(List<FlowConvenioBanco> flowConvenioBancoList) {
        this.flowConvenioBancoList = flowConvenioBancoList;
    }

    
    public List<FinaRenPago> getFinaRenPagoList() {
        return finaRenPagoList;
    }

    public void setFinaRenPagoList(List<FinaRenPago> finaRenPagoList) {
        this.finaRenPagoList = finaRenPagoList;
    }

    
    public List<FlowRegpObservacionesIngreso> getFlowRegpObservacionesIngresoList() {
        return flowRegpObservacionesIngresoList;
    }

    public void setFlowRegpObservacionesIngresoList(List<FlowRegpObservacionesIngreso> flowRegpObservacionesIngresoList) {
        this.flowRegpObservacionesIngresoList = flowRegpObservacionesIngresoList;
    }

    public FlowRegpEstadoLiquidacion getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(FlowRegpEstadoLiquidacion estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public FlowRegpEstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(FlowRegpEstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    
    public List<FlowRegpLiquidacionDetalles> getFlowRegpLiquidacionDetallesList() {
        return flowRegpLiquidacionDetallesList;
    }

    public void setFlowRegpLiquidacionDetallesList(List<FlowRegpLiquidacionDetalles> flowRegpLiquidacionDetallesList) {
        this.flowRegpLiquidacionDetallesList = flowRegpLiquidacionDetallesList;
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
        if (!(object instanceof FlowRegpLiquidacion)) {
            return false;
        }
        FlowRegpLiquidacion other = (FlowRegpLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowRegpLiquidacion[ id=" + id + " ]";
    }
    
}
