/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.models.ListadoInventarioModel;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Formula;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "detalle_item", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleItem.findAll", query = "SELECT d FROM DetalleItem d"),
    @NamedQuery(name = "DetalleItem.findById", query = "SELECT d FROM DetalleItem d WHERE d.id = :id"),

//    @NamedQuery(name = "DetalleItem.findByCodigoList", query = "SELECT d FROM DetalleItem d WHERE d.codigo = ?1 and d.periodo = ?2 and d.estado = true"),
    @NamedQuery(name = "DetalleItem.findByCodigoList", query = "SELECT d FROM DetalleItem d WHERE d.codigoAgrupado = ?1  and d.estado = true"),
    
    @NamedQuery(name = "DetalleItem.findByDescripcion", query = "SELECT d FROM DetalleItem d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DetalleItem.findByTipoActivo", query = "SELECT t FROM DetalleItem d JOIN d.tipoActivo t JOIN t.catalogo c WHERE c.codigo = ?1 AND t.codigo = ?2"),
    @NamedQuery(name = "DetalleItem.getItemsParameter", query = "SELECT d FROM DetalleItem d JOIN d.tipoGasto t WHERE d.tipoGasto = ?1 AND d.estado = true AND d.cuentaContable IS NOT NULL"),    // erwin
    @NamedQuery(name = "DetalleItem.getItemsParameter1", query = "SELECT d FROM DetalleItem d JOIN d.tipoGasto t JOIN d.cuentaContable c WHERE  d.estado = true AND c.codigo =?1"),    // erwin
    @NamedQuery(name = "DetalleItem.getItemsParameter2", query = "SELECT d FROM DetalleItem d  JOIN d.cuentaContable c JOIN d.asignarGrupo a WHERE d.estado = true AND a.id =?1"),    // erwin
    @NamedQuery(name = "DetalleItem.findByMarca", query = "SELECT d FROM DetalleItem d WHERE d.marca = :marca"),
    @NamedQuery(name = "DetalleItem.findByFechaIngresoSistema", query = "SELECT d FROM DetalleItem d WHERE d.fechaIngresoSistema = :fechaIngresoSistema"),
    @NamedQuery(name = "DetalleItem.findByStockMinimo", query = "SELECT d FROM DetalleItem d WHERE d.stockMinimo = :stockMinimo"),
    @NamedQuery(name = "DetalleItem.findByStockMaximo", query = "SELECT d FROM DetalleItem d WHERE d.stockMaximo = :stockMaximo"),
    @NamedQuery(name = "DetalleItem.findByStockCritico", query = "SELECT d FROM DetalleItem d WHERE d.stockCritico = :stockCritico"),
    @NamedQuery(name = "DetalleItem.findByEstado", query = "SELECT d FROM DetalleItem d WHERE d.estado = :estado"),
    @NamedQuery(name = "DetalleItem.findByEstadotrue", query = "SELECT d FROM DetalleItem d WHERE d.estado = true"),
    @NamedQuery(name = "DetalleItem.findByCuentaContable", query = "SELECT d FROM DetalleItem d WHERE d.cuentaContable = :cuentaContable")})
@SqlResultSetMapping(name = "listadoItemsInventarioMapping",
        classes = @ConstructorResult(targetClass = ListadoInventarioModel.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "codigoAgrupado", type = String.class),
                    @ColumnResult(name = "descripcion", type = String.class),
                    @ColumnResult(name = "area", type = String.class),
                    @ColumnResult(name = "grupo", type = String.class),
                    @ColumnResult(name = "subgrupo", type = String.class),
                    @ColumnResult(name = "medida", type = String.class),
                    @ColumnResult(name = "cantidad", type = Integer.class),
                    @ColumnResult(name = "precio", type = BigDecimal.class),
                    @ColumnResult(name = "total", type = BigDecimal.class),
                    @ColumnResult(name = "estante", type = Short.class),
                    @ColumnResult(name = "fila", type = Short.class),
                    @ColumnResult(name = "columna", type = Short.class),
                    @ColumnResult(name = "cajon", type = Short.class),
                    @ColumnResult(name = "cuadrante", type = Short.class),
                    @ColumnResult(name = "cuentaContable", type = String.class),
                    @ColumnResult(name = "nombreCuenta", type = String.class),})
)
public class DetalleItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;

//    @NotNull
    @JoinColumn(name = "tipo_activo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoActivo;
    @Size(max = 255)
    @Column(name = "marca")
    private String marca;
    @Size(max = 255)
    @Column(name = "serie_1")
    private String serie1;
    @Size(max = 255)
    @Column(name = "serie_2")
    private String serie2;
    @Size(max = 100)
    @Column(name = "color_1")
    private String color1;
    @Size(max = 100)
    @Column(name = "color_2")
    private String color2;
    @Column(name = "fecha_ingreso_sistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngresoSistema;
    @Size(max = 255)
    @Column(name = "descripcion_adicional")
    private String descripcionAdicional;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stock_minimo")
    private BigDecimal stockMinimo;
    @Column(name = "stock_maximo")
    private BigDecimal stockMaximo;

    @Column(name = "stock_critico")
    private BigDecimal stockCritico;
    @Size(min = 1, max = 5)
    @Column(name = "codigo")
    private String codigo;
    @JoinColumn(name = "tipo_medida", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoMedida tipoMedida;
    @JoinTable(name = "inventario_items", joinColumns = {
        @JoinColumn(name = "detalle_item", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "inventario", referencedColumnName = "id")})
    @ManyToMany
    private List<Inventario> inventarioList;
    @JoinColumn(name = "asignar_grupo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private GrupoNiveles asignarGrupo;
//    @JoinColumn(name = "tipos_gastos", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private CuentaContable tiposGastos;

    @Column(name = "estante")
    private Short estante;

    @Column(name = "fila")
    private Short fila;

    @Column(name = "columna")
    private Short columna;
    @Column(name = "cajon")
    private Short cajon;
    @Column(name = "cuadrante")
    private Short cuadrante;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "estado")
    private Boolean estado;
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "orden")
    private Long orden;

    @Basic(optional = false)
//    @NotNull
    @Column(name = "periodo")
    private Short periodo;

    @Column(name = "codigo_agrupado")
    private String codigoAgrupado;
    @JoinColumn(name = "catalogo_existencias", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoExistencias catalogoExistencias;
    @OneToMany(mappedBy = "detalleItem")
    private List<DetalleItemImagen> detalleItemImagenes;

    @JoinColumn(name = "tipo_gasto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas tipoGasto;
    @JoinColumn(name = "cuenta_contable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas cuentaContable;
    @JoinColumn(name = "contra_cuenta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas contraCuenta;

    // importante
    @Transient
    private Integer cantidadTemp = 0;
    @Transient
    private Integer cantidadTempSol = 0;
    @Transient
    private Integer cantidadTempDes = 0;

    // importante
    @Transient
    private Integer cantMas = 0;
    @Transient
    private Integer cantMen = 0;
    @Transient
    private String obsAdicional;

    @Transient
    private Integer contador = 0;
    @Transient
    private BigDecimal precioTemp = BigDecimal.ZERO;
    @Transient
    private BigDecimal totalTemp = BigDecimal.ZERO;
    @Transient
    private String observacionTemp;

    @Transient
    private BigDecimal ivaTemp = BigDecimal.ZERO;

    @Column(name = "precio_calculado")
    private BigDecimal precioCalculado;
    @Column(name = "total_calculado")
    private BigDecimal totalCalculado;
    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalleBodega")
    //    private List<ActivoFijo> activoFijoList;
    @Column(name = "cantidad_existente")
    private Integer cantidadExistente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemsProducto")
    private List<Kardex> Listkardexs;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalleItem")
    private List<DetalleConstFisicaInventario> listaDetalleConstFisicaInv;

    @Column(name = "id_codigoqr")
    private String idAndCodigo;
    @Column(name = "codigo_qr")
    private String codigoQR;
    @Column(name = "url_imagen")
    private String urlImagen;

    @Transient
    private String urlImagenConstatacion;
    @Formula(value = "(SELECT (CASE WHEN d.stock_critico >= d.cantidad_existente THEN 1 ELSE 0 END ) FROM activos.detalle_item d where d.id = id )")
    private Integer critico;

    public DetalleItem() {
    }

    public DetalleItem(Long id) {
        this.id = id;
    }

    public DetalleItem(Long id, String descripcion, boolean estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public String getUrlImagenConstatacion() {
        return urlImagenConstatacion;
    }

    public void setUrlImagenConstatacion(String urlImagenConstatacion) {
        this.urlImagenConstatacion = urlImagenConstatacion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CatalogoItem getTipoActivo() {
        return tipoActivo;
    }

    public void setTipoActivo(CatalogoItem tipoActivo) {
        this.tipoActivo = tipoActivo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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

    public Date getFechaIngresoSistema() {
        return fechaIngresoSistema;
    }

    public void setFechaIngresoSistema(Date fechaIngresoSistema) {
        this.fechaIngresoSistema = fechaIngresoSistema;
    }

    public String getDescripcionAdicional() {
        return descripcionAdicional;
    }

    public void setDescripcionAdicional(String descripcionAdicional) {
        this.descripcionAdicional = descripcionAdicional;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(BigDecimal stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public BigDecimal getStockCritico() {
        return stockCritico;
    }

    public void setStockCritico(BigDecimal stockCritico) {
        this.stockCritico = stockCritico;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public ContCuentas getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(ContCuentas tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public List<Inventario> getInventarioList() {
        return inventarioList;
    }

    public void setInventarioList(List<Inventario> inventarioList) {
        this.inventarioList = inventarioList;
    }

    public GrupoNiveles getAsignarGrupo() {
        return asignarGrupo;
    }

    public void setAsignarGrupo(GrupoNiveles asignarGrupo) {
        this.asignarGrupo = asignarGrupo;
    }

//    public CuentaContable getTiposGastos() {
//        return tiposGastos;
//    }
//
//    public void setTiposGastos(CuentaContable tiposGastos) {
//        this.tiposGastos = tiposGastos;
//    }
    public Short getEstante() {
        return estante;
    }

    public void setEstante(Short estante) {
        this.estante = estante;
    }

    public Short getFila() {
        return fila;
    }

    public void setFila(Short fila) {
        this.fila = fila;
    }

    public Short getColumna() {
        return columna;
    }

    public void setColumna(Short columna) {
        this.columna = columna;
    }

    public Short getCajon() {
        return cajon;
    }

    public void setCajon(Short cajon) {
        this.cajon = cajon;
    }

    public Short getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(Short cuadrante) {
        this.cuadrante = cuadrante;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CatalogoMedida getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(CatalogoMedida tipoMedida) {
        this.tipoMedida = tipoMedida;
    }

    public Integer getCantidadExistente() {
        return cantidadExistente;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public void setCantidadExistente(Integer cantidadExistente) {
        this.cantidadExistente = cantidadExistente;
    }

    public Integer getCantidadTemp() {
        return cantidadTemp;
    }

    public void setCantidadTemp(Integer cantidadTemp) {
        this.cantidadTemp = cantidadTemp;
    }

    public BigDecimal getPrecioCalculado() {
        return precioCalculado;
    }

    public void setPrecioCalculado(BigDecimal precioCalculado) {
        this.precioCalculado = precioCalculado;
    }

    public BigDecimal getTotalCalculado() {
        return totalCalculado;
    }

    public void setTotalCalculado(BigDecimal totalCalculado) {
        this.totalCalculado = totalCalculado;
    }

    public BigDecimal getPrecioTemp() {
        return precioTemp;
    }

    public void setPrecioTemp(BigDecimal precioTemp) {
        this.precioTemp = precioTemp;
    }

    public BigDecimal getTotalTemp() {
        return totalTemp;
    }

    public void setTotalTemp(BigDecimal totalTemp) {
        this.totalTemp = totalTemp;
    }

    public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }

    public String getCodigoAgrupado() {
        return codigoAgrupado;
    }

    public void setCodigoAgrupado(String codigoAgrupado) {
        this.codigoAgrupado = codigoAgrupado;
    }

    public CatalogoExistencias getCatalogoExistencias() {
        return catalogoExistencias;
    }

    public void setCatalogoExistencias(CatalogoExistencias catalogoExistencias) {
        this.catalogoExistencias = catalogoExistencias;
    }

    public String getObservacionTemp() {
        return observacionTemp;
    }

    public void setObservacionTemp(String observacionTemp) {
        this.observacionTemp = observacionTemp;
    }

    public Integer getCantidadTempSol() {
        return cantidadTempSol;
    }

    public void setCantidadTempSol(Integer cantidadTempSol) {
        this.cantidadTempSol = cantidadTempSol;
    }

    public Integer getCantidadTempDes() {
        return cantidadTempDes;
    }

    public void setCantidadTempDes(Integer cantidadTempDes) {
        this.cantidadTempDes = cantidadTempDes;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public List<Kardex> getListkardexs() {
        return Listkardexs;
    }

    public void setListkardexs(List<Kardex> Listkardexs) {
        this.Listkardexs = Listkardexs;
    }

    public List<DetalleConstFisicaInventario> getListaDetalleConstFisicaInv() {
        return listaDetalleConstFisicaInv;
    }

    public void setListaDetalleConstFisicaInv(List<DetalleConstFisicaInventario> listaDetalleConstFisicaInv) {
        this.listaDetalleConstFisicaInv = listaDetalleConstFisicaInv;
    }

    public Integer getCantMas() {
        return cantMas;
    }

    public void setCantMas(Integer cantMas) {
        this.cantMas = cantMas;
    }

    public Integer getCantMen() {
        return cantMen;
    }

    public void setCantMen(Integer cantMen) {
        this.cantMen = cantMen;
    }

    public String getObsAdicional() {
        return obsAdicional;
    }

    public void setObsAdicional(String obsAdicional) {
        this.obsAdicional = obsAdicional;
    }

    public String getIdAndCodigo() {
        return idAndCodigo;
    }

    public void setIdAndCodigo(String idAndCodigo) {
        this.idAndCodigo = idAndCodigo;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public List<DetalleItemImagen> getDetalleItemImagenes() {
        return detalleItemImagenes;
    }

    public void setDetalleItemImagenes(List<DetalleItemImagen> detalleItemImagenes) {
        this.detalleItemImagenes = detalleItemImagenes;
    }

    public BigDecimal getIvaTemp() {
        return ivaTemp;
    }

    public void setIvaTemp(BigDecimal ivaTemp) {
        this.ivaTemp = ivaTemp;
    }

    public ContCuentas getContraCuenta() {
        return contraCuenta;
    }

    public void setContraCuenta(ContCuentas contraCuenta) {
        this.contraCuenta = contraCuenta;
    }

    public Integer getCritico() {
        return critico;
    }

    public void setCritico(Integer critico) {
        this.critico = critico;
    }

    public String detalleEtiquetaQR() {
        return "ID: " + id + "\n "
                + "Codigo: " + codigo + "\n "
                + "Pertenece a: " + (tipoGasto != null ? tipoGasto.getDescripcion() : "") + "\n "
                + "Descripcion: " + descripcion + "\n "
                + "Serie No. 1: " + serie1 + "\n "
                + "Serie No. 2: " + serie2 + "\n "
                + "Color No. 1: " + color1 + "\n "
                + "Color No. 2: " + color2 + "\n "
                //                + "Periodo: " + periodo + "\n "
                + "Grupo Contable: " + cuentaContable.getDescripcion();
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
        if (!(object instanceof DetalleItem)) {
            return false;
        }
        DetalleItem other = (DetalleItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DetalleItem[ id=" + id + " ]";
    }

}
