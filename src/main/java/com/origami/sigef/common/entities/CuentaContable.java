/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "cuenta_contable", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CuentaContable.findAll", query = "SELECT c FROM CuentaContable c"),
    @NamedQuery(name = "CuentaContable.findById", query = "SELECT c FROM CuentaContable c WHERE c.id = :id"),
    @NamedQuery(name = "CuentaContable.findByTitulo", query = "SELECT c FROM CuentaContable c WHERE c.titulo = :titulo"),
    @NamedQuery(name = "CuentaContable.findCtaContableByTipo", query = "SELECT c FROM CuentaContable c WHERE c.padre = ?1 "), //erwin
    @NamedQuery(name = "CuentaContable.findByPadre", query = "SELECT c FROM CuentaContable c WHERE c.padre.id = ?1 AND c.estado = TRUE"),
    @NamedQuery(name = "CuentaContable.findAllTipoMovimiento", query = "SELECT c FROM CuentaContable c WHERE c.movimiento = ?1 AND c.periodo = ?2 AND c.estado = TRUE ORDER BY c.codigo"),
    @NamedQuery(name = "CuentaContable.findByTituloMax", query = "SELECT MAX(c.titulo) FROM CuentaContable c WHERE c.nivel.orden = ?1")
})
public class CuentaContable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "titulo")
    private short titulo;
    @Column(name = "grupo")
    private Short grupo;
    @Column(name = "sub_grupo")
    private Short subGrupo;
    @Column(name = "cuenta_nivel_1")
    private Short cuentaNivel1;
    @Column(name = "cuenta_nivel_2")
    private Short cuentaNivel2;
    @Column(name = "cuenta_nivel_3")
    private Short cuentaNivel3;
    @Column(name = "cuenta_nivel_4")
    private Short cuentaNivel4;
    @Column(name = "cuenta_nivel_otro")
    private Short cuentaNivelOtro;
    @Basic(optional = false)
    @Size(min = 1, max = 200)
    @Column(name = "codigo")
    private String codigo;
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "periodo")
    private short periodo;
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Column(name = "gobierno")
    private boolean gobierno;
    @Column(name = "cta_pagar_cobrar")
    private Boolean ctaPagarCobrar;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @OneToMany(mappedBy = "cuentaContable")
    private List<CuentaContablePresupuesto> presupuestos;
    @OneToMany(mappedBy = "padre")
    private List<CuentaContable> hijos;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable padre;
    @JoinColumn(name = "nivel", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Nivel nivel;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
    @JoinColumn(name = "credito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto credito;
    @JoinColumn(name = "debito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto debito;
    @Column(name = "movimiento")
    private Boolean movimiento;
    @JoinColumn(name = "cobrado_devengado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoPresupuesto cobradoDevengado;
    @Column(name = "saldo_inicial_debe")
    private BigDecimal saldoInicialDebe = BigDecimal.ZERO;
    @Column(name = "saldo_inicial_haber")
    private BigDecimal saldoInicialHaber = BigDecimal.ZERO;

    @Transient
    private Boolean isHija;

    public CuentaContable() {
        this.estado = Boolean.TRUE;
        this.gobierno = Boolean.TRUE;
        this.movimiento = Boolean.FALSE;
        this.ctaPagarCobrar = Boolean.FALSE;
    }

    public Boolean getIsHija() {
        return isHija;
    }

    public void setIsHija(Boolean isHija) {
        this.isHija = isHija;
    }

    public Boolean getCtaPagarCobrar() {
        return ctaPagarCobrar;
    }

    public void setCtaPagarCobrar(Boolean ctaPagarCobrar) {
        this.ctaPagarCobrar = ctaPagarCobrar;
    }

    public CatalogoPresupuesto getCobradoDevengado() {
        return cobradoDevengado;
    }

    public void setCobradoDevengado(CatalogoPresupuesto cobradoDevengado) {
        this.cobradoDevengado = cobradoDevengado;
    }

    public CuentaContable(Long id) {
        this.id = id;
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

    public Short getGrupo() {
        return grupo;
    }

    public void setGrupo(Short grupo) {
        this.grupo = grupo;
    }

    public Short getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(Short subGrupo) {
        this.subGrupo = subGrupo;
    }

    public Short getCuentaNivel1() {
        return cuentaNivel1;
    }

    public void setCuentaNivel1(Short cuentaNivel1) {
        this.cuentaNivel1 = cuentaNivel1;
    }

    public Short getCuentaNivel2() {
        return cuentaNivel2;
    }

    public void setCuentaNivel2(Short cuentaNivel2) {
        this.cuentaNivel2 = cuentaNivel2;
    }

    public Short getCuentaNivel3() {
        return cuentaNivel3;
    }

    public void setCuentaNivel3(Short cuentaNivel3) {
        this.cuentaNivel3 = cuentaNivel3;
    }

    public Short getCuentaNivel4() {
        return cuentaNivel4;
    }

    public void setCuentaNivel4(Short cuentaNivel4) {
        this.cuentaNivel4 = cuentaNivel4;
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

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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

    public boolean getGobierno() {
        return gobierno;
    }

    public void setGobierno(boolean gobierno) {
        this.gobierno = gobierno;
    }

    public List<CuentaContablePresupuesto> getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(List<CuentaContablePresupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    public List<CuentaContable> getHijos() {
        return hijos;
    }

    public void setHijos(List<CuentaContable> hijos) {
        this.hijos = hijos;
    }

    public CuentaContable getPadre() {
        return padre;
    }

    public void setPadre(CuentaContable padre) {
        this.padre = padre;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Short getCuentaNivelOtro() {
        return cuentaNivelOtro;
    }

    public void setCuentaNivelOtro(Short cuentaNivelOtro) {
        this.cuentaNivelOtro = cuentaNivelOtro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }

    public CatalogoPresupuesto getCredito() {
        return credito;
    }

    public void setCredito(CatalogoPresupuesto credito) {
        this.credito = credito;
    }

    public CatalogoPresupuesto getDebito() {
        return debito;
    }

    public void setDebito(CatalogoPresupuesto debito) {
        this.debito = debito;
    }

    public BigDecimal getSaldoInicialDebe() {
        return saldoInicialDebe;
    }

    public void setSaldoInicialDebe(BigDecimal saldoInicialDebe) {
        this.saldoInicialDebe = saldoInicialDebe;
    }

    public BigDecimal getSaldoInicialHaber() {
        return saldoInicialHaber;
    }

    public void setSaldoInicialHaber(BigDecimal saldoInicialHaber) {
        this.saldoInicialHaber = saldoInicialHaber;
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
        if (!(object instanceof CuentaContable)) {
            return false;
        }
        CuentaContable other = (CuentaContable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.CuentaContable[ id=" + id + " ]";
    }

}
