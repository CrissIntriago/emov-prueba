/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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
@Table(name = "plan_local_programa_proyecto")
@NamedQueries({
    @NamedQuery(name = "PlanLocalProgramaProyecto.findAll", query = "SELECT p FROM PlanLocalProgramaProyecto p")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findById", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.id = :id")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByCodigo", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.codigo = :codigo")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByPeriodo", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.periodo = :periodo")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByDescripcion", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.descripcion = :descripcion")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByFechaVigencia", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.fechaVigencia = :fechaVigencia")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByFechaCaducidad", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.fechaCaducidad = :fechaCaducidad")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByEstado", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.estado = :estado")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByFechaCreacion", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.fechaCreacion = :fechaCreacion")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByUsuarioCreacion", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.usuarioCreacion = :usuarioCreacion")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByFechaModificacion", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.fechaModificacion = :fechaModificacion")
    ,
    @NamedQuery(name = "PlanLocalProgramaProyecto.findByUsuarioModifica", query = "SELECT p FROM PlanLocalProgramaProyecto p WHERE p.usuarioModifica = :usuarioModifica")})
public class PlanLocalProgramaProyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "periodo")
    private short periodo;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @Basic(optional = false)
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
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
    private BigInteger numeroTramite;
    @Column(name = "codigo_reforma_traspaso")
    private BigInteger codigoReformaTraspaso;
    @Column(name = "registro_nuevo_referencia")
    private BigInteger registroNuevoReferencia;
    @JoinColumn(name = "estado_papp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPapp;
    @JoinColumn(name = "politica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanLocalPolitica politica;
    @OneToMany(mappedBy = "planLocal")
    private List<PlanAnualPoliticaPublica> planAnualPoliticaPublicaList;

    public PlanLocalProgramaProyecto() {
    }

    public PlanLocalProgramaProyecto(Long id) {
        this.id = id;
    }

    public PlanLocalProgramaProyecto(Long id, short periodo, String descripcion, Date fechaVigencia, Date fechaCaducidad, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
        this.id = id;
        this.periodo = periodo;
        this.fechaVigencia = fechaVigencia;
        this.fechaCaducidad = fechaCaducidad;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public PlanLocalPolitica getPolitica() {
        return politica;
    }

    public void setPolitica(PlanLocalPolitica politica) {
        this.politica = politica;
    }

    public List<PlanAnualPoliticaPublica> getPlanAnualPoliticaPublicaList() {
        return planAnualPoliticaPublicaList;
    }

    public void setPlanAnualPoliticaPublicaList(List<PlanAnualPoliticaPublica> planAnualPoliticaPublicaList) {
        this.planAnualPoliticaPublicaList = planAnualPoliticaPublicaList;
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

    public BigInteger getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(BigInteger numeroTramite) {
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
        if (!(object instanceof PlanLocalProgramaProyecto)) {
            return false;
        }
        PlanLocalProgramaProyecto other = (PlanLocalProgramaProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.PlanLocalProgramaProyecto[ id=" + id + " ]";
    }

}
