/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.Presupuesto.Entity.PlanificacionPrograma;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "plan_anual_programa_proyecto")
@NamedQueries({
    @NamedQuery(name = "PlanAnualProgramaProyecto.findAll", query = "SELECT p FROM PlanAnualProgramaProyecto p"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findById", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.id = :id"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByPeriodo", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.periodo = :periodo"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByIdentificacionProgramaProyecto", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.identificacionProgramaProyecto = :identificacionProgramaProyecto"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByNombreProgramaProyecto", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.nombreProgramaProyecto = :nombreProgramaProyecto"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByEstado", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.estado = :estado"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByFechaCreacion", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findVerificarHijos", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.planAnual.id=?1"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByUsuarioCreacion", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByFechaModificacion", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "PlanAnualProgramaProyecto.findByUsuarioModifica", query = "SELECT p FROM PlanAnualProgramaProyecto p WHERE p.usuarioModifica = :usuarioModifica")})
public class PlanAnualProgramaProyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    // @NotNull
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "identificacion_programa_proyecto")
    private Short identificacionProgramaProyecto;
    @Size(max = 2147483647)
    @Column(name = "nombre_programa_proyecto")
    private String nombreProgramaProyecto;
    @Basic(optional = false)
    // @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    // @NotNull
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
    // @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_solicita")
    private String usuario_solicita;
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
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVigencia;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaducidad;
    @Column(name = "codigo")
    private String Codigo;
    @JoinColumn(name = "linea_accion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem lineaAccion;
    @JoinColumn(name = "estado_papp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPapp;
    @JoinColumn(name = "estado_proceso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoProceso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planProgramaProyecto", orphanRemoval = true)
    private List<ActividadOperativa> actividadOperativaList;
    @JoinColumn(name = "plan_anual", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanAnualPoliticaPublica planAnual;

    @JoinColumn(name = "programa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanificacionPrograma programa;

    public PlanAnualProgramaProyecto() {
        estado = Boolean.TRUE;
        fechaVigencia = new Date();
    }

    public PlanAnualProgramaProyecto(Long id) {
        this.id = id;
    }

    public PlanAnualProgramaProyecto(Long id, Short periodo, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
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

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Short getIdentificacionProgramaProyecto() {
        return identificacionProgramaProyecto;
    }

    public void setIdentificacionProgramaProyecto(Short identificacionProgramaProyecto) {
        this.identificacionProgramaProyecto = identificacionProgramaProyecto;
    }

    public String getNombreProgramaProyecto() {
        return nombreProgramaProyecto;
    }

    public void setNombreProgramaProyecto(String nombreProgramaProyecto) {
        this.nombreProgramaProyecto = nombreProgramaProyecto;
    }

    public boolean isEstado() {
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

    public String getUsuario_solicita() {
        return usuario_solicita;
    }

    public void setUsuario_solicita(String usuario_solicita) {
        this.usuario_solicita = usuario_solicita;
    }

    public BigInteger getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(BigInteger codigoReforma) {
        this.codigoReforma = codigoReforma;
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

    public BigInteger getCodigoReformaTraspaso() {
        return codigoReformaTraspaso;
    }

    public void setCodigoReformaTraspaso(BigInteger codigoReformaTraspaso) {
        this.codigoReformaTraspaso = codigoReformaTraspaso;
    }

    public BigInteger getRegistroNuevoReferencia() {
        return registroNuevoReferencia;
    }

    public void setRegistroNuevoReferencia(BigInteger registroNuevoReferencia) {
        this.registroNuevoReferencia = registroNuevoReferencia;
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

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public CatalogoItem getEstadoPapp() {
        return estadoPapp;
    }

    public void setEstadoPapp(CatalogoItem estadoPapp) {
        this.estadoPapp = estadoPapp;
    }

    public CatalogoItem getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(CatalogoItem estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public List<ActividadOperativa> getActividadOperativaList() {
        return actividadOperativaList;
    }

    public void setActividadOperativaList(List<ActividadOperativa> actividadOperativaList) {
        this.actividadOperativaList = actividadOperativaList;
    }

    public PlanAnualPoliticaPublica getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(PlanAnualPoliticaPublica planAnual) {
        this.planAnual = planAnual;
    }

    public PlanificacionPrograma getPrograma() {
        return programa;
    }

    public void setPrograma(PlanificacionPrograma programa) {
        this.programa = programa;
    }

    public CatalogoItem getLineaAccion() {
        return lineaAccion;
    }

    public void setLineaAccion(CatalogoItem lineaAccion) {
        this.lineaAccion = lineaAccion;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanAnualProgramaProyecto)) {
            return false;
        }
        PlanAnualProgramaProyecto other = (PlanAnualProgramaProyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.PlanAnualProgramaProyecto[ id=" + id + " ]";

    }

}
