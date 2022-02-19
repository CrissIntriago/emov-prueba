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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "docu_notificacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocuNotificacion.findAll", query = "SELECT d FROM DocuNotificacion d"),
    @NamedQuery(name = "DocuNotificacion.findById", query = "SELECT d FROM DocuNotificacion d WHERE d.id = :id"),
    @NamedQuery(name = "DocuNotificacion.findBySecuencia", query = "SELECT d FROM DocuNotificacion d WHERE d.secuencia = :secuencia"),
    @NamedQuery(name = "DocuNotificacion.findByAnio", query = "SELECT d FROM DocuNotificacion d WHERE d.anio = :anio"),
    @NamedQuery(name = "DocuNotificacion.findByCodigo", query = "SELECT d FROM DocuNotificacion d WHERE d.codigo = :codigo"),
    @NamedQuery(name = "DocuNotificacion.findByFecha", query = "SELECT d FROM DocuNotificacion d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "DocuNotificacion.findByContenido", query = "SELECT d FROM DocuNotificacion d WHERE d.contenido = :contenido"),
    @NamedQuery(name = "DocuNotificacion.findByTramite", query = "SELECT d FROM DocuNotificacion d WHERE d.tramite = :tramite"),
    @NamedQuery(name = "DocuNotificacion.findByColor", query = "SELECT d FROM DocuNotificacion d WHERE d.color = :color"),
    @NamedQuery(name = "DocuNotificacion.findByFechaRevision", query = "SELECT d FROM DocuNotificacion d WHERE d.fechaRevision = :fechaRevision"),
    @NamedQuery(name = "DocuNotificacion.findByObservacion", query = "SELECT d FROM DocuNotificacion d WHERE d.observacion = :observacion"),
    @NamedQuery(name = "DocuNotificacion.findByRevisada", query = "SELECT d FROM DocuNotificacion d WHERE d.revisada = :revisada"),
    @NamedQuery(name = "DocuNotificacion.findByTitulo", query = "SELECT d FROM DocuNotificacion d WHERE d.titulo = :titulo"),
    @NamedQuery(name = "DocuNotificacion.findByUsuario", query = "SELECT d FROM DocuNotificacion d WHERE d.usuario = :usuario"),
    @NamedQuery(name = "DocuNotificacion.findByAprobado", query = "SELECT d FROM DocuNotificacion d WHERE d.aprobado = :aprobado")})
public class DocuNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "secuencia")
    private BigInteger secuencia;
    @Column(name = "anio")
    private BigInteger anio;
    @Size(max = 100)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 2147483647)
    @Column(name = "contenido")
    private String contenido;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Size(max = 255)
    @Column(name = "color")
    private String color;
    @Column(name = "fecha_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRevision;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "revisada")
    private Boolean revisada;
    @Size(max = 255)
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "usuario")
    private BigInteger usuario;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @JoinColumn(name = "solicitud_servicio", referencedColumnName = "id")
    @ManyToOne
    private AppSolicitudServicios solicitudServicio;
    @JoinColumn(name = "tipo_notificacion", referencedColumnName = "id")
    @ManyToOne
    private DocuTipoNotificacion tipoNotificacion;
    @JoinColumn(name = "solicitud_ventanilla", referencedColumnName = "id")
    @ManyToOne
    private VentSolicitudVentanilla solicitudVentanilla;
    @OneToMany(mappedBy = "notificacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<VentRequisitosErrores> ventRequisitosErroresList;

    public DocuNotificacion() {
    }

    public DocuNotificacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getRevisada() {
        return revisada;
    }

    public void setRevisada(Boolean revisada) {
        this.revisada = revisada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public AppSolicitudServicios getSolicitudServicio() {
        return solicitudServicio;
    }

    public void setSolicitudServicio(AppSolicitudServicios solicitudServicio) {
        this.solicitudServicio = solicitudServicio;
    }

    public DocuTipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(DocuTipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public VentSolicitudVentanilla getSolicitudVentanilla() {
        return solicitudVentanilla;
    }

    public void setSolicitudVentanilla(VentSolicitudVentanilla solicitudVentanilla) {
        this.solicitudVentanilla = solicitudVentanilla;
    }

    
    public List<VentRequisitosErrores> getVentRequisitosErroresList() {
        return ventRequisitosErroresList;
    }

    public void setVentRequisitosErroresList(List<VentRequisitosErrores> ventRequisitosErroresList) {
        this.ventRequisitosErroresList = ventRequisitosErroresList;
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
        if (!(object instanceof DocuNotificacion)) {
            return false;
        }
        DocuNotificacion other = (DocuNotificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.DocuNotificacion[ id=" + id + " ]";
    }
    
}
