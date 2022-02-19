/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.ParticipacionCantones;
import com.gestionTributaria.Entities.RenValoresPlusvalia;
import com.gestionTributaria.models.InteresRecargoDescuento;
import com.origami.sigef.arrendamiento.entities.Arrendatarios;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.common.util.VariableRecaudacion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Basic;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_liquidacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenLiquidacion.findAll", query = "SELECT f FROM FinaRenLiquidacion f"),
    @NamedQuery(name = "FinaRenLiquidacion.findById", query = "SELECT f FROM FinaRenLiquidacion f WHERE f.id=?1"),
    @NamedQuery(name = "FinaRenLiquidacion.findByIdLiquidacion", query = "SELECT l FROM FinaRenLiquidacion l WHERE l.idLiquidacion = ?1")})
@SqlResultSetMapping(name = "interesRecargoDescuentoMapping",
        classes = @ConstructorResult(targetClass = InteresRecargoDescuento.class,
                columns = {
                    @ColumnResult(name = "recargo", type = BigDecimal.class),
                    @ColumnResult(name = "descuento", type = BigDecimal.class),
                    @ColumnResult(name = "interes", type = BigDecimal.class)
                })
)
public class FinaRenLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 40)
    @Column(name = "id_liquidacion")
    private String idLiquidacion;
    @Size(max = 200)
    @Column(name = "ip_user_session")
    private String ipUserSession;
    @Column(name = "anio")
    private Integer anio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area_total")
    private BigDecimal areaTotal;
    @Column(name = "avaluo_municipal")
    private BigDecimal avaluoMunicipal;
    @Column(name = "avaluo_construccion")
    private BigDecimal avaluoConstruccion;
    @Column(name = "avaluo_solar")
    private BigDecimal avaluoSolar;
    @Column(name = "banda_impositiva")
    private BigDecimal bandaImpositiva;
    @Column(name = "base_imponible")
    private BigDecimal baseImponible;
    @Size(max = 40)
    @Column(name = "codigo_local")
    private String codigoLocal;
    @Column(name = "coactiva")
    private Boolean coactiva;
    @JoinColumn(name = "comprador", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente comprador;
    @JoinColumn(name = "convenio_pago", referencedColumnName = "id")
    @ManyToOne
    private FnConvenioPago convenioPago;
    @Column(name = "costo_adq")
    private BigDecimal costoAdq;
    @Column(name = "cuantia")
    private BigDecimal cuantia;
    @Column(name = "estado_coactiva")
    private Integer estadoCoactiva;
    @Size(max = 50)
    @Column(name = "estado_referencia")
    private String estadoReferencia;
    @Column(name = "codigo_verificador")
    private String codigoVerificador;
    @Column(name = "codigo_verificado")
    private Boolean codigoVerificado;

    @Size(max = 2147483647)
    @Column(name = "exoneracion_descripcion")
    private String exoneracionDescripcion;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @Column(name = "fecha_creacion_original")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacionOriginal;
    @Basic(optional = false)
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 500)
    @Column(name = "identificacion")
    private String identificacion;

    @JoinColumn(name = "local_comercial", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLocalComercial localComercial;
    @Size(max = 200)
    @Column(name = "mac_addres_usuario_ingreso")
    private String macAddresUsuarioIngreso;
    @Column(name = "migrado")
    private Boolean migrado;
    @Size(max = 450)
    @Column(name = "nombre_comprador")
    private String nombreComprador;
    @Column(name = "num_comprobante")
    private BigInteger numComprobante;
    @Column(name = "num_liquidacion")
    private BigInteger numLiquidacion;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "otros")
    private BigDecimal otros;
    @Column(name = "porciento_rebaja")
    private BigDecimal porcientoRebaja;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "t_titulo")
    private BigInteger tTitulo;
    @Column(name = "total_adicionales")
    private BigDecimal totalAdicionales;
    @Column(name = "total_pago")
    private BigDecimal totalPago;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Size(max = 500)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Size(max = 200)
    @Column(name = "usuario_anular")
    private String usuarioAnular;
    @Size(max = 40)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Size(max = 200)
    @Column(name = "usuario_valida")
    private String usuarioValida;
    @Column(name = "validada")
    private Boolean validada;
    @Column(name = "valor_exoneracion")
    private BigDecimal valorExoneracion;
    @Column(name = "valor_comercial")
    private BigDecimal valorComercial;
    @Column(name = "valor_compra")
    private BigDecimal valorCompra;
    @Column(name = "valor_catastral")
    private BigDecimal valorCatastral;
    @Column(name = "valor_hipoteca")
    private BigDecimal valorHipoteca;
    @Column(name = "valor_mejoras")
    private BigDecimal valorMejoras;
    @Column(name = "valor_nominal")
    private BigDecimal valorNominal;
    @Column(name = "valor_mora")
    private BigDecimal valorMora;
    @Column(name = "valor_venta")
    private BigDecimal valorVenta;
    @Column(name = "interes_temp")
    private BigDecimal interesTemp;
    @JoinColumn(name = "vendedor", referencedColumnName = "id")
    @ManyToOne
    private Cliente vendedor;
    @JoinColumn(name = "departamento_detalle_titulos", referencedColumnName = "id")
    @ManyToOne
    private AppDepartamentoDetalleTitulos departamentoDetalleTitulos;
    @JoinColumn(name = "estado_liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenEstadoLiquidacion estadoLiquidacion;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoLiquidacion tipoLiquidacion;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenDetLiquidacion> finaRenDetLiquidacionList;
    @Transient
    private BigDecimal pagoFinal = BigDecimal.ZERO;
    @Transient
    private BigDecimal recargo = BigDecimal.ZERO;
    @Transient
    private BigDecimal interes = BigDecimal.ZERO;
    @Transient
    private BigDecimal valorCoactiva = BigDecimal.ZERO;
    @Transient
    private BigDecimal descuento;
//    @Basic(fetch = FetchType.LAZY)
//    @Formula("(SELECT COALESCE((SELECT CASE WHEN j.numero_juicio IS NULL THEN 0.03 ELSE 0.10 END "
//            + "FROM sgm._predio jp "
//            + "INNER JOIN sgm. j ON j.id = jp.juicio "
//            + "WHERE jp.liquidacion = id AND jp.estado group by 1), 0.03))")
    @Transient
    private BigDecimal porcentajeCoactiva;
    @Column(name = "num_reporte")
    private String numReporte;
    @Column(name = "fecha_contrato_ant")
    @Temporal(TemporalType.DATE)
    private Date fechaContratoAnt;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @Where(clause = "estado AND pago_indebido = false ")
    @OrderBy("num_comprobante ASC")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPago> renPagoCollection;
    @Column(name = "vendedor_rural")
    private String vendedorRural;
    @Column(name = "vendedor_rural_identificacion")
    private String vendedorRuralIdentificacion;
    @JoinColumn(name = "patente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenPatente patente;
    @JoinColumn(name = "arriendo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Arrendatarios arriendo;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenFactura factura;
    @Column(name = "referencia")
    private Long referencia;
    @Column(name = "temporal")
    private Boolean temporal;
    @Column(name = "rentas")
    private Boolean rentas;
    @Column(name = "nombre_comprador_historic")
    private String nombreCompradorHistoric;
    @Column(name = "exonerado")
    private Boolean estaExonerado;
    @Column(name = "bombero")
    private Boolean bombero = false;
//    @Expose
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @OrderBy("valor ASC")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenDetLiquidacion> renDetLiquidacionCollection;

    @Column(name = "clave_catastral_rural")
    private String claveCatastralRural;

    @Column(name = "predio_rustico")
    private Long predioRustico;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "liquidacion")
    private RenValoresPlusvalia renValoresPlusvalia;
    @Column(name = "rural_excel")
    private Long ruralExcel;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<ParticipacionCantones> listDistribuciionCantones;
    @OneToMany(mappedBy = "liquidacionOriginal", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnExoneracionLiquidacion> exoneracionLiquidacionCollection;
    @OneToMany(mappedBy = "liquidacionPosterior", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FnExoneracionLiquidacion> exoneracionLiquidacionPosteriorCollection;
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Column(name = "predio_historico")
    private BigInteger predioHistorico;
    @Column(name = "nombre_documento")
    private String nombreDocumento;
    @Column(name = "direccion_documento")
    private String direccionDocumento;
    @Column(name = "num_boleta")
    private BigInteger numBoleta;
    @Column(name = "mes")
    private Long mes;
    @Column(name = "liquidador_responsable")
    private String liquidadorResponsable;
    @Column(name = "liquidador_aprobador")
    private String liquidadorAprobador;
    @Column(name = "pre_anualcion")
    private Boolean preAnulacion;
    @Column(name = "liquidacion_terreno")
    private Long liquidacionTerreno;
    @Transient
    private BigDecimal totalTmp;
    @Transient
    private BigDecimal descuentoBiess;

    @Column(name = "num_tramite")
    private Long numTramite;

    public FinaRenLiquidacion() {
        this.validada = Boolean.TRUE;
        codigoVerificado = Boolean.FALSE;
        anio = Utils.getAnio(new Date());
        preAnulacion = Boolean.FALSE;
        descuentoBiess = BigDecimal.ZERO;
    }

    public FinaRenLiquidacion(Long id) {
        this.id = id;
        anio = Utils.getAnio(new Date());
        preAnulacion = Boolean.FALSE;
    }

    public FinaRenLiquidacion(Long id, Date fechaIngreso) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        anio = Utils.getAnio(new Date());
        preAnulacion = Boolean.FALSE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(String idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public String getIpUserSession() {
        return ipUserSession;
    }

    public void setIpUserSession(String ipUserSession) {
        this.ipUserSession = ipUserSession;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(BigDecimal areaTotal) {
        this.areaTotal = areaTotal;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public BigDecimal getAvaluoConstruccion() {
        return avaluoConstruccion;
    }

    public void setAvaluoConstruccion(BigDecimal avaluoConstruccion) {
        this.avaluoConstruccion = avaluoConstruccion;
    }

    public BigDecimal getAvaluoSolar() {
        return avaluoSolar;
    }

    public void setAvaluoSolar(BigDecimal avaluoSolar) {
        this.avaluoSolar = avaluoSolar;
    }

    public BigDecimal getBandaImpositiva() {
        return bandaImpositiva;
    }

    public void setBandaImpositiva(BigDecimal bandaImpositiva) {
        this.bandaImpositiva = bandaImpositiva;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Boolean getBombero() {
        return bombero;
    }

    public void setBombero(Boolean bombero) {
        this.bombero = bombero;
    }

    public String getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(String codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public Boolean getCoactiva() {
        return coactiva;
    }

    public void setCoactiva(Boolean coactiva) {
        this.coactiva = coactiva;
    }

    public Cliente getComprador() {
        return comprador;
    }

    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    public BigInteger gettTitulo() {
        return tTitulo;
    }

    public void settTitulo(BigInteger tTitulo) {
        this.tTitulo = tTitulo;
    }

    public FnConvenioPago getConvenioPago() {
        return convenioPago;
    }

    public void setConvenioPago(FnConvenioPago convenioPago) {
        this.convenioPago = convenioPago;
    }

    public BigDecimal getCostoAdq() {
        return costoAdq;
    }

    public void setCostoAdq(BigDecimal costoAdq) {
        this.costoAdq = costoAdq;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public Integer getEstadoCoactiva() {
        return estadoCoactiva;
    }

    public void setEstadoCoactiva(Integer estadoCoactiva) {
        this.estadoCoactiva = estadoCoactiva;
    }

    public String getEstadoReferencia() {
        return estadoReferencia;
    }

    public void setEstadoReferencia(String estadoReferencia) {
        this.estadoReferencia = estadoReferencia;
    }

    public BigInteger getPredioHistorico() {
        return predioHistorico;
    }

    public void setPredioHistorico(BigInteger predioHistorico) {
        this.predioHistorico = predioHistorico;
    }

    public String getExoneracionDescripcion() {
        return exoneracionDescripcion;
    }

    public void setExoneracionDescripcion(String exoneracionDescripcion) {
        this.exoneracionDescripcion = exoneracionDescripcion;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public Date getFechaCreacionOriginal() {
        return fechaCreacionOriginal;
    }

    public void setFechaCreacionOriginal(Date fechaCreacionOriginal) {
        this.fechaCreacionOriginal = fechaCreacionOriginal;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public String getMacAddresUsuarioIngreso() {
        return macAddresUsuarioIngreso;
    }

    public void setMacAddresUsuarioIngreso(String macAddresUsuarioIngreso) {
        this.macAddresUsuarioIngreso = macAddresUsuarioIngreso;
    }

    public Boolean getMigrado() {
        return migrado;
    }

    public void setMigrado(Boolean migrado) {
        this.migrado = migrado;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public BigInteger getNumLiquidacion() {
        return numLiquidacion;
    }

    public void setNumLiquidacion(BigInteger numLiquidacion) {
        this.numLiquidacion = numLiquidacion;
    }

    public Long getPredioRustico() {
        return predioRustico;
    }

    public void setPredioRustico(Long predioRustico) {
        this.predioRustico = predioRustico;
    }

    public RenValoresPlusvalia getRenValoresPlusvalia() {
        return renValoresPlusvalia;
    }

    public void setRenValoresPlusvalia(RenValoresPlusvalia renValoresPlusvalia) {
        this.renValoresPlusvalia = renValoresPlusvalia;
    }

    public Long getRuralExcel() {
        return ruralExcel;
    }

    public void setRuralExcel(Long ruralExcel) {
        this.ruralExcel = ruralExcel;
    }

    public Long getMes() {
        return mes;
    }

    public void setMes(Long mes) {
        this.mes = mes;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getOtros() {
        return otros;
    }

    public void setOtros(BigDecimal otros) {
        this.otros = otros;
    }

    public FinaRenPatente getPatente() {
        return patente;
    }

    public void setPatente(FinaRenPatente patente) {
        this.patente = patente;
    }

    public BigDecimal getPorcientoRebaja() {
        return porcientoRebaja;
    }

    public void setPorcientoRebaja(BigDecimal porcientoRebaja) {
        this.porcientoRebaja = porcientoRebaja;
    }

    public CatPredio getPredio() {

        if (predio == null) {
            if (localComercial != null) {
                return new CatPredio(localComercial.getPredio(), localComercial.getClavePreial(), localComercial.getTipoPredio());

            }
            return new CatPredio();
        } else {

            return predio;
        }
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigInteger getTTitulo() {
        return tTitulo;
    }

    public void setTTitulo(BigInteger tTitulo) {
        this.tTitulo = tTitulo;
    }

    public BigDecimal getTotalAdicionales() {
        return totalAdicionales;
    }

    public void setTotalAdicionales(BigDecimal totalAdicionales) {
        this.totalAdicionales = totalAdicionales;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUsuarioAnular() {
        return usuarioAnular;
    }

    public void setUsuarioAnular(String usuarioAnular) {
        this.usuarioAnular = usuarioAnular;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public String getUsuarioValida() {
        return usuarioValida;
    }

    public void setUsuarioValida(String usuarioValida) {
        this.usuarioValida = usuarioValida;
    }

    public Boolean getValidada() {
        return validada;
    }

    public void setValidada(Boolean validada) {
        this.validada = validada;
    }

    public BigDecimal getValorExoneracion() {
        return valorExoneracion;
    }

    public void setValorExoneracion(BigDecimal valorExoneracion) {
        this.valorExoneracion = valorExoneracion;
    }

    public BigDecimal getValorComercial() {
        return valorComercial;
    }

    public void setValorComercial(BigDecimal valorComercial) {
        this.valorComercial = valorComercial;
    }

    public BigDecimal getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }

    public BigDecimal getValorCatastral() {
        return valorCatastral;
    }

    public void setValorCatastral(BigDecimal valorCatastral) {
        this.valorCatastral = valorCatastral;
    }

    public BigDecimal getValorHipoteca() {
        return valorHipoteca;
    }

    public void setValorHipoteca(BigDecimal valorHipoteca) {
        this.valorHipoteca = valorHipoteca;
    }

    public BigDecimal getValorMejoras() {
        return valorMejoras;
    }

    public void setValorMejoras(BigDecimal valorMejoras) {
        this.valorMejoras = valorMejoras;
    }

    public BigDecimal getValorNominal() {
        return valorNominal;
    }

    public void setValorNominal(BigDecimal valorNominal) {
        this.valorNominal = valorNominal;
    }

    public BigDecimal getValorMora() {
        return valorMora;
    }

    public void setValorMora(BigDecimal valorMora) {
        this.valorMora = valorMora;
    }

    public BigDecimal getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(BigDecimal valorVenta) {
        this.valorVenta = valorVenta;
    }

    public Cliente getVendedor() {
        return vendedor;
    }

    public void setVendedor(Cliente vendedor) {
        this.vendedor = vendedor;
    }

    public AppDepartamentoDetalleTitulos getDepartamentoDetalleTitulos() {
        return departamentoDetalleTitulos;
    }

    public void setDepartamentoDetalleTitulos(AppDepartamentoDetalleTitulos departamentoDetalleTitulos) {
        this.departamentoDetalleTitulos = departamentoDetalleTitulos;
    }

    public FinaRenEstadoLiquidacion getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(FinaRenEstadoLiquidacion estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public Arrendatarios getArriendo() {
        return arriendo;
    }

    public void setArriendo(Arrendatarios arriendo) {
        this.arriendo = arriendo;
    }

    public RenFactura getFactura() {
        return factura;
    }

    public void setFactura(RenFactura factura) {
        this.factura = factura;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public String getCodigoVerificador() {
        return codigoVerificador;
    }

    public void setCodigoVerificador(String codigoVerificador) {
        this.codigoVerificador = codigoVerificador;
    }

    public Boolean getCodigoVerificado() {
        return codigoVerificado;
    }

    public void setCodigoVerificado(Boolean codigoVerificado) {
        this.codigoVerificado = codigoVerificado;
    }

    public Boolean getPreAnulacion() {
        return preAnulacion;
    }

    public void setPreAnulacion(Boolean preAnulacion) {
        this.preAnulacion = preAnulacion;
    }

    public Long getLiquidacionTerreno() {
        return liquidacionTerreno;
    }

    public void setLiquidacionTerreno(Long liquidacionTerreno) {
        this.liquidacionTerreno = liquidacionTerreno;
    }

    @XmlTransient
    public List<FinaRenDetLiquidacion> getFinaRenDetLiquidacionList() {
        return finaRenDetLiquidacionList;
    }

    public void setFinaRenDetLiquidacionList(List<FinaRenDetLiquidacion> finaRenDetLiquidacionList) {
        this.finaRenDetLiquidacionList = finaRenDetLiquidacionList;
    }

    public void calcularPago() {
        if (this.saldo == null) {
            this.saldo = BigDecimal.ZERO;
        }
        if (this.recargo == null) {
            this.recargo = BigDecimal.ZERO;
        }
        if (this.descuento == null) {
            this.descuento = BigDecimal.ZERO;
        }
        if (this.interes == null) {
            this.interes = BigDecimal.ZERO;
        }
        if (this.interesTemp == null) {
            this.interesTemp = BigDecimal.ZERO;
        }

        if (estadoCoactiva != null && estadoCoactiva == 2 && this.coactiva) {
            BigDecimal subtract = this.saldo.add(this.recargo);
            this.valorCoactiva = Utils.bigdecimalTo2Decimals(subtract.multiply(new BigDecimal(VariableRecaudacion.COACTIVA).divide(new BigDecimal("100"))));
            this.pagoFinal = subtract.add(this.valorCoactiva).add(this.interes).subtract(this.descuento);
        } else {
            this.pagoFinal = this.saldo.add(this.recargo).add(this.interes).subtract(this.descuento);
        }
    }

    public BigDecimal getInteresTemp() {
        return interesTemp;
    }

    public void setInteresTemp(BigDecimal interesTemp) {
        this.interesTemp = interesTemp;
    }

    public void calcularCoactiva() {
        pagoFinal = saldo.add(recargo).subtract(descuento);
        BigDecimal subtract = pagoFinal.add(recargo);
        valorCoactiva = Utils.bigdecimalTo2Decimals(subtract.multiply(new BigDecimal(VariableRecaudacion.COACTIVA).divide(new BigDecimal("100"))));
        pagoFinal = pagoFinal.add(valorCoactiva).add(interes);
    }

    public void calcularPagoConCoactiva() {
//        this.pagoFinal = this.saldo.add(this.recargo).subtract(this.descuento).subtract(this.valorExoneracion == null ? BigDecimal.ZERO : this.valorExoneracion);
        if (this.saldo == null) {
            this.saldo = BigDecimal.ZERO;
        }
        if (this.recargo == null) {
            this.recargo = BigDecimal.ZERO;
        }
        if (this.descuento == null) {
            this.descuento = BigDecimal.ZERO;
        }
        if (this.estadoCoactiva != null && this.estadoCoactiva == 2) {
            Double total = 0.00;
            BigDecimal subtract = this.saldo.add(this.recargo);
            total = (VariableRecaudacion.COACTIVA / 100.00);
            this.valorCoactiva = Utils.bigdecimalTo2Decimals(subtract.multiply(BigDecimal.valueOf(total)));
            this.pagoFinal = subtract.add(this.valorCoactiva).add(this.interes);
        } else {
//            this.pagoFinal = this.pagoFinal.add(this.interes);
            this.pagoFinal = this.saldo.add(this.recargo).subtract(this.descuento).add(this.interes);
        }
    }

    public void calcularPagoAPConCoactiva() {
        if (this.valorExoneracion == null) {
            this.valorExoneracion = BigDecimal.ZERO;
        }
        this.pagoFinal = this.saldo.add(this.interes);
        if (this.estadoCoactiva != null && this.estadoCoactiva == 2) {
            this.valorCoactiva = Utils.bigdecimalTo2Decimals(this.pagoFinal.multiply(new BigDecimal(VariableRecaudacion.COACTIVA)).divide(new BigDecimal("100")));
            this.pagoFinal = this.pagoFinal.add(this.valorCoactiva);
        }
    }

    public Boolean calculoMinimoPago(BigDecimal valorTotal) {
        BigDecimal minimoPago = this.recargo.add(this.interes);
//        System.out.println("minimo pago>>" + minimoPago + " valor coactiva>>" + VariableRecaudacion.COACTIVA);
        if (this.estadoCoactiva != null && this.estadoCoactiva == 2) {
            minimoPago = minimoPago.add(Utils.bigdecimalTo2Decimals(minimoPago.multiply(new BigDecimal(VariableRecaudacion.COACTIVA).divide(new BigDecimal("100")))));
        }
        System.out.println("// minimo pago: " + minimoPago);
        return valorTotal.compareTo(minimoPago) <= 0;
    }

    public BigDecimal calculoMinimoPago() {
        BigDecimal minimoPago = this.recargo.add(this.interes);
//        System.out.println("minimo pago>>" + minimoPago + " valor coactiva>>" + VariableRecaudacion.COACTIVA);
        if (this.estadoCoactiva != null && this.estadoCoactiva == 2) {
            minimoPago = minimoPago.add(this.valorCoactiva);
        }
        System.out.println("// minimo pago: " + minimoPago);
        return minimoPago;
    }

    public BigInteger getNumBoleta() {
        return numBoleta;
    }

    public void setNumBoleta(BigInteger numBoleta) {
        this.numBoleta = numBoleta;
    }

    public void calcularPagoAP() {
        if (this.valorExoneracion == null) {
            this.valorExoneracion = BigDecimal.ZERO;
        }
        this.pagoFinal = this.saldo.add(this.interes);
        if (estadoCoactiva != null && estadoCoactiva == 2) {
            this.valorCoactiva = Utils.bigdecimalTo2Decimals(this.pagoFinal.multiply(porcentajeCoactiva).divide(new BigDecimal("100")));
        }
    }

    public String getExoneracionDescriptiva() {
        String nombre = "";
        List<FnExoneracionLiquidacion> resul = new ArrayList<>();
        if (Utils.isNotEmpty(exoneracionLiquidacionCollection)) {
            resul = exoneracionLiquidacionCollection.stream().filter(x -> x.getEstado().equals(Boolean.TRUE)).collect(Collectors.toList());
            for (FnExoneracionLiquidacion item : resul) {
                nombre = item.getExoneracion().getExoneracionTipo().getDescripcion() + " - " + nombre;
            }
        }

        return nombre;
    }

    public String getLiquidadorResponsable() {
        return liquidadorResponsable;
    }

    public void setLiquidadorResponsable(String liquidadorResponsable) {
        this.liquidadorResponsable = liquidadorResponsable;
    }

    public BigDecimal getExoneracionSumValor() {
        BigDecimal valor = BigDecimal.ZERO;
        List<FnExoneracionLiquidacion> resul = new ArrayList<>();
        if (Utils.isNotEmpty(exoneracionLiquidacionCollection)) {
            resul = exoneracionLiquidacionCollection.stream().filter(x -> x.getEstado().equals(Boolean.TRUE)).collect(Collectors.toList());
            for (FnExoneracionLiquidacion item : resul) {
                valor = valor.add(item.getExoneracion().getValor());
            }
        }
        this.valorExoneracion = valor;
        return valor;
    }

    public String getLiquidadorAprobador() {
        return liquidadorAprobador;
    }

    public void setLiquidadorAprobador(String liquidadorAprobador) {
        this.liquidadorAprobador = liquidadorAprobador;
    }

    public BigDecimal getTotalTmp() {
        return totalTmp;
    }

    public void setTotalTmp(BigDecimal totalTmp) {
        this.totalTmp = totalTmp;
    }

    public BigDecimal getDescuentoBiess() {
        return descuentoBiess;
    }

    public void setDescuentoBiess(BigDecimal descuentoBiess) {
        this.descuentoBiess = descuentoBiess;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public boolean getVerificarListaParticipacion() {
        if (listDistribuciionCantones != null && !listDistribuciionCantones.isEmpty()) {
            if (listDistribuciionCantones.size() > 0) {
                return true;
            }
            return false;
        }

        return false;
    }

    public BigDecimal abonado() {
        BigDecimal abono = BigDecimal.ZERO;
        if (renPagoCollection != null && !renPagoCollection.isEmpty()) {
            abono = renPagoCollection.stream().map(FinaRenPago::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return abono;
    }

    public BigDecimal getInteresFina() {
        if (renPagoCollection != null) {
            BigDecimal inte = BigDecimal.ZERO;
            for (FinaRenPago item : renPagoCollection) {
                inte = inte.add(item.getInteres());
            }
            return inte;
        }
        return BigDecimal.ZERO;
    }

    public List<ParticipacionCantones> getListDistribuciionCantones() {
        return listDistribuciionCantones;
    }

    public void setListDistribuciionCantones(List<ParticipacionCantones> listDistribuciionCantones) {
        this.listDistribuciionCantones = listDistribuciionCantones;
    }

//    public String getClaveCatastralRural() {
//        return claveCatastralRural;
//    }
//
//    public void setClaveCatastralRural(String claveCatastralRural) {
//        this.claveCatastralRural = claveCatastralRural;
//    }
    //<editor-fold defaultstate="collapsed" desc="get and ser">
    public BigDecimal getPagoFinal() {
        if (pagoFinal == null) {
            pagoFinal = new BigDecimal("0.00");
        }
        return pagoFinal;
    }

    public void setPagoFinal(BigDecimal pagoFinal) {
        this.pagoFinal = pagoFinal;
    }

    public BigDecimal getRecargo() {
        if (recargo == null) {
            recargo = new BigDecimal("0.00");
        }
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getInteres() {
        if (interes == null) {
            interes = new BigDecimal("0.00");
        }
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getValorCoactiva() {
        return valorCoactiva;
    }

    public void setValorCoactiva(BigDecimal valorCoactiva) {
        this.valorCoactiva = valorCoactiva;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPorcentajeCoactiva() {
        return porcentajeCoactiva;
    }

    public void setPorcentajeCoactiva(BigDecimal porcentajeCoactiva) {
        this.porcentajeCoactiva = porcentajeCoactiva;
    }

    public Date getFechaContratoAnt() {
        return fechaContratoAnt;
    }

    public void setFechaContratoAnt(Date fechaContratoAnt) {
        this.fechaContratoAnt = fechaContratoAnt;
    }

    public List<FinaRenPago> getRenPagoCollection() {
        return renPagoCollection;
    }

    public void setRenPagoCollection(List<FinaRenPago> renPagoCollection) {
        this.renPagoCollection = renPagoCollection;
    }

    public String getVendedorRural() {
        return vendedorRural;
    }

    public void setVendedorRural(String vendedorRural) {
        this.vendedorRural = vendedorRural;
    }

    public String getVendedorRuralIdentificacion() {
        return vendedorRuralIdentificacion;
    }

    public void setVendedorRuralIdentificacion(String vendedorRuralIdentificacion) {
        this.vendedorRuralIdentificacion = vendedorRuralIdentificacion;
    }

    public String getNumReporte() {
        return numReporte;
    }

    public void setNumReporte(String numReporte) {
        this.numReporte = numReporte;
    }

    public Boolean getTemporal() {
        return temporal;
    }

    public void setTemporal(Boolean temporal) {
        this.temporal = temporal;
    }

    public Boolean getRentas() {
        return rentas;
    }

    public void setRentas(Boolean rentas) {
        this.rentas = rentas;
    }

    public String getNombreCompradorHistoric() {
        return nombreCompradorHistoric;
    }

    public void setNombreCompradorHistoric(String nombreCompradorHistoric) {
        this.nombreCompradorHistoric = nombreCompradorHistoric;
    }

    public Boolean getEstaExonerado() {
        return estaExonerado;
    }

    public void setEstaExonerado(Boolean estaExonerado) {
        this.estaExonerado = estaExonerado;
    }

    public List<FinaRenDetLiquidacion> getRenDetLiquidacionCollection() {
        return renDetLiquidacionCollection;
    }

    public void setRenDetLiquidacionCollection(List<FinaRenDetLiquidacion> renDetLiquidacionCollection) {
        this.renDetLiquidacionCollection = renDetLiquidacionCollection;
    }

    public String getClaveCatastralRural() {
        return claveCatastralRural;
    }

    public void setClaveCatastralRural(String claveCatastralRural) {
        this.claveCatastralRural = claveCatastralRural;
    }

    public List<FnExoneracionLiquidacion> getExoneracionLiquidacionCollection() {
        return exoneracionLiquidacionCollection;
    }

    public void setExoneracionLiquidacionCollection(List<FnExoneracionLiquidacion> exoneracionLiquidacionCollection) {
        this.exoneracionLiquidacionCollection = exoneracionLiquidacionCollection;
    }

    public List<FnExoneracionLiquidacion> getExoneracionLiquidacionPosteriorCollection() {
        return exoneracionLiquidacionPosteriorCollection;
    }

    public void setExoneracionLiquidacionPosteriorCollection(List<FnExoneracionLiquidacion> exoneracionLiquidacionPosteriorCollection) {
        this.exoneracionLiquidacionPosteriorCollection = exoneracionLiquidacionPosteriorCollection;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getDireccionDocumento() {
        return direccionDocumento;
    }

    public void setDireccionDocumento(String direccionDocumento) {
        this.direccionDocumento = direccionDocumento;
    }

    public BigDecimal getDesceuentoBiess() {
        return descuentoBiess;
    }

    public void setDesceuentoBiess(BigDecimal descuentoBiess) {
        this.descuentoBiess = descuentoBiess;
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
        if (!(object instanceof FinaRenLiquidacion)) {
            return false;
        }
        FinaRenLiquidacion other = (FinaRenLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FinaRenLiquidacion{" + "id=" + id + '}';
    }
//</editor-fold>

}
