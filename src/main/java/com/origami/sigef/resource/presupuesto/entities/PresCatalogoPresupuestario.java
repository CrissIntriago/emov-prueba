/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "pres_catalogo_presupuestario", schema = "presupuesto")
@NamedQueries({
    @NamedQuery(name = "PresCatalogoPresupuestario.findAll", query = "SELECT p FROM PresCatalogoPresupuestario p"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findById", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.id = :id"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findBySubGrupo", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.subGrupo = :subGrupo"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByCodigo", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByDescripcion", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByConfId", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.confId = :confId"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByIngreso", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.ingreso = :ingreso"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByMovimiento", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.movimiento = :movimiento"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByClasificacion", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.clasificacion = :clasificacion"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByActivo", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.activo = :activo"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByFechaCreacion", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByUsuarioCreacion", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByFechaModificacion", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByUsuarioModifica", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByPresupuestoInicial", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.presupuestoInicial = :presupuestoInicial"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByReformaSuplementaria", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.reformaSuplementaria = :reformaSuplementaria"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByReformaReduccion", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.reformaReduccion = :reformaReduccion"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByNivel", query = "SELECT p FROM PresCatalogoPresupuestario p INNER JOIN p.confId c WHERE p.estado = true AND c.nivel = ?1 ORDER BY p.codigo ASC"),
    @NamedQuery(name = "PresCatalogoPresupuestario.findByPresupuestoCodificado", query = "SELECT p FROM PresCatalogoPresupuestario p WHERE p.presupuestoCodificado = :presupuestoCodificado")})
public class PresCatalogoPresupuestario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "sub_grupo")
    private Short subGrupo;
    @Size(max = 200)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "conf_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanCuentas confId;
    @Column(name = "ingreso")
    private Boolean ingreso;
    @Column(name = "movimiento")
    private Boolean movimiento;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 200)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 200)
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
    @OneToMany(mappedBy = "padre")
    private List<PresCatalogoPresupuestario> presCatalogoPresupuestarioList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private PresCatalogoPresupuestario padre;
    @JoinColumn(name = "fuete_new", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento fueteNew;

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
    @Transient
    private String codPadre;
    @Transient
    private String codIngreso;

    public PresCatalogoPresupuestario() {
        activo = true;
        movimiento = false;
        ingreso = true;
        estado = true;
    }

    public PresCatalogoPresupuestario(Long id) {
        this.id = id;
    }

    public PresCatalogoPresupuestario(Long idpresupuesto, String codpresupuesto, String nompresupuesto) {
        this.id = idpresupuesto;
        this.codigo = codpresupuesto;
        this.descripcion = nompresupuesto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(Short subGrupo) {
        this.subGrupo = subGrupo;
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

    public PlanCuentas getConfId() {
        return confId;
    }

    public void setConfId(PlanCuentas confId) {
        this.confId = confId;
    }

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Boolean getIngreso() {
        return ingreso;
    }

    public void setIngreso(Boolean ingreso) {
        this.ingreso = ingreso;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public List<PresCatalogoPresupuestario> getPresCatalogoPresupuestarioList() {
        return presCatalogoPresupuestarioList;
    }

    public void setPresCatalogoPresupuestarioList(List<PresCatalogoPresupuestario> presCatalogoPresupuestarioList) {
        this.presCatalogoPresupuestarioList = presCatalogoPresupuestarioList;
    }

    public PresCatalogoPresupuestario getPadre() {
        return padre;
    }

    public void setPadre(PresCatalogoPresupuestario padre) {
        this.padre = padre;
    }

    public String getOrientacionGastos() {
        return orientacionGastos;
    }

    public void setOrientacionGastos(String orientacionGastos) {
        this.orientacionGastos = orientacionGastos;
    }

    public BigDecimal getReformasByFechas() {
        return reformasByFechas;
    }

    public void setReformasByFechas(BigDecimal reformasByFechas) {
        this.reformasByFechas = reformasByFechas;
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

    public String getCodPadre() {
        if (padre != null) {
            codPadre = padre.getCodigo();
        }
        return codPadre;
    }

    public void setCodPadre(String codPadre) {
        this.codPadre = codPadre;
    }

    public PresFuenteFinanciamiento getFueteNew() {
        return fueteNew;
    }

    public void setFueteNew(PresFuenteFinanciamiento fueteNew) {
        this.fueteNew = fueteNew;
    }

    public String getCodIngreso() {
        if (padre != null) {
            int aux = codigo.length() - padre.getCodigo().length();
            if (aux > 0) {
                codIngreso = codigo.substring(padre.getCodigo().length()).replace(confId.getCaracter(), "");
            }
        } else {
            codIngreso = codigo;
        }
        return codIngreso;
    }

    public void setCodIngreso(String codIngreso) {
        this.codIngreso = codIngreso;
    }

    public String returnIngreso() {
        return codIngreso;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof PresCatalogoPresupuestario)) {
            return false;
        }
        PresCatalogoPresupuestario other = (PresCatalogoPresupuestario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PresCatalogoPresupuestario{" + "id=" + id + ", codigo=" + codigo + ", confId=" + confId + ", ingreso=" + ingreso + ", estado=" + estado + ", clasificacion=" + clasificacion + '}';
    }
    

}
