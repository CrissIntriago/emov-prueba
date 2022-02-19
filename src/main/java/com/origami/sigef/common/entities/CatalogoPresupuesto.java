/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.Presupuesto.Entity.DetalleReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Model.GrupoPresupuestoModel;
import com.origami.sigef.tesoreria.entities.Rubro;
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

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "catalogo_presupuesto", schema = "public")
@SqlResultSetMapping(name = "GrupoPresupuestarioMapping",
        classes = @ConstructorResult(targetClass = GrupoPresupuestoModel.class,
                columns = {
                    @ColumnResult(name = "titulo", type = Integer.class)
                    ,@ColumnResult(name = "naturaleza", type = Integer.class)
                    ,@ColumnResult(name = "grupo", type = String.class)
                })
)
@NamedQueries({
    @NamedQuery(name = "CatalogoPresupuesto.findAll", query = "SELECT c FROM CatalogoPresupuesto c")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByPadre", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.padre.id = ?1 AND c.estado = TRUE")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findById", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.id = :id")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByTitulo", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.titulo = :titulo")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByNaturaleza", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.naturaleza = :naturaleza")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findBySubGrupo", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.subGrupo = :subGrupo")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByNivel", query = "SELECT c FROM CatalogoPresupuesto c JOIN c.nivel cn WHERE c.estado = ?1 AND cn.orden = ?2")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByNivelEgresos", query = "SELECT c FROM CatalogoPresupuesto c JOIN c.nivel cn WHERE c.estado = ?1  AND c.flujoIngreso= ?2 AND cn.orden = ?3 AND c.anio= ?4 order by c.codigo ASC")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByRubro", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.rubro = :rubro")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByCodigo", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.codigo = :codigo")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByDescripcion", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.descripcion = :descripcion")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByFlujoIngreso", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.flujoIngreso = :flujoIngreso")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByCuentaMovimiento", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.cuentaMovimiento = :cuentaMovimiento")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByFechaVigencia", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.fechaVigencia = :fechaVigencia")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByFechaCaducidad", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.fechaCaducidad = :fechaCaducidad")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByEstado", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.estado = :estado")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByEstadoValido", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.estado = ?1")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByEgresos", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.flujoIngreso=?1 AND c.nivel.orden=?2 AND c.estado = ?3 ")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByAnio", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.anio = :anio")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByFechaCreacion", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.fechaCreacion = :fechaCreacion")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByUsuarioCreacion", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.usuarioCreacion = :usuarioCreacion")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByFechaModificacion", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.fechaModificacion = :fechaModificacion")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByUsuarioModifica", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.usuarioModifica = :usuarioModifica")
    ,
    @NamedQuery(name = "CatalogoPresupuesto.findByPresupuestoInicial", query = "SELECT c FROM CatalogoPresupuesto c WHERE c.presupuestoInicial = :presupuestoInicial")})
public class CatalogoPresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "titulo")
    private short titulo;
    @Column(name = "naturaleza")
    private Short naturaleza;
    @Column(name = "sub_grupo")
    private Short subGrupo;
    @Column(name = "rubro")
    private Short rubro;
    @Basic(optional = false)
    @Size(min = 1, max = 200)
    @Column(name = "codigo")
    private String codigo;
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "flujo_ingreso")
    private Boolean flujoIngreso;
    @NotNull
    @Column(name = "cuenta_movimiento")
    private boolean cuentaMovimiento;
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "anio")
    private Short anio;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIME)
    private Date fechaCreacion;
    @Size(min = 1, max = 200)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(min = 1, max = 200)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "presupuesto_inicial")
    private BigDecimal presupuestoInicial;
    @Column(name = "reforma_suplementaria")
    private BigDecimal reformaSuplementaria;
    @Column(name = "reforma_reduccion")
    private BigDecimal reformaReduccion;
    @Column(name = "presupuesto_codificado")
    private BigDecimal presupuestoCodificado;

    @OneToMany(mappedBy = "credito")
    private List<CuentaContable> credito;
    @OneToMany(mappedBy = "debito")
    private List<CuentaContable> debito;
    @OneToMany(mappedBy = "cobradoDevengado")
    private List<CuentaContable> cobradoDevengado;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
    @OneToMany(mappedBy = "padre")
    private List<CatalogoPresupuesto> catalogoPresupuestoList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto padre;
    @JoinColumn(name = "fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FuenteFinanciamiento fuente;
    @JoinColumn(name = "nivel", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Nivel nivel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemPresupuestario")
    private List<ProgramacionIngresoEgreso> programacionIngresos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoPresupuesto")
    private List<CuentaContablecatalogoPresupuesto> cuentaContable;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partida")
    private List<Rubro> rubroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemApA")
    private List<PartidasDistributivoAnexo> ListaPartidaDisAnexo;
//    @ManyToMany(mappedBy = "catalogoPresupuesto")
//    private List<CuentaContable> cuentaContableList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemApR")
    private List<ValoresRoles> ListaItemApRTipoRol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoPresupuesto")
    private List<DetalleReformaIngresoSuplemento> listaDetalleSuplementoIngreso;

    @Transient
    private String orientacionGastos;
    @Transient
    private BigDecimal reformasByFechas = BigDecimal.ZERO;
    @Transient
    private BigDecimal codificado = BigDecimal.ZERO;
    @Transient
    private BigDecimal compromiso = BigDecimal.ZERO;
    @Transient
    private BigDecimal devengado = BigDecimal.ZERO;

    public CatalogoPresupuesto() {
        this.estado = Boolean.TRUE;
        this.reformaReduccion = BigDecimal.ZERO;
        this.reformaSuplementaria = BigDecimal.ZERO;
        this.presupuestoInicial = BigDecimal.ZERO;
        this.presupuestoCodificado = BigDecimal.ZERO;
    }

    public CatalogoPresupuesto(Long id) {
        this.id = id;
    }

    public CatalogoPresupuesto(Long id, short titulo, String codigo, String descripcion, boolean cuentaMovimiento, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
        this.id = id;
        this.titulo = titulo;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cuentaMovimiento = cuentaMovimiento;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
    }

    public List<CuentaContable> getCobradoDevengado() {
        return cobradoDevengado;
    }

    public void setCobradoDevengado(List<CuentaContable> cobradoDevengado) {
        this.cobradoDevengado = cobradoDevengado;
    }

    public List<CuentaContable> getCredito() {
        return credito;
    }

    public void setCredito(List<CuentaContable> credito) {
        this.credito = credito;
    }

    public List<CuentaContable> getDebito() {
        return debito;
    }

    public void setDebito(List<CuentaContable> debito) {
        this.debito = debito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getTitulo() {
        return titulo;
    }

    public void setTitulo(short titulo) {
        this.titulo = titulo;
    }

    public Short getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(Short naturaleza) {
        this.naturaleza = naturaleza;
    }

    public Short getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(Short subGrupo) {
        this.subGrupo = subGrupo;
    }

    public Short getRubro() {
        return rubro;
    }

    public void setRubro(Short rubro) {
        this.rubro = rubro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getFlujoIngreso() {
        return flujoIngreso;
    }

    public void setFlujoIngreso(Boolean flujoIngreso) {
        this.flujoIngreso = flujoIngreso;
    }

    public boolean getCuentaMovimiento() {
        return cuentaMovimiento;
    }

    public void setCuentaMovimiento(boolean cuentaMovimiento) {
        this.cuentaMovimiento = cuentaMovimiento;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public BigDecimal getPresupuestoInicial() {
        return presupuestoInicial;
    }

    public void setPresupuestoInicial(BigDecimal presupuestoInicial) {
        this.presupuestoInicial = presupuestoInicial;
    }

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public List<CatalogoPresupuesto> getCatalogoPresupuestoList() {
        return catalogoPresupuestoList;
    }

    public void setCatalogoPresupuestoList(List<CatalogoPresupuesto> catalogoPresupuestoList) {
        this.catalogoPresupuestoList = catalogoPresupuestoList;
    }

    public FuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public CatalogoPresupuesto getPadre() {
        return padre;
    }

    public void setPadre(CatalogoPresupuesto padre) {
        this.padre = padre;
    }

    public List<ProgramacionIngresoEgreso> getProgramacionIngresos() {
        return programacionIngresos;
    }

    public void setProgramacionIngresos(List<ProgramacionIngresoEgreso> programacionIngresos) {
        this.programacionIngresos = programacionIngresos;
    }

    public List<CuentaContablecatalogoPresupuesto> getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(List<CuentaContablecatalogoPresupuesto> cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public List<DetalleReformaIngresoSuplemento> getListaDetalleSuplementoIngreso() {
        return listaDetalleSuplementoIngreso;
    }

    public void setListaDetalleSuplementoIngreso(List<DetalleReformaIngresoSuplemento> listaDetalleSuplementoIngreso) {
        this.listaDetalleSuplementoIngreso = listaDetalleSuplementoIngreso;
    }

    public BigDecimal getReformaSuplementaria() {
        return reformaSuplementaria;
    }

    public void setReformaSuplementaria(BigDecimal reformaSuplementaria) {
        this.reformaSuplementaria = reformaSuplementaria;
    }

    public BigDecimal getReformaReduccion() {
        return reformaReduccion;
    }

    public void setReformaReduccion(BigDecimal reformaReduccion) {
        this.reformaReduccion = reformaReduccion;
    }

    public BigDecimal getPresupuestoCodificado() {
        return presupuestoCodificado;
    }

    public void setPresupuestoCodificado(BigDecimal presupuestoCodificado) {
        this.presupuestoCodificado = presupuestoCodificado;
    }

    public String getOrientacionGastos() {
        return orientacionGastos;
    }

    public void setOrientacionGastos(String orientacionGastos) {
        this.orientacionGastos = orientacionGastos;
    }

    public BigDecimal getCodificado() {
        return codificado;
    }

    public void setCodificado(BigDecimal codificado) {
        this.codificado = codificado;
    }

    public BigDecimal getCompromiso() {
        return compromiso;
    }

    public void setCompromiso(BigDecimal compromiso) {
        this.compromiso = compromiso;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getReformasByFechas() {
        return reformasByFechas;
    }

    public void setReformasByFechas(BigDecimal reformasByFechas) {
        this.reformasByFechas = reformasByFechas;
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
        if (!(object instanceof CatalogoPresupuesto)) {
            return false;
        }
        CatalogoPresupuesto other = (CatalogoPresupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CatalogoPresupuesto[ id=" + id + " ]";
    }

}
