/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_ge_tipo_tramite", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppGeTipoTramite.findAll", query = "SELECT a FROM AppGeTipoTramite a"),
    @NamedQuery(name = "AppGeTipoTramite.findById", query = "SELECT a FROM AppGeTipoTramite a WHERE a.id = :id"),
    @NamedQuery(name = "AppGeTipoTramite.findByDescripcion", query = "SELECT a FROM AppGeTipoTramite a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AppGeTipoTramite.findByActivitykey", query = "SELECT a FROM AppGeTipoTramite a WHERE a.activitykey = :activitykey"),
    @NamedQuery(name = "AppGeTipoTramite.findByRol", query = "SELECT a FROM AppGeTipoTramite a WHERE a.rol = :rol"),
    @NamedQuery(name = "AppGeTipoTramite.findByEstado", query = "SELECT a FROM AppGeTipoTramite a WHERE a.estado = :estado"),
    @NamedQuery(name = "AppGeTipoTramite.findByUrlEnlaceVideo", query = "SELECT a FROM AppGeTipoTramite a WHERE a.urlEnlaceVideo = :urlEnlaceVideo"),
    @NamedQuery(name = "AppGeTipoTramite.findByArchivoBpmn", query = "SELECT a FROM AppGeTipoTramite a WHERE a.archivoBpmn = :archivoBpmn"),
    @NamedQuery(name = "AppGeTipoTramite.findByAbreviatura", query = "SELECT a FROM AppGeTipoTramite a WHERE a.abreviatura = :abreviatura"),
    @NamedQuery(name = "AppGeTipoTramite.findByDepartamento", query = "SELECT a FROM AppGeTipoTramite a WHERE a.departamento = :departamento"),
    @NamedQuery(name = "AppGeTipoTramite.findByAsignacionManual", query = "SELECT a FROM AppGeTipoTramite a WHERE a.asignacionManual = :asignacionManual"),
    @NamedQuery(name = "AppGeTipoTramite.findByCarpeta", query = "SELECT a FROM AppGeTipoTramite a WHERE a.carpeta = :carpeta"),
    @NamedQuery(name = "AppGeTipoTramite.findByTieneDigitalizacion", query = "SELECT a FROM AppGeTipoTramite a WHERE a.tieneDigitalizacion = :tieneDigitalizacion"),
    @NamedQuery(name = "AppGeTipoTramite.findByUserDireccion", query = "SELECT a FROM AppGeTipoTramite a WHERE a.userDireccion = :userDireccion"),
    @NamedQuery(name = "AppGeTipoTramite.findByTramiteVentanillaUnica", query = "SELECT a FROM AppGeTipoTramite a WHERE a.tramiteVentanillaUnica = :tramiteVentanillaUnica"),
    @NamedQuery(name = "AppGeTipoTramite.findByInterno", query = "SELECT a FROM AppGeTipoTramite a WHERE a.interno = :interno"),
    @NamedQuery(name = "AppGeTipoTramite.findByDias", query = "SELECT a FROM AppGeTipoTramite a WHERE a.dias = :dias"),
    @NamedQuery(name = "AppGeTipoTramite.findByHoras", query = "SELECT a FROM AppGeTipoTramite a WHERE a.horas = :horas"),
    @NamedQuery(name = "AppGeTipoTramite.findByMinutos", query = "SELECT a FROM AppGeTipoTramite a WHERE a.minutos = :minutos"),
    @NamedQuery(name = "AppGeTipoTramite.findBySegundos", query = "SELECT a FROM AppGeTipoTramite a WHERE a.segundos = :segundos"),
    @NamedQuery(name = "AppGeTipoTramite.findByUrlImagen", query = "SELECT a FROM AppGeTipoTramite a WHERE a.urlImagen = :urlImagen"),
    @NamedQuery(name = "AppGeTipoTramite.findByOrden", query = "SELECT a FROM AppGeTipoTramite a WHERE a.orden = :orden")})
public class AppGeTipoTramite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 255)
    @Column(name = "activitykey")
    private String activitykey;
    @Column(name = "rol")
    private BigInteger rol;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 255)
    @Column(name = "url_enlace_video")
    private String urlEnlaceVideo;
    @Size(max = 100)
    @Column(name = "archivo_bpmn")
    private String archivoBpmn;
    @Size(max = 50)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "departamento")
    private BigInteger departamento;
    @Column(name = "asignacion_manual")
    private Boolean asignacionManual;
    @Size(max = 255)
    @Column(name = "carpeta")
    private String carpeta;
    @Column(name = "tiene_digitalizacion")
    private Boolean tieneDigitalizacion;
    @Size(max = 255)
    @Column(name = "user_direccion")
    private String userDireccion;
    @Column(name = "tramite_ventanilla_unica")
    private Boolean tramiteVentanillaUnica;
    @Column(name = "interno")
    private Boolean interno;
    @Column(name = "dias")
    private Integer dias;
    @Column(name = "horas")
    private Integer horas;
    @Column(name = "minutos")
    private Integer minutos;
    @Column(name = "segundos")
    private Integer segundos;
    @Size(max = 255)
    @Column(name = "url_imagen")
    private String urlImagen;
    @Column(name = "orden")
    private Integer orden;

    public AppGeTipoTramite() {
    }

    public AppGeTipoTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivitykey() {
        return activitykey;
    }

    public void setActivitykey(String activitykey) {
        this.activitykey = activitykey;
    }

    public BigInteger getRol() {
        return rol;
    }

    public void setRol(BigInteger rol) {
        this.rol = rol;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUrlEnlaceVideo() {
        return urlEnlaceVideo;
    }

    public void setUrlEnlaceVideo(String urlEnlaceVideo) {
        this.urlEnlaceVideo = urlEnlaceVideo;
    }

    public String getArchivoBpmn() {
        return archivoBpmn;
    }

    public void setArchivoBpmn(String archivoBpmn) {
        this.archivoBpmn = archivoBpmn;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public BigInteger getDepartamento() {
        return departamento;
    }

    public void setDepartamento(BigInteger departamento) {
        this.departamento = departamento;
    }

    public Boolean getAsignacionManual() {
        return asignacionManual;
    }

    public void setAsignacionManual(Boolean asignacionManual) {
        this.asignacionManual = asignacionManual;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public Boolean getTieneDigitalizacion() {
        return tieneDigitalizacion;
    }

    public void setTieneDigitalizacion(Boolean tieneDigitalizacion) {
        this.tieneDigitalizacion = tieneDigitalizacion;
    }

    public String getUserDireccion() {
        return userDireccion;
    }

    public void setUserDireccion(String userDireccion) {
        this.userDireccion = userDireccion;
    }

    public Boolean getTramiteVentanillaUnica() {
        return tramiteVentanillaUnica;
    }

    public void setTramiteVentanillaUnica(Boolean tramiteVentanillaUnica) {
        this.tramiteVentanillaUnica = tramiteVentanillaUnica;
    }

    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public Integer getSegundos() {
        return segundos;
    }

    public void setSegundos(Integer segundos) {
        this.segundos = segundos;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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
        if (!(object instanceof AppGeTipoTramite)) {
            return false;
        }
        AppGeTipoTramite other = (AppGeTipoTramite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppGeTipoTramite[ id=" + id + " ]";
    }
    
}
