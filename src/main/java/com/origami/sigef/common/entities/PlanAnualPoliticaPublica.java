/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kriiz
 */
@Entity
@Table(name = "plan_anual_politica_publica")
@NamedQueries({
    @NamedQuery(name = "PlanAnualPoliticaPublica.findAll", query = "SELECT p FROM PlanAnualPoliticaPublica p")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findById", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.id = :id")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByPeriodo", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.periodo = :periodo")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByAsignacionInicial", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.asignacionInicial = :asignacionInicial")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByReformas", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.reformas = :reformas")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByCodificado", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.codificado = :codificado")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByComprometido", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.comprometido = :comprometido")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByDevengado", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.devengado = :devengado")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByRecaudado", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.recaudado = :recaudado")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByEstado", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.estado = :estado")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByFechaCreacion", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.fechaCreacion = :fechaCreacion")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByUsuarioCreacion", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.usuarioCreacion = :usuarioCreacion")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByFechaModificacion", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.fechaModificacion = :fechaModificacion")
    ,
    @NamedQuery(name = "PlanAnualPoliticaPublica.findByUsuarioModifica", query = "SELECT p FROM PlanAnualPoliticaPublica p WHERE p.usuarioModifica = :usuarioModifica")})
public class PlanAnualPoliticaPublica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "periodo")
    private short periodo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "asignacion_inicial")
    private BigDecimal asignacionInicial;
    @Column(name = "reformas")
    private BigDecimal reformas;
    @Column(name = "codificado")
    private BigDecimal codificado;
    @Column(name = "comprometido")
    private BigDecimal comprometido;
    @Column(name = "devengado")
    private BigDecimal devengado;
    @Column(name = "recaudado")
    private BigDecimal recaudado;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "codigo_reforma")
    private BigInteger codigoReforma;
    @Column(name = "numero_orden_id")
    private BigInteger numeroOrdenId;
    @Column(name = "numero_tramite")
    private Short numeroTramite;
    @Column(name = "codigo_reforma_traspaso")
    private BigInteger codigoReformaTraspaso;
    @Column(name = "registro_nuevo_referencia")
    private BigInteger registroNuevoReferencia;
    @JoinColumn(name = "estado_papp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPapp;
    @OneToMany(mappedBy = "planAnual")
    private List<PlanAnualProgramaProyecto> planAnualProgramaProyectoList;
    @JoinColumn(name = "plan_local", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanLocalProgramaProyecto planLocal;
    @JoinColumn(name = "politica_nacional", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanNacionalPolitica politicaNacional;

    public PlanAnualPoliticaPublica() {
    }

    public PlanAnualPoliticaPublica(Long id) {
        this.id = id;
    }

    public PlanAnualPoliticaPublica(Long id, short periodo, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
        this.id = id;
        this.periodo = periodo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getAsignacionInicial() {
        return asignacionInicial;
    }

    public void setAsignacionInicial(BigDecimal asignacionInicial) {
        this.asignacionInicial = asignacionInicial;
    }

    public BigDecimal getReformas() {
        return reformas;
    }

    public void setReformas(BigDecimal reformas) {
        this.reformas = reformas;
    }

    public BigDecimal getCodificado() {
        return codificado;
    }

    public void setCodificado(BigDecimal codificado) {
        this.codificado = codificado;
    }

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getRecaudado() {
        return recaudado;
    }

    public void setRecaudado(BigDecimal recaudado) {
        this.recaudado = recaudado;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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

    public List<PlanAnualProgramaProyecto> getPlanAnualProgramaProyectoList() {
        return planAnualProgramaProyectoList;
    }

    public void setPlanAnualProgramaProyectoList(List<PlanAnualProgramaProyecto> planAnualProgramaProyectoList) {
        this.planAnualProgramaProyectoList = planAnualProgramaProyectoList;
    }

    public PlanLocalProgramaProyecto getPlanLocal() {
        return planLocal;
    }

    public void setPlanLocal(PlanLocalProgramaProyecto planLocal) {
        this.planLocal = planLocal;
    }

    public PlanNacionalPolitica getPoliticaNacional() {
        return politicaNacional;
    }

    public void setPoliticaNacional(PlanNacionalPolitica politicaNacional) {
        this.politicaNacional = politicaNacional;
    }

    public BigInteger getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(BigInteger codigoReforma) {
        this.codigoReforma = codigoReforma;
    }

    public BigInteger getCodigoReformaTraspaso() {
        return codigoReformaTraspaso;
    }

    public void setCodigoReformaTraspaso(BigInteger codigoReformaTraspaso) {
        this.codigoReformaTraspaso = codigoReformaTraspaso;
    }

    public BigInteger getNumeroOrdenId() {
        return numeroOrdenId;
    }

    public void setNumeroOrdenId(BigInteger numeroOrdenId) {
        this.numeroOrdenId = numeroOrdenId;
    }

    public Short getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Short numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public CatalogoItem getEstadoPapp() {
        return estadoPapp;
    }

    public void setEstadoPapp(CatalogoItem estadoPapp) {
        this.estadoPapp = estadoPapp;
    }

    public BigInteger getRegistroNuevoReferencia() {
        return registroNuevoReferencia;
    }

    public void setRegistroNuevoReferencia(BigInteger registroNuevoReferencia) {
        this.registroNuevoReferencia = registroNuevoReferencia;
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
        if (!(object instanceof PlanAnualPoliticaPublica)) {
            return false;
        }
        PlanAnualPoliticaPublica other = (PlanAnualPoliticaPublica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.PlanAnualPoliticaPublica[ id=" + id + " ]";
    }

}
