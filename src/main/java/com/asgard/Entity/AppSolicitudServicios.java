/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_solicitud_servicios", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppSolicitudServicios.findAll", query = "SELECT a FROM AppSolicitudServicios a"),
    @NamedQuery(name = "AppSolicitudServicios.findById", query = "SELECT a FROM AppSolicitudServicios a WHERE a.id = :id"),
    @NamedQuery(name = "AppSolicitudServicios.findByArchivar", query = "SELECT a FROM AppSolicitudServicios a WHERE a.archivar = :archivar"),
    @NamedQuery(name = "AppSolicitudServicios.findByAsignado", query = "SELECT a FROM AppSolicitudServicios a WHERE a.asignado = :asignado"),
    @NamedQuery(name = "AppSolicitudServicios.findByAsignados", query = "SELECT a FROM AppSolicitudServicios a WHERE a.asignados = :asignados"),
    @NamedQuery(name = "AppSolicitudServicios.findByDescripcionInconveniente", query = "SELECT a FROM AppSolicitudServicios a WHERE a.descripcionInconveniente = :descripcionInconveniente"),
    @NamedQuery(name = "AppSolicitudServicios.findByFechaCreacion", query = "SELECT a FROM AppSolicitudServicios a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "AppSolicitudServicios.findByFechaInconveniente", query = "SELECT a FROM AppSolicitudServicios a WHERE a.fechaInconveniente = :fechaInconveniente"),
    @NamedQuery(name = "AppSolicitudServicios.findByFechaMaximaRespuesta", query = "SELECT a FROM AppSolicitudServicios a WHERE a.fechaMaximaRespuesta = :fechaMaximaRespuesta"),
    @NamedQuery(name = "AppSolicitudServicios.findByInforme", query = "SELECT a FROM AppSolicitudServicios a WHERE a.informe = :informe"),
    @NamedQuery(name = "AppSolicitudServicios.findByNotaGuia", query = "SELECT a FROM AppSolicitudServicios a WHERE a.notaGuia = :notaGuia"),
    @NamedQuery(name = "AppSolicitudServicios.findByNotificacion", query = "SELECT a FROM AppSolicitudServicios a WHERE a.notificacion = :notificacion"),
    @NamedQuery(name = "AppSolicitudServicios.findByNotificacionDep", query = "SELECT a FROM AppSolicitudServicios a WHERE a.notificacionDep = :notificacionDep"),
    @NamedQuery(name = "AppSolicitudServicios.findByNotificar", query = "SELECT a FROM AppSolicitudServicios a WHERE a.notificar = :notificar"),
    @NamedQuery(name = "AppSolicitudServicios.findByPrioridad", query = "SELECT a FROM AppSolicitudServicios a WHERE a.prioridad = :prioridad"),
    @NamedQuery(name = "AppSolicitudServicios.findByRepresentante", query = "SELECT a FROM AppSolicitudServicios a WHERE a.representante = :representante"),
    @NamedQuery(name = "AppSolicitudServicios.findBySolicitudInterna", query = "SELECT a FROM AppSolicitudServicios a WHERE a.solicitudInterna = :solicitudInterna"),
    @NamedQuery(name = "AppSolicitudServicios.findByStatus", query = "SELECT a FROM AppSolicitudServicios a WHERE a.status = :status"),
    @NamedQuery(name = "AppSolicitudServicios.findByEnteSolicitante", query = "SELECT a FROM AppSolicitudServicios a WHERE a.enteSolicitante = :enteSolicitante"),
    @NamedQuery(name = "AppSolicitudServicios.findByTramite", query = "SELECT a FROM AppSolicitudServicios a WHERE a.tramite = :tramite"),
    @NamedQuery(name = "AppSolicitudServicios.findByUsuarioIngreso", query = "SELECT a FROM AppSolicitudServicios a WHERE a.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "AppSolicitudServicios.findByGeTipoTramite", query = "SELECT a FROM AppSolicitudServicios a WHERE a.geTipoTramite = :geTipoTramite")})
public class AppSolicitudServicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "archivar")
    private Boolean archivar;
    @Column(name = "asignado")
    private Boolean asignado;
    @Size(max = 255)
    @Column(name = "asignados")
    private String asignados;
    @Size(max = 255)
    @Column(name = "descripcion_inconveniente")
    private String descripcionInconveniente;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_inconveniente")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInconveniente;
    @Column(name = "fecha_maxima_respuesta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMaximaRespuesta;
    @Size(max = 255)
    @Column(name = "informe")
    private String informe;
    @Size(max = 255)
    @Column(name = "nota_guia")
    private String notaGuia;
    @Size(max = 255)
    @Column(name = "notificacion")
    private String notificacion;
    @Size(max = 255)
    @Column(name = "notificacion_dep")
    private String notificacionDep;
    @Column(name = "notificar")
    private Boolean notificar;
    @Column(name = "prioridad")
    private Integer prioridad;
    @Size(max = 255)
    @Column(name = "representante")
    private String representante;
    @Column(name = "solicitud_interna")
    private Boolean solicitudInterna;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Column(name = "ente_solicitante")
    private BigInteger enteSolicitante;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Column(name = "usuario_ingreso")
    private BigInteger usuarioIngreso;
    @Column(name = "ge_tipo_tramite")
    private BigInteger geTipoTramite;
    @OneToMany(mappedBy = "solicitud")
    private List<AppSolicitudDocumento> appSolicitudDocumentoList;
    @JoinColumn(name = "tipo_servicio", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamento tipoServicio;
    @JoinColumn(name = "lugar_audiencia", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosDepartamento lugarAudiencia;
    @OneToMany(mappedBy = "solicitudServicio")
    private List<DocuNotificacion> docuNotificacionList;
    @OneToMany(mappedBy = "solicitud")
    private List<AppSolicitudDepartamento> appSolicitudDepartamentoList;

    public AppSolicitudServicios() {
    }

    public AppSolicitudServicios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getArchivar() {
        return archivar;
    }

    public void setArchivar(Boolean archivar) {
        this.archivar = archivar;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public String getAsignados() {
        return asignados;
    }

    public void setAsignados(String asignados) {
        this.asignados = asignados;
    }

    public String getDescripcionInconveniente() {
        return descripcionInconveniente;
    }

    public void setDescripcionInconveniente(String descripcionInconveniente) {
        this.descripcionInconveniente = descripcionInconveniente;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaInconveniente() {
        return fechaInconveniente;
    }

    public void setFechaInconveniente(Date fechaInconveniente) {
        this.fechaInconveniente = fechaInconveniente;
    }

    public Date getFechaMaximaRespuesta() {
        return fechaMaximaRespuesta;
    }

    public void setFechaMaximaRespuesta(Date fechaMaximaRespuesta) {
        this.fechaMaximaRespuesta = fechaMaximaRespuesta;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getNotaGuia() {
        return notaGuia;
    }

    public void setNotaGuia(String notaGuia) {
        this.notaGuia = notaGuia;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public String getNotificacionDep() {
        return notificacionDep;
    }

    public void setNotificacionDep(String notificacionDep) {
        this.notificacionDep = notificacionDep;
    }

    public Boolean getNotificar() {
        return notificar;
    }

    public void setNotificar(Boolean notificar) {
        this.notificar = notificar;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public Boolean getSolicitudInterna() {
        return solicitudInterna;
    }

    public void setSolicitudInterna(Boolean solicitudInterna) {
        this.solicitudInterna = solicitudInterna;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigInteger getEnteSolicitante() {
        return enteSolicitante;
    }

    public void setEnteSolicitante(BigInteger enteSolicitante) {
        this.enteSolicitante = enteSolicitante;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public BigInteger getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(BigInteger usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public BigInteger getGeTipoTramite() {
        return geTipoTramite;
    }

    public void setGeTipoTramite(BigInteger geTipoTramite) {
        this.geTipoTramite = geTipoTramite;
    }

    
    public List<AppSolicitudDocumento> getAppSolicitudDocumentoList() {
        return appSolicitudDocumentoList;
    }

    public void setAppSolicitudDocumentoList(List<AppSolicitudDocumento> appSolicitudDocumentoList) {
        this.appSolicitudDocumentoList = appSolicitudDocumentoList;
    }

    public AppServiciosDepartamento getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(AppServiciosDepartamento tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public AppServiciosDepartamento getLugarAudiencia() {
        return lugarAudiencia;
    }

    public void setLugarAudiencia(AppServiciosDepartamento lugarAudiencia) {
        this.lugarAudiencia = lugarAudiencia;
    }

    
    public List<DocuNotificacion> getDocuNotificacionList() {
        return docuNotificacionList;
    }

    public void setDocuNotificacionList(List<DocuNotificacion> docuNotificacionList) {
        this.docuNotificacionList = docuNotificacionList;
    }

    
    public List<AppSolicitudDepartamento> getAppSolicitudDepartamentoList() {
        return appSolicitudDepartamentoList;
    }

    public void setAppSolicitudDepartamentoList(List<AppSolicitudDepartamento> appSolicitudDepartamentoList) {
        this.appSolicitudDepartamentoList = appSolicitudDepartamentoList;
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
        if (!(object instanceof AppSolicitudServicios)) {
            return false;
        }
        AppSolicitudServicios other = (AppSolicitudServicios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppSolicitudServicios[ id=" + id + " ]";
    }
    
}
