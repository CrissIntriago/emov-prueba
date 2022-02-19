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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_datos_predio", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppDatosPredio.findAll", query = "SELECT a FROM AppDatosPredio a"),
    @NamedQuery(name = "AppDatosPredio.findById", query = "SELECT a FROM AppDatosPredio a WHERE a.id = :id"),
    @NamedQuery(name = "AppDatosPredio.findByArchivo", query = "SELECT a FROM AppDatosPredio a WHERE a.archivo = :archivo"),
    @NamedQuery(name = "AppDatosPredio.findByAreaConstruccion", query = "SELECT a FROM AppDatosPredio a WHERE a.areaConstruccion = :areaConstruccion"),
    @NamedQuery(name = "AppDatosPredio.findByAreaTerreno", query = "SELECT a FROM AppDatosPredio a WHERE a.areaTerreno = :areaTerreno"),
    @NamedQuery(name = "AppDatosPredio.findByAvaluoComercial", query = "SELECT a FROM AppDatosPredio a WHERE a.avaluoComercial = :avaluoComercial"),
    @NamedQuery(name = "AppDatosPredio.findByBarrio", query = "SELECT a FROM AppDatosPredio a WHERE a.barrio = :barrio"),
    @NamedQuery(name = "AppDatosPredio.findByCalle", query = "SELECT a FROM AppDatosPredio a WHERE a.calle = :calle"),
    @NamedQuery(name = "AppDatosPredio.findByClaveCatastral", query = "SELECT a FROM AppDatosPredio a WHERE a.claveCatastral = :claveCatastral"),
    @NamedQuery(name = "AppDatosPredio.findByClaveCatastralAnterior", query = "SELECT a FROM AppDatosPredio a WHERE a.claveCatastralAnterior = :claveCatastralAnterior"),
    @NamedQuery(name = "AppDatosPredio.findByDatosPropietario", query = "SELECT a FROM AppDatosPredio a WHERE a.datosPropietario = :datosPropietario"),
    @NamedQuery(name = "AppDatosPredio.findByIdentificacionPropietario", query = "SELECT a FROM AppDatosPredio a WHERE a.identificacionPropietario = :identificacionPropietario"),
    @NamedQuery(name = "AppDatosPredio.findByParroquia", query = "SELECT a FROM AppDatosPredio a WHERE a.parroquia = :parroquia"),
    @NamedQuery(name = "AppDatosPredio.findByParroquiaAnterior", query = "SELECT a FROM AppDatosPredio a WHERE a.parroquiaAnterior = :parroquiaAnterior"),
    @NamedQuery(name = "AppDatosPredio.findByTipoPredio", query = "SELECT a FROM AppDatosPredio a WHERE a.tipoPredio = :tipoPredio"),
    @NamedQuery(name = "AppDatosPredio.findByUrlDocumento", query = "SELECT a FROM AppDatosPredio a WHERE a.urlDocumento = :urlDocumento"),
    @NamedQuery(name = "AppDatosPredio.findByUsuario", query = "SELECT a FROM AppDatosPredio a WHERE a.usuario = :usuario"),
    @NamedQuery(name = "AppDatosPredio.findByTramite", query = "SELECT a FROM AppDatosPredio a WHERE a.tramite = :tramite"),
    @NamedQuery(name = "AppDatosPredio.findByEnviarDocumento", query = "SELECT a FROM AppDatosPredio a WHERE a.enviarDocumento = :enviarDocumento"),
    @NamedQuery(name = "AppDatosPredio.findByUrlDominio", query = "SELECT a FROM AppDatosPredio a WHERE a.urlDominio = :urlDominio"),
    @NamedQuery(name = "AppDatosPredio.findByArchivoDevCarp", query = "SELECT a FROM AppDatosPredio a WHERE a.archivoDevCarp = :archivoDevCarp"),
    @NamedQuery(name = "AppDatosPredio.findByFecha", query = "SELECT a FROM AppDatosPredio a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AppDatosPredio.findByArchivoAbg", query = "SELECT a FROM AppDatosPredio a WHERE a.archivoAbg = :archivoAbg")})
public class AppDatosPredio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "archivo")
    private String archivo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area_construccion")
    private Double areaConstruccion;
    @Column(name = "area_terreno")
    private Double areaTerreno;
    @Column(name = "avaluo_comercial")
    private Double avaluoComercial;
    @Size(max = 255)
    @Column(name = "barrio")
    private String barrio;
    @Size(max = 255)
    @Column(name = "calle")
    private String calle;
    @Size(max = 255)
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Size(max = 255)
    @Column(name = "clave_catastral_anterior")
    private String claveCatastralAnterior;
    @Size(max = 255)
    @Column(name = "datos_propietario")
    private String datosPropietario;
    @Size(max = 255)
    @Column(name = "identificacion_propietario")
    private String identificacionPropietario;
    @Size(max = 255)
    @Column(name = "parroquia")
    private String parroquia;
    @Size(max = 255)
    @Column(name = "parroquia_anterior")
    private String parroquiaAnterior;
    @Size(max = 255)
    @Column(name = "tipo_predio")
    private String tipoPredio;
    @Size(max = 255)
    @Column(name = "url_documento")
    private String urlDocumento;
    @Size(max = 255)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Column(name = "enviar_documento")
    private Boolean enviarDocumento;
    @Size(max = 200)
    @Column(name = "url_dominio")
    private String urlDominio;
    @Size(max = 2147483647)
    @Column(name = "archivo_dev_carp")
    private String archivoDevCarp;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 2147483647)
    @Column(name = "archivo_abg")
    private String archivoAbg;

    public AppDatosPredio() {
    }

    public AppDatosPredio(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Double getAreaConstruccion() {
        return areaConstruccion;
    }

    public void setAreaConstruccion(Double areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public Double getAreaTerreno() {
        return areaTerreno;
    }

    public void setAreaTerreno(Double areaTerreno) {
        this.areaTerreno = areaTerreno;
    }

    public Double getAvaluoComercial() {
        return avaluoComercial;
    }

    public void setAvaluoComercial(Double avaluoComercial) {
        this.avaluoComercial = avaluoComercial;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getClaveCatastralAnterior() {
        return claveCatastralAnterior;
    }

    public void setClaveCatastralAnterior(String claveCatastralAnterior) {
        this.claveCatastralAnterior = claveCatastralAnterior;
    }

    public String getDatosPropietario() {
        return datosPropietario;
    }

    public void setDatosPropietario(String datosPropietario) {
        this.datosPropietario = datosPropietario;
    }

    public String getIdentificacionPropietario() {
        return identificacionPropietario;
    }

    public void setIdentificacionPropietario(String identificacionPropietario) {
        this.identificacionPropietario = identificacionPropietario;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getParroquiaAnterior() {
        return parroquiaAnterior;
    }

    public void setParroquiaAnterior(String parroquiaAnterior) {
        this.parroquiaAnterior = parroquiaAnterior;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public String getUrlDocumento() {
        return urlDocumento;
    }

    public void setUrlDocumento(String urlDocumento) {
        this.urlDocumento = urlDocumento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public Boolean getEnviarDocumento() {
        return enviarDocumento;
    }

    public void setEnviarDocumento(Boolean enviarDocumento) {
        this.enviarDocumento = enviarDocumento;
    }

    public String getUrlDominio() {
        return urlDominio;
    }

    public void setUrlDominio(String urlDominio) {
        this.urlDominio = urlDominio;
    }

    public String getArchivoDevCarp() {
        return archivoDevCarp;
    }

    public void setArchivoDevCarp(String archivoDevCarp) {
        this.archivoDevCarp = archivoDevCarp;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getArchivoAbg() {
        return archivoAbg;
    }

    public void setArchivoAbg(String archivoAbg) {
        this.archivoAbg = archivoAbg;
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
        if (!(object instanceof AppDatosPredio)) {
            return false;
        }
        AppDatosPredio other = (AppDatosPredio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppDatosPredio[ id=" + id + " ]";
    }
    
}
