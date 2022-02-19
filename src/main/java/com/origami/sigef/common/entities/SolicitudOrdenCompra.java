/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "solicitud_orden_compra", schema = "administracion")
@NamedQueries({
    @NamedQuery(name = "SolicitudOrdenCompra.findAll", query = "SELECT s FROM SolicitudOrdenCompra s"),
    @NamedQuery(name = "SolicitudOrdenCompra.findById", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.id = :id"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByCodigoOrden", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.codigoOrden = :codigoOrden"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByObjetoContratacion", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.objetoContratacion = :objetoContratacion"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByNuevoConcepto", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.nuevoConcepto = :nuevoConcepto"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByDetalleConcepto", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.detalleConcepto = :detalleConcepto"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByDiaPlazo", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.diaPlazo = :diaPlazo"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByObligaciones", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.obligaciones = :obligaciones"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByEstado", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.estado = :estado"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByUsuarioModifica", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByFechaModifica", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.fechaModifica = :fechaModifica"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByUsuarioCreacion", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "SolicitudOrdenCompra.findByFechaCreacion", query = "SELECT s FROM SolicitudOrdenCompra s WHERE s.fechaCreacion = :fechaCreacion")})
public class SolicitudOrdenCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "codigo_orden")
    private String codigoOrden;
    @Size(max = 2147483647)
    @Column(name = "objeto_contratacion")
    private String objetoContratacion;
    @Size(max = 2147483647)
    @Column(name = "nuevo_concepto")
    private String nuevoConcepto;
    @Size(max = 2147483647)
    @Column(name = "detalle_concepto")
    private String detalleConcepto;
    @Column(name = "dia_plazo")
    private Short diaPlazo;
    @Column(name = "obligaciones")
    private Boolean obligaciones;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_elaboracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaElaboracion;
    @Column(name = "fecha_aceptacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAceptacion;
    @Size(max = 2147483647)
    @Column(name = "detalle_garantia")
    private String detalleGarantia;
    @Size(max = 2147483647)
    @Column(name = "detalle_obligacion")
    private String detalleObligacion;
    @Size(max = 2147483647)
    @Column(name = "multa")
    private String multa;
    @Size(max = 2147483647)
    @Column(name = "detalle_consideracion")
    private String detalleConsideracion; //Observaciones
    @Size(max = 2147483647)
    @Column(name = "detalle_valor_adjudicado")
    private String detalleValorAdjudicado;
    @Size(max = 2147483647)
    @Column(name = "plazo_entrerga")
    private String plazoEntrerga;
    @Size(max = 2147483647)
    @Column(name = "condicion_plazo")
    private String condicionPlazo;
    @Size(max = 2147483647)
    @Column(name = "detalle_entrega")
    private String detalleEntrega;
    @Size(max = 2147483647)
    @Column(name = "detalle_vigencia")
    private String detalleVigencia;
    @Size(max = 2147483647)
    @Column(name = "detalle_controversia")
    private String detalleControversia;
    @Size(max = 2147483647)
    @Column(name = "detalle_terminacion_orden")
    private String detalleTerminacionOrden;
    @Size(max = 2147483647)
    @Column(name = "detalle_certificacion")
    private String detalleCertificacion;
    @Size(max = 2147483647)
    @Column(name = "detalle_presupuesto")
    private String detallePresupuesto;
    @Size(max = 2147483647)
    @Column(name = "detalle_notificacion")
    private String detalleNotificacion;
    @Column(name = "numero_tramite")
    private BigInteger numeroTramite;
    @Size(max = 2147483647)
    @Column(name = "elaboracion_doc")
    private String elaboracionDoc;
    @Size(max = 2147483647)
    @Column(name = "email_solicitante")
    private String emailSolicitante;
    @Size(max = 2147483647)
    @Column(name = "codigo_proceso")
    private String codigoProceso;
    @Column(name = "url_proceso_contratacion")
    private String urlProcesoContratacion;
    @Column(name = "id_tramite")
    private Long idTramite;

    @OneToMany(mappedBy = "ordenCompra", fetch = FetchType.LAZY)
    private List<EspecificacionTecnica> especificacionTecnicaList;
    @OneToMany(mappedBy = "ordenCompra", fetch = FetchType.LAZY)
    private List<CaracteristicaTecnica> caracteristicaTecnicaList;
    @OneToMany(mappedBy = "ordenCompra", fetch = FetchType.LAZY)
    private List<DetallePresupuesto> detallePresupuestosList;
    @OneToMany(mappedBy = "ordenCompra", fetch = FetchType.LAZY)
    private List<ObligacionResponsable> obligacionResponsableList;

    @JoinColumn(name = "unidad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidad;
    @JoinColumn(name = "tipo_plazo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoPlazo;
    @JoinColumn(name = "autorizador", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor autorizador;
    @JoinColumn(name = "reserva_compromiso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudReservaCompromiso reservaCompromiso;
    @JoinColumn(name = "adquisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Adquisiciones adquisicion;
    @JoinColumn(name = "estado_proceso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoProceso;
    @Column(name = "procesado")
    private Boolean procesado;
    @Column(name = "num_cotizacion")
    private String numCotizacion;

    @Column(name = "valor_subtotal")
    private BigDecimal valorSubtotal;
    @Column(name = "aplica_iva")
    private Boolean aplicaIva;
    @Column(name = "aplica_impuesto")
    private Boolean aplicaImpuesto;
    @Column(name = "valor_impuesto")
    private BigDecimal valorImpuesto;

    @Column(name = "aplica_credito")
    private Boolean aplicaCredito;

    public SolicitudOrdenCompra() {
        this.estado = Boolean.TRUE;
    }

    public SolicitudOrdenCompra(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoOrden() {
        return codigoOrden;
    }

    public void setCodigoOrden(String codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public String getObjetoContratacion() {
        return objetoContratacion;
    }

    public void setObjetoContratacion(String objetoContratacion) {
        this.objetoContratacion = objetoContratacion;
    }

    public String getNuevoConcepto() {
        return nuevoConcepto;
    }

    public void setNuevoConcepto(String nuevoConcepto) {
        this.nuevoConcepto = nuevoConcepto;
    }

    public String getDetalleConcepto() {
        return detalleConcepto;
    }

    public void setDetalleConcepto(String detalleConcepto) {
        this.detalleConcepto = detalleConcepto;
    }

    public Short getDiaPlazo() {
        return diaPlazo;
    }

    public void setDiaPlazo(Short diaPlazo) {
        this.diaPlazo = diaPlazo;
    }

    public Boolean getObligaciones() {
        return obligaciones;
    }

    public void setObligaciones(Boolean obligaciones) {
        this.obligaciones = obligaciones;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
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

    public List<EspecificacionTecnica> getEspecificacionTecnicaList() {
        return especificacionTecnicaList;
    }

    public void setEspecificacionTecnicaList(List<EspecificacionTecnica> especificacionTecnicaList) {
        this.especificacionTecnicaList = especificacionTecnicaList;
    }

    public List<CaracteristicaTecnica> getCaracteristicaTecnicaList() {
        return caracteristicaTecnicaList;
    }

    public void setCaracteristicaTecnicaList(List<CaracteristicaTecnica> caracteristicaTecnicaList) {
        this.caracteristicaTecnicaList = caracteristicaTecnicaList;
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public CatalogoItem getTipoPlazo() {
        return tipoPlazo;
    }

    public void setTipoPlazo(CatalogoItem tipoPlazo) {
        this.tipoPlazo = tipoPlazo;
    }

    public Servidor getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(Servidor autorizador) {
        this.autorizador = autorizador;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getDetalleGarantia() {
        return detalleGarantia;
    }

    public void setDetalleGarantia(String detalleGarantia) {
        this.detalleGarantia = detalleGarantia;
    }

    public String getDetalleObligacion() {
        return detalleObligacion;
    }

    public void setDetalleObligacion(String detalleObligacion) {
        this.detalleObligacion = detalleObligacion;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public SolicitudReservaCompromiso getReservaCompromiso() {
        return reservaCompromiso;
    }

    public void setReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso) {
        this.reservaCompromiso = reservaCompromiso;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public String getDetalleConsideracion() {
        return detalleConsideracion;
    }

    public void setDetalleConsideracion(String detalleConsideracion) {
        this.detalleConsideracion = detalleConsideracion;
    }

    public String getDetalleValorAdjudicado() {
        return detalleValorAdjudicado;
    }

    public void setDetalleValorAdjudicado(String detalleValorAdjudicado) {
        this.detalleValorAdjudicado = detalleValorAdjudicado;
    }

    public String getPlazoEntrerga() {
        return plazoEntrerga;
    }

    public void setPlazoEntrerga(String plazoEntrerga) {
        this.plazoEntrerga = plazoEntrerga;
    }

    public String getCondicionPlazo() {
        return condicionPlazo;
    }

    public void setCondicionPlazo(String condicionPlazo) {
        this.condicionPlazo = condicionPlazo;
    }

    public String getDetalleEntrega() {
        return detalleEntrega;
    }

    public void setDetalleEntrega(String detalleEntrega) {
        this.detalleEntrega = detalleEntrega;
    }

    public String getDetalleVigencia() {
        return detalleVigencia;
    }

    public void setDetalleVigencia(String detalleVigencia) {
        this.detalleVigencia = detalleVigencia;
    }

    public String getDetalleControversia() {
        return detalleControversia;
    }

    public void setDetalleControversia(String detalleControversia) {
        this.detalleControversia = detalleControversia;
    }

    public String getDetalleTerminacionOrden() {
        return detalleTerminacionOrden;
    }

    public void setDetalleTerminacionOrden(String detalleTerminacionOrden) {
        this.detalleTerminacionOrden = detalleTerminacionOrden;
    }

    public String getDetalleCertificacion() {
        return detalleCertificacion;
    }

    public void setDetalleCertificacion(String detalleCertificacion) {
        this.detalleCertificacion = detalleCertificacion;
    }

    public String getDetallePresupuesto() {
        return detallePresupuesto;
    }

    public void setDetallePresupuesto(String detallePresupuesto) {
        this.detallePresupuesto = detallePresupuesto;
    }

    public String getDetalleNotificacion() {
        return detalleNotificacion;
    }

    public void setDetalleNotificacion(String detalleNotificacion) {
        this.detalleNotificacion = detalleNotificacion;
    }

    public CatalogoItem getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(CatalogoItem estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public BigInteger getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(BigInteger numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public String getElaboracionDoc() {
        return elaboracionDoc;
    }

    public void setElaboracionDoc(String elaboracionDoc) {
        this.elaboracionDoc = elaboracionDoc;
    }

    public String getEmailSolicitante() {
        return emailSolicitante;
    }

    public void setEmailSolicitante(String emailSolicitante) {
        this.emailSolicitante = emailSolicitante;
    }

    public Date getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(Date fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }

    public String getCodigoProceso() {
        return codigoProceso;
    }

    public void setCodigoProceso(String codigoProceso) {
        this.codigoProceso = codigoProceso;
    }

    public String getUrlProcesoContratacion() {
        return urlProcesoContratacion;
    }

    public void setUrlProcesoContratacion(String urlProcesoContratacion) {
        this.urlProcesoContratacion = urlProcesoContratacion;
    }

    public Long getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Long idTramite) {
        this.idTramite = idTramite;
    }

    public List<DetallePresupuesto> getDetallePresupuestosList() {
        return detallePresupuestosList;
    }

    public void setDetallePresupuestosList(List<DetallePresupuesto> detallePresupuestosList) {
        this.detallePresupuestosList = detallePresupuestosList;
    }

    public List<ObligacionResponsable> getObligacionResponsableList() {
        return obligacionResponsableList;
    }

    public void setObligacionResponsableList(List<ObligacionResponsable> obligacionResponsableList) {
        this.obligacionResponsableList = obligacionResponsableList;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public String getNumCotizacion() {
        return numCotizacion;
    }

    public void setNumCotizacion(String numCotizacion) {
        this.numCotizacion = numCotizacion;
    }

    public BigDecimal getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(BigDecimal valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public Boolean getAplicaIva() {
        return aplicaIva;
    }

    public void setAplicaIva(Boolean aplicaIva) {
        this.aplicaIva = aplicaIva;
    }

    public Boolean getAplicaImpuesto() {
        return aplicaImpuesto;
    }

    public void setAplicaImpuesto(Boolean aplicaImpuesto) {
        this.aplicaImpuesto = aplicaImpuesto;
    }

    public BigDecimal getValorImpuesto() {
        return valorImpuesto;
    }

    public void setValorImpuesto(BigDecimal valorImpuesto) {
        this.valorImpuesto = valorImpuesto;
    }

    public Boolean getAplicaCredito() {
        return aplicaCredito;
    }

    public void setAplicaCredito(Boolean aplicaCredito) {
        this.aplicaCredito = aplicaCredito;
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
        if (!(object instanceof SolicitudOrdenCompra)) {
            return false;
        }
        SolicitudOrdenCompra other = (SolicitudOrdenCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SolicitudOrdenCompra[ id=" + id + " ]";
    }

}
