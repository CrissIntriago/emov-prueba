/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.resource.activos.entities.BienCatalogoBld;
import com.origami.sigef.resource.activos.entities.BienVidaUtil;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "bienes_item", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BienesItem.findAll", query = "SELECT b FROM BienesItem b"),
    @NamedQuery(name = "BienesItem.findById", query = "SELECT b FROM BienesItem b WHERE b.id = :id"),
    @NamedQuery(name = "BienesItem.findByCodigoBien", query = "SELECT b FROM BienesItem b WHERE b.codigoBien = :codigoBien"),
    @NamedQuery(name = "BienesItem.findByCodigoBienAgrupado", query = "SELECT b FROM BienesItem b WHERE b.codigoBienAgrupado = :codigoBienAgrupado"),
    @NamedQuery(name = "BienesItem.findByDescripcion", query = "SELECT b FROM BienesItem b WHERE b.descripcion = :descripcion"),
    @NamedQuery(name = "BienesItem.findByTipoBien", query = "SELECT b FROM BienesItem b WHERE b.tipoBien = :tipoBien"),
    @NamedQuery(name = "BienesItem.findByMarca", query = "SELECT b FROM BienesItem b WHERE b.marca = :marca"),
    @NamedQuery(name = "BienesItem.findByMaterialBien", query = "SELECT b FROM BienesItem b WHERE b.materialBien = :materialBien"),
    @NamedQuery(name = "BienesItem.findByAlto", query = "SELECT b FROM BienesItem b WHERE b.alto = :alto"),
    @NamedQuery(name = "BienesItem.findByAncho", query = "SELECT b FROM BienesItem b WHERE b.ancho = :ancho"),
    @NamedQuery(name = "BienesItem.findByLargo", query = "SELECT b FROM BienesItem b WHERE b.largo = :largo"),
    @NamedQuery(name = "BienesItem.findByFechaAdquisicion", query = "SELECT b FROM BienesItem b WHERE b.fechaAdquisicion = :fechaAdquisicion"),
    @NamedQuery(name = "BienesItem.findByCostoAdquisicion", query = "SELECT b FROM BienesItem b WHERE b.costoAdquisicion = :costoAdquisicion"),
    @NamedQuery(name = "BienesItem.findByCantidad", query = "SELECT b FROM BienesItem b WHERE b.cantidad = :cantidad"),
    @NamedQuery(name = "BienesItem.findByValorTotal", query = "SELECT b FROM BienesItem b WHERE b.valorTotal = :valorTotal"),
    @NamedQuery(name = "BienesItem.findByVidaUtil", query = "SELECT b FROM BienesItem b WHERE b.vidaUtil = :vidaUtil"),
    @NamedQuery(name = "BienesItem.findByComponente", query = "SELECT b FROM BienesItem b WHERE b.componente = :componente"),
    @NamedQuery(name = "BienesItem.findByEstado", query = "SELECT b FROM BienesItem b WHERE b.estado = :estado"),
    @NamedQuery(name = "BienesItem.findByOrden", query = "SELECT b FROM BienesItem b WHERE b.orden = :orden"),
    @NamedQuery(name = "BienesItem.findByUsuarioCreador", query = "SELECT b FROM BienesItem b WHERE b.usuarioCreador = :usuarioCreador"),
    @NamedQuery(name = "BienesItem.findByFechaCreacion", query = "SELECT b FROM BienesItem b WHERE b.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "BienesItem.findByUsuarioModifica", query = "SELECT b FROM BienesItem b WHERE b.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "BienesItem.findByFechaModificacion", query = "SELECT b FROM BienesItem b WHERE b.fechaModificacion = :fechaModificacion")})
public class BienesItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo_bien")
    private String codigoBien;
    @Column(name = "codigo_bien_agrupado")
    private String codigoBienAgrupado;
    @Column(name = "codigo_anterior")
    private String codigoAnterior;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "marca")
    private String marca;
    @Column(name = "modelo")
    private String modelo;

    @Column(name = "serie1")
    private String serie1;

    @Column(name = "serie2")
    private String serie2;
    @Column(name = "color1")
    private String color1;
    @Column(name = "color2")
    private String color2;

    @Column(name = "material_bien")
    private String materialBien;
    @Column(name = "alto")
    private Short alto;
    @Column(name = "ancho")
    private Short ancho;
    @Column(name = "largo")
    private Short largo;
    @Column(name = "fecha_adquisicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAdquisicion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_adquisicion")
    private BigDecimal costoAdquisicion;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Column(name = "vida_util")
    private Short vidaUtil;
    @Column(name = "componente")
    private Boolean componente;
    @Column(name = "item_bien_boolean")
    private Boolean itemBienBoolean;
    @Column(name = "uso")
    private Boolean uso;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "orden")
    private long orden;

    @Column(name = "usuario_creador")
    private String usuarioCreador;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "anio_fabricacion")
    private short anioFabricacion;
    @Column(name = "observacion")
    private String observacion;
    @OneToMany(mappedBy = "grupoPadre")
    private List<BienesItem> bienesItemList;

    @JoinColumn(name = "grupo_padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem grupoPadre;
    @JoinColumn(name = "catalogo_bienes", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienCatalogoBld catalogoBienes;
    @JoinColumn(name = "tipo_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoBien;
//    @JoinColumn(name = "clasificacion_tipo_bien", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private CuentaContable clasificacionTipoBien;
//    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private CuentaContable cuentasContables;
    @JoinColumn(name = "estado_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoBien;
    @JoinColumn(name = "padre_pertenece", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem padrePertenece;
    @JoinColumn(name = "tipo_dato_adicional", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoDatoAdicional;

    @Column(name = "placa_codigocatastral")
    private String placaCodigocatastral;

    @Column(name = "ubicacion_numchasis")
    private String ubicacionNumchasis;

    @Column(name = "serie_motor")
    private String serieMotor;
    @Column(name = "fecha_inscripcion_predio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcionPredio;
    @JoinColumn(name = "bienes_movimiento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesMovimiento bienesMovimiento;
    @Column(name = "fecha_ultim_depreciacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimDepreciacion;
    @Column(name = "depreciacion_acumulada")
    private BigDecimal depreciacionAcumulada;

    @Column(name = "tiene_componentes")
    private Boolean tieneComponentes;
    @Column(name = "utpe")
    private BigDecimal utpe;

    @JoinColumn(name = "unidad_medida", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem unidadMedida;

    @JoinColumn(name = "vida_util_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienVidaUtil vidaUtilBien;

    @Transient
    private CatalogoItem estadoBienConst;
    @Transient
    private String observAdicional;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemBien")
    private List<BienesMovimiento> bienesMovimientoList;

    @Column(name = "id_and_codigo")
    private String idAndCodigo;
    @Column(name = "url_imagen_qr")
    private String urlImagenQr;
    @Column(name = "nombre_imagen_qr")
    private String nombreImagenQR;
    @Column(name = "referencia")
    private Long referencia;
    @JoinColumn(name = "clasific_tipo_bien", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas clasificTipoBien;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaContable;

    public BienesItem() {
    }

    public BienesItem(Long id) {
        this.id = id;
    }

    public String getIdAndCodigo() {
        return idAndCodigo;
    }

    public void setIdAndCodigo(String idAndCodigo) {
        this.idAndCodigo = idAndCodigo;
    }

    public String getUrlImagenQr() {
        return urlImagenQr;
    }

    public void setUrlImagenQr(String urlImagenQr) {
        this.urlImagenQr = urlImagenQr;
    }

    public String getNombreImagenQR() {
        return nombreImagenQR;
    }

    public void setNombreImagenQR(String nombreImagenQR) {
        this.nombreImagenQR = nombreImagenQR;
    }

    public BienesItem(Long id, String codigoBien, String descripcion, String usuarioCreador, Date fechaCreacion) {
        this.id = id;
        this.codigoBien = codigoBien;
        this.descripcion = descripcion;
        this.usuarioCreador = usuarioCreador;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBien() {
        return codigoBien;
    }

    public void setCodigoBien(String codigoBien) {
        this.codigoBien = codigoBien;
    }

    public String getCodigoBienAgrupado() {
        return codigoBienAgrupado;
    }

    public void setCodigoBienAgrupado(String codigoBienAgrupado) {
        this.codigoBienAgrupado = codigoBienAgrupado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie1() {
        return serie1;
    }

    public void setSerie1(String serie1) {
        this.serie1 = serie1;
    }

    public String getSerie2() {
        return serie2;
    }

    public void setSerie2(String serie2) {
        this.serie2 = serie2;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getMaterialBien() {
        return materialBien;
    }

    public void setMaterialBien(String materialBien) {
        this.materialBien = materialBien;
    }

    public Short getAlto() {
        return alto;
    }

    public void setAlto(Short alto) {
        this.alto = alto;
    }

    public Short getAncho() {
        return ancho;
    }

    public void setAncho(Short ancho) {
        this.ancho = ancho;
    }

    public Short getLargo() {
        return largo;
    }

    public void setLargo(Short largo) {
        this.largo = largo;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public BigDecimal getCostoAdquisicion() {
        return costoAdquisicion;
    }

    public void setCostoAdquisicion(BigDecimal costoAdquisicion) {
        this.costoAdquisicion = costoAdquisicion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Short getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(Short vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public Boolean getComponente() {
        return componente;
    }

    public void setComponente(Boolean componente) {
        this.componente = componente;
    }

    public Boolean getItemBienBoolean() {
        return itemBienBoolean;
    }

    public void setItemBienBoolean(Boolean itemBienBoolean) {
        this.itemBienBoolean = itemBienBoolean;
    }

    public Boolean getUso() {
        return uso;
    }

    public void setUso(Boolean uso) {
        this.uso = uso;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public short getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(short anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<BienesItem> getBienesItemList() {
        return bienesItemList;
    }

    public void setBienesItemList(List<BienesItem> bienesItemList) {
        this.bienesItemList = bienesItemList;
    }

    public BienesItem getGrupoPadre() {
        return grupoPadre;
    }

    public void setGrupoPadre(BienesItem grupoPadre) {
        this.grupoPadre = grupoPadre;
    }

    public BienCatalogoBld getCatalogoBienes() {
        return catalogoBienes;
    }

    public void setCatalogoBienes(BienCatalogoBld catalogoBienes) {
        this.catalogoBienes = catalogoBienes;
    }

    public CatalogoItem getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(CatalogoItem tipoBien) {
        this.tipoBien = tipoBien;
    }

//    public CuentaContable getClasificacionTipoBien() {
//        return clasificacionTipoBien;
//    }
//
//    public void setClasificacionTipoBien(CuentaContable clasificacionTipoBien) {
//        this.clasificacionTipoBien = clasificacionTipoBien;
//    }
    public ContCuentas getClasificTipoBien() {
        return clasificTipoBien;
    }

    public void setClasificTipoBien(ContCuentas clasificTipoBien) {
        this.clasificTipoBien = clasificTipoBien;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public CatalogoItem getEstadoBien() {
        return estadoBien;
    }

    public void setEstadoBien(CatalogoItem estadoBien) {
        this.estadoBien = estadoBien;
    }

    public BienesItem getPadrePertenece() {
        return padrePertenece;
    }

    public void setPadrePertenece(BienesItem padrePertenece) {
        this.padrePertenece = padrePertenece;
    }

    public CatalogoItem getTipoDatoAdicional() {
        return tipoDatoAdicional;
    }

    public void setTipoDatoAdicional(CatalogoItem tipoDatoAdicional) {
        this.tipoDatoAdicional = tipoDatoAdicional;
    }

    public String getPlacaCodigocatastral() {
        return placaCodigocatastral;
    }

    public void setPlacaCodigocatastral(String placaCodigocatastral) {
        this.placaCodigocatastral = placaCodigocatastral;
    }

    public String getUbicacionNumchasis() {
        return ubicacionNumchasis;
    }

    public void setUbicacionNumchasis(String ubicacionNumchasis) {
        this.ubicacionNumchasis = ubicacionNumchasis;
    }

    public String getSerieMotor() {
        return serieMotor;
    }

    public void setSerieMotor(String serieMotor) {
        this.serieMotor = serieMotor;
    }

    public Date getFechaInscripcionPredio() {
        return fechaInscripcionPredio;
    }

    public void setFechaInscripcionPredio(Date fechaInscripcionPredio) {
        this.fechaInscripcionPredio = fechaInscripcionPredio;
    }

    public BienesMovimiento getBienesMovimiento() {
        return bienesMovimiento;
    }

    public void setBienesMovimiento(BienesMovimiento bienesMovimiento) {
        this.bienesMovimiento = bienesMovimiento;
    }

    public Date getFechaUltimDepreciacion() {
        return fechaUltimDepreciacion;
    }

    public void setFechaUltimDepreciacion(Date fechaUltimDepreciacion) {
        this.fechaUltimDepreciacion = fechaUltimDepreciacion;
    }

    public BigDecimal getDepreciacionAcumulada() {
        return depreciacionAcumulada;
    }

    public void setDepreciacionAcumulada(BigDecimal depreciacionAcumulada) {
        this.depreciacionAcumulada = depreciacionAcumulada;
    }

    public Boolean getTieneComponentes() {
        return tieneComponentes;
    }

    public void setTieneComponentes(Boolean tieneComponentes) {
        this.tieneComponentes = tieneComponentes;
    }

    public BigDecimal getUtpe() {
        return utpe;
    }

    public void setUtpe(BigDecimal utpe) {
        this.utpe = utpe;
    }

    public List<BienesMovimiento> getBienesMovimientoList() {
        return bienesMovimientoList;
    }

    public void setBienesMovimientoList(List<BienesMovimiento> bienesMovimientoList) {
        this.bienesMovimientoList = bienesMovimientoList;
    }

    public CatalogoItem getEstadoBienConst() {
        return estadoBienConst;
    }

    public void setEstadoBienConst(CatalogoItem estadoBienConst) {
        this.estadoBienConst = estadoBienConst;
    }

    public String getObservAdicional() {
        return observAdicional;
    }

    public void setObservAdicional(String observAdicional) {
        this.observAdicional = observAdicional;
    }

    public CatalogoItem getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(CatalogoItem unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BienVidaUtil getVidaUtilBien() {
        return vidaUtilBien;
    }

    public void setVidaUtilBien(BienVidaUtil vidaUtilBien) {
        this.vidaUtilBien = vidaUtilBien;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public String getCodigoAnterior() {
        return codigoAnterior;
    }

    public void setCodigoAnterior(String codigoAnterior) {
        this.codigoAnterior = codigoAnterior;
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
        if (!(object instanceof BienesItem)) {
            return false;
        }
        BienesItem other = (BienesItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.BienesItem[ id=" + id + " ]";
    }

    public String detalleEtiquetaQR() {
        return "ID: " + id + "\n "
                + "Codigo: " + codigoBienAgrupado + "\n "
                + "Pertenece a: " + bienesMovimiento.getCodigo() + "\n "
                + "Descripcion: " + descripcion + "\n "
                + "Serie No. 1: " + serie1 + "\n "
                + "Serie No. 2: " + serie2 + "\n "
                + "Color No. 1: " + color1 + "\n "
                + "Color No. 2: " + color2 + "\n "
                + "Modelo: " + modelo + "\n "
                + "Estado: " + estadoBien.getDescripcion() + "\n "
//                + "Periodo: " + periodo + "\n "
                + "Grupo Contable: " + cuentaContable.getDescripcion();
    }
}
