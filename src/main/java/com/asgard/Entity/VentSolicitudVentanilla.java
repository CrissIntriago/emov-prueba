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
@Table(name = "vent_solicitud_ventanilla", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VentSolicitudVentanilla.findAll", query = "SELECT v FROM VentSolicitudVentanilla v"),
    @NamedQuery(name = "VentSolicitudVentanilla.findById", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.id = :id"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByClaveCatastral", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.claveCatastral = :claveCatastral"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByDatosPropietario", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.datosPropietario = :datosPropietario"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByDireccion", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.direccion = :direccion"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByFecha", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByIdentificacionPropietario", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.identificacionPropietario = :identificacionPropietario"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByNumTramite", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.numTramite = :numTramite"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByTipoPredio", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.tipoPredio = :tipoPredio"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByTipoTramite", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.tipoTramite = :tipoTramite"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByEstado", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.estado = :estado"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByBarrioLocal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.barrioLocal = :barrioLocal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByCantonLocal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.cantonLocal = :cantonLocal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByCodigoLocal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.codigoLocal = :codigoLocal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByNombreLocal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.nombreLocal = :nombreLocal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByNombreRepresentanteLegal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.nombreRepresentanteLegal = :nombreRepresentanteLegal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByParroquiaLocal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.parroquiaLocal = :parroquiaLocal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByProvinciaLocal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.provinciaLocal = :provinciaLocal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByRucLocal", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.rucLocal = :rucLocal"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByObservacion", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.observacion = :observacion"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByEmisionAlcance", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.emisionAlcance = :emisionAlcance"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByVerificado", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.verificado = :verificado"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByEstadoSolicitud", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.estadoSolicitud = :estadoSolicitud"),
    @NamedQuery(name = "VentSolicitudVentanilla.findByObservacionArchivo", query = "SELECT v FROM VentSolicitudVentanilla v WHERE v.observacionArchivo = :observacionArchivo")})
public class VentSolicitudVentanilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Size(max = 255)
    @Column(name = "datos_propietario")
    private String datosPropietario;
    @Size(max = 255)
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 255)
    @Column(name = "identificacion_propietario")
    private String identificacionPropietario;
    @Size(max = 255)
    @Column(name = "num_tramite")
    private String numTramite;
    @Size(max = 255)
    @Column(name = "tipo_predio")
    private String tipoPredio;
    @Column(name = "tipo_tramite")
    private BigInteger tipoTramite;
    @Column(name = "estado")
    private Integer estado;
    @Size(max = 255)
    @Column(name = "barrio_local")
    private String barrioLocal;
    @Size(max = 255)
    @Column(name = "canton_local")
    private String cantonLocal;
    @Size(max = 255)
    @Column(name = "codigo_local")
    private String codigoLocal;
    @Size(max = 255)
    @Column(name = "nombre_local")
    private String nombreLocal;
    @Size(max = 255)
    @Column(name = "nombre_representante_legal")
    private String nombreRepresentanteLegal;
    @Size(max = 255)
    @Column(name = "parroquia_local")
    private String parroquiaLocal;
    @Size(max = 255)
    @Column(name = "provincia_local")
    private String provinciaLocal;
    @Size(max = 255)
    @Column(name = "ruc_local")
    private String rucLocal;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "emision_alcance")
    private Integer emisionAlcance;
    @Column(name = "verificado")
    private Boolean verificado;
    @Column(name = "estado_solicitud")
    private Integer estadoSolicitud;
    @Size(max = 255)
    @Column(name = "observacion_archivo")
    private String observacionArchivo;
    @OneToMany(mappedBy = "solicitud", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<VentSolicitudVentanillaDocs> ventSolicitudVentanillaDocsList;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne
    private VentUsuariosVentanilla usuario;
    @OneToMany(mappedBy = "solicitudVentanilla", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DocuNotificacion> docuNotificacionList;
    @OneToMany(mappedBy = "solicitudVentanilla", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<VentRequisitosErrores> ventRequisitosErroresList;

    public VentSolicitudVentanilla() {
    }

    public VentSolicitudVentanilla(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getDatosPropietario() {
        return datosPropietario;
    }

    public void setDatosPropietario(String datosPropietario) {
        this.datosPropietario = datosPropietario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdentificacionPropietario() {
        return identificacionPropietario;
    }

    public void setIdentificacionPropietario(String identificacionPropietario) {
        this.identificacionPropietario = identificacionPropietario;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public BigInteger getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(BigInteger tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getBarrioLocal() {
        return barrioLocal;
    }

    public void setBarrioLocal(String barrioLocal) {
        this.barrioLocal = barrioLocal;
    }

    public String getCantonLocal() {
        return cantonLocal;
    }

    public void setCantonLocal(String cantonLocal) {
        this.cantonLocal = cantonLocal;
    }

    public String getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(String codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getNombreRepresentanteLegal() {
        return nombreRepresentanteLegal;
    }

    public void setNombreRepresentanteLegal(String nombreRepresentanteLegal) {
        this.nombreRepresentanteLegal = nombreRepresentanteLegal;
    }

    public String getParroquiaLocal() {
        return parroquiaLocal;
    }

    public void setParroquiaLocal(String parroquiaLocal) {
        this.parroquiaLocal = parroquiaLocal;
    }

    public String getProvinciaLocal() {
        return provinciaLocal;
    }

    public void setProvinciaLocal(String provinciaLocal) {
        this.provinciaLocal = provinciaLocal;
    }

    public String getRucLocal() {
        return rucLocal;
    }

    public void setRucLocal(String rucLocal) {
        this.rucLocal = rucLocal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getEmisionAlcance() {
        return emisionAlcance;
    }

    public void setEmisionAlcance(Integer emisionAlcance) {
        this.emisionAlcance = emisionAlcance;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Integer getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(Integer estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public String getObservacionArchivo() {
        return observacionArchivo;
    }

    public void setObservacionArchivo(String observacionArchivo) {
        this.observacionArchivo = observacionArchivo;
    }

    
    public List<VentSolicitudVentanillaDocs> getVentSolicitudVentanillaDocsList() {
        return ventSolicitudVentanillaDocsList;
    }

    public void setVentSolicitudVentanillaDocsList(List<VentSolicitudVentanillaDocs> ventSolicitudVentanillaDocsList) {
        this.ventSolicitudVentanillaDocsList = ventSolicitudVentanillaDocsList;
    }

    public VentUsuariosVentanilla getUsuario() {
        return usuario;
    }

    public void setUsuario(VentUsuariosVentanilla usuario) {
        this.usuario = usuario;
    }

    
    public List<DocuNotificacion> getDocuNotificacionList() {
        return docuNotificacionList;
    }

    public void setDocuNotificacionList(List<DocuNotificacion> docuNotificacionList) {
        this.docuNotificacionList = docuNotificacionList;
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
        if (!(object instanceof VentSolicitudVentanilla)) {
            return false;
        }
        VentSolicitudVentanilla other = (VentSolicitudVentanilla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.VentSolicitudVentanilla[ id=" + id + " ]";
    }
    
}
