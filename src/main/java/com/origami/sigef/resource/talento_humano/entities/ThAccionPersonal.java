/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_accion_personal", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThAccionPersonal.findAll", query = "SELECT t FROM ThAccionPersonal t"),
    @NamedQuery(name = "ThAccionPersonal.findById", query = "SELECT t FROM ThAccionPersonal t WHERE t.id = :id"),
    @NamedQuery(name = "ThAccionPersonal.findByExplicacion", query = "SELECT t FROM ThAccionPersonal t WHERE t.explicacion = :explicacion"),
    @NamedQuery(name = "ThAccionPersonal.findByEstado", query = "SELECT t FROM ThAccionPersonal t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThAccionPersonal.findByFechaCreacion", query = "SELECT t FROM ThAccionPersonal t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThAccionPersonal.findByFechaModificacion", query = "SELECT t FROM ThAccionPersonal t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThAccionPersonal.findByUsuarioCreacion", query = "SELECT t FROM ThAccionPersonal t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThAccionPersonal.findByUsuarioModifica", query = "SELECT t FROM ThAccionPersonal t WHERE t.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "ThAccionPersonal.findByCodigo", query = "SELECT t FROM ThAccionPersonal t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "ThAccionPersonal.findByOrden", query = "SELECT t FROM ThAccionPersonal t WHERE t.orden = :orden"),
    @NamedQuery(name = "ThAccionPersonal.findByFechaAccionPersonal", query = "SELECT t FROM ThAccionPersonal t WHERE t.fechaAccionPersonal = :fechaAccionPersonal"),
    @NamedQuery(name = "ThAccionPersonal.findBySituacionActual", query = "SELECT t FROM ThAccionPersonal t WHERE t.situacionActual = :situacionActual"),
    @NamedQuery(name = "ThAccionPersonal.findBySituacionPropuesta", query = "SELECT t FROM ThAccionPersonal t WHERE t.situacionPropuesta = :situacionPropuesta"),
    @NamedQuery(name = "ThAccionPersonal.findByActaFinal", query = "SELECT t FROM ThAccionPersonal t WHERE t.actaFinal = :actaFinal"),
    @NamedQuery(name = "ThAccionPersonal.findByActaRecursoHumano", query = "SELECT t FROM ThAccionPersonal t WHERE t.actaRecursoHumano = :actaRecursoHumano"),
    @NamedQuery(name = "ThAccionPersonal.findByCaucion", query = "SELECT t FROM ThAccionPersonal t WHERE t.caucion = :caucion"),
    @NamedQuery(name = "ThAccionPersonal.findByReemplazo", query = "SELECT t FROM ThAccionPersonal t WHERE t.reemplazo = :reemplazo"),
    @NamedQuery(name = "ThAccionPersonal.findByAccionpersonal", query = "SELECT t FROM ThAccionPersonal t WHERE t.accionpersonal = :accionpersonal"),
    @NamedQuery(name = "ThAccionPersonal.findByCodActa", query = "SELECT t FROM ThAccionPersonal t WHERE t.codActa = :codActa"),
    @NamedQuery(name = "ThAccionPersonal.findByCodigoList", query = "SELECT t FROM ThAccionPersonal t WHERE t.codigoList = :codigoList"),
    @NamedQuery(name = "ThAccionPersonal.findByOrdenMax", query = "SELECT MAX(t.orden)+1 FROM ThAccionPersonal t WHERE t.estado = true"),
    @NamedQuery(name = "ThAccionPersonal.findByInfoReemplazo", query = "SELECT t FROM ThAccionPersonal t WHERE t.infoReemplazo = :infoReemplazo")})
public class ThAccionPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "explicacion")
    private String explicacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Size(max = 500)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private long orden;
    @Column(name = "fecha_accion_personal")
    @Temporal(TemporalType.DATE)
    private Date fechaAccionPersonal;
    @Size(max = 2147483647)
    @Column(name = "situacion_actual")
    private String situacionActual;
    @Size(max = 2147483647)
    @Column(name = "situacion_propuesta")
    private String situacionPropuesta;
    @Size(max = 2147483647)
    @Column(name = "acta_final")
    private String actaFinal;
    @Size(max = 2147483647)
    @Column(name = "acta_recurso_humano")
    private String actaRecursoHumano;
    @Size(max = 2147483647)
    @Column(name = "caucion")
    private String caucion;
    @Size(max = 2147483647)
    @Column(name = "reemplazo")
    private String reemplazo;
    @Size(max = 2147483647)
    @Column(name = "accionpersonal")
    private String accionpersonal;
    @Size(max = 2147483647)
    @Column(name = "cod_acta")
    private String codActa;
    @Size(max = 2147483647)
    @Column(name = "codigo_list")
    private String codigoList;
    @Size(max = 2147483647)
    @Column(name = "info_reemplazo")
    private String infoReemplazo;
    @JoinColumn(name = "recursos_humano", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor recursosHumano;
    @JoinColumn(name = "responsable_administrativo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor responsableAdministrativo;
    @JoinColumn(name = "servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor servidor;

    @Transient
    private String[] codMotivoApList;

    public ThAccionPersonal() {
    }

    public ThAccionPersonal(Long id) {
        this.id = id;
    }

    public ThAccionPersonal(Long id, boolean estado, long orden) {
        this.id = id;
        this.estado = estado;
        this.orden = orden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
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

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public Date getFechaAccionPersonal() {
        return fechaAccionPersonal;
    }

    public void setFechaAccionPersonal(Date fechaAccionPersonal) {
        this.fechaAccionPersonal = fechaAccionPersonal;
    }

    public String getSituacionActual() {
        return situacionActual;
    }

    public void setSituacionActual(String situacionActual) {
        this.situacionActual = situacionActual;
    }

    public String getSituacionPropuesta() {
        return situacionPropuesta;
    }

    public void setSituacionPropuesta(String situacionPropuesta) {
        this.situacionPropuesta = situacionPropuesta;
    }

    public String getActaFinal() {
        return actaFinal;
    }

    public void setActaFinal(String actaFinal) {
        this.actaFinal = actaFinal;
    }

    public String getActaRecursoHumano() {
        return actaRecursoHumano;
    }

    public void setActaRecursoHumano(String actaRecursoHumano) {
        this.actaRecursoHumano = actaRecursoHumano;
    }

    public String getCaucion() {
        return caucion;
    }

    public void setCaucion(String caucion) {
        this.caucion = caucion;
    }

    public String getReemplazo() {
        return reemplazo;
    }

    public void setReemplazo(String reemplazo) {
        this.reemplazo = reemplazo;
    }

    public String getAccionpersonal() {
        return accionpersonal;
    }

    public void setAccionpersonal(String accionpersonal) {
        this.accionpersonal = accionpersonal;
    }

    public String getCodActa() {
        return codActa;
    }

    public void setCodActa(String codActa) {
        this.codActa = codActa;
    }

    public String getCodigoList() {
        return codigoList;
    }

    public void setCodigoList(String codigoList) {
        this.codigoList = codigoList;
    }

    public String getInfoReemplazo() {
        return infoReemplazo;
    }

    public void setInfoReemplazo(String infoReemplazo) {
        this.infoReemplazo = infoReemplazo;
    }

    public Servidor getRecursosHumano() {
        return recursosHumano;
    }

    public void setRecursosHumano(Servidor recursosHumano) {
        this.recursosHumano = recursosHumano;
    }

    public Servidor getResponsableAdministrativo() {
        return responsableAdministrativo;
    }

    public void setResponsableAdministrativo(Servidor responsableAdministrativo) {
        this.responsableAdministrativo = responsableAdministrativo;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public String[] getCodMotivoApList() {
        return codMotivoApList;
    }

    public void setCodMotivoApList(String[] codMotivoApList) {
        this.codMotivoApList = codMotivoApList;
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
        if (!(object instanceof ThAccionPersonal)) {
            return false;
        }
        ThAccionPersonal other = (ThAccionPersonal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThAccionPersonal[ id=" + id + " ]";
    }

}
