/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ricardo
 */
@Entity
@Table(name = "registro_solicitud_requisitos", schema = Utils.SCHEMA_VENTANILLA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroSolicitudRequisitos.findAll", query = "SELECT r FROM RegistroSolicitudRequisitos r"),
    @NamedQuery(name = "RegistroSolicitudRequisitos.findAllBySolicitud", query = "SELECT r FROM RegistroSolicitudRequisitos r WHERE r.solicitudServicios.id=?1")})
public class RegistroSolicitudRequisitos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "solicitud_servicio_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudServicios solicitudServicios;
    @JoinColumn(name = "servicio_requisito_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ServicioRequisito servicioRequisito;
    @JoinColumn(name = "solicitud_documento", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudDocumento solicitudDocumento;
    @Column(name = "entregado")
    private Boolean entregado;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "correccion")
    private Boolean correccion;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "fecha_creacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaModifica;
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "corregido")
    private Boolean corregido;

    public RegistroSolicitudRequisitos() {
    }

    public RegistroSolicitudRequisitos(SolicitudServicios solicitudServicios, ServicioRequisito servicioRequisito, Boolean corregido,
            Boolean entregado, Boolean activo, Boolean correccion, String observacion, Date fechaCreacion, String usuarioCreacion) {
        this.solicitudServicios = solicitudServicios;
        this.servicioRequisito = servicioRequisito;
        this.entregado = entregado;
        this.activo = activo;
        this.corregido = corregido;
        this.correccion = correccion;
        this.observacion = observacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
    }

    public RegistroSolicitudRequisitos(Long id) {
        this.id = id;
    }

    public SolicitudDocumento getSolicitudDocumento() {
        return solicitudDocumento;
    }

    public void setSolicitudDocumento(SolicitudDocumento solicitudDocumento) {
        this.solicitudDocumento = solicitudDocumento;
    }

    public Boolean getCorregido() {
        return corregido;
    }

    public void setCorregido(Boolean corregido) {
        this.corregido = corregido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public ServicioRequisito getServicioRequisito() {
        return servicioRequisito;
    }

    public void setServicioRequisito(ServicioRequisito servicioRequisito) {
        this.servicioRequisito = servicioRequisito;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getCorreccion() {
        return correccion;
    }

    public void setCorreccion(Boolean correccion) {
        this.correccion = correccion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
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
        if (!(object instanceof RegistroSolicitudRequisitos)) {
            return false;
        }
        RegistroSolicitudRequisitos other = (RegistroSolicitudRequisitos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RegistroSolicitudRequisitos{" + "id=" + id
                + ", solicitudServicios=" + solicitudServicios
                + ", servicioRequisito=" + servicioRequisito + ", entregado=" + entregado
                + ", activo=" + activo + ", correccion=" + correccion + ", observacion=" + observacion
                + ", fechaCreacion=" + fechaCreacion + ", fechaModifica=" + fechaModifica
                + ", usuarioModifica=" + usuarioModifica + ", usuarioCreacion=" + usuarioCreacion + '}';
    }

}
