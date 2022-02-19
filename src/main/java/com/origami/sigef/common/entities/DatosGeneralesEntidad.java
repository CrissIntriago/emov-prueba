/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Criss Intriago
 */
@Entity
@Table(name = "datos_generales_entidad")
@NamedQueries({
        @NamedQuery(name = "DatosGeneralesEntidad.findAll", query = "SELECT d FROM DatosGeneralesEntidad d"),
        @NamedQuery(name = "DatosGeneralesEntidad.findById", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.id = :id"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByNombreEntidad", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.nombreEntidad = :nombreEntidad"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByAbreviatura", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.abreviatura = :abreviatura"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByRuc", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.ruc = :ruc"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByCodigoMef", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.codigoMef = :codigoMef"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByCodigoUnidadEjecutora", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.codigoUnidadEjecutora = :codigoUnidadEjecutora"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByDireccion", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.direccion = :direccion"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByNumero", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.numero = :numero"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByTelefono2", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.telefono2 = :telefono2"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByMovil", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.movil = :movil"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByEmail", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.email = :email"),
        @NamedQuery(name = "DatosGeneralesEntidad.findByEstado", query = "SELECT d FROM DatosGeneralesEntidad d WHERE d.estado = :estado")})
public class DatosGeneralesEntidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "nombre_entidad")
    private String nombreEntidad;
    @Basic(optional = false)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Basic(optional = false)
    @Column(name = "ruc")
    private String ruc;
    @Basic(optional = false)
    @Column(name = "codigo_mef")
    private String codigoMef;
    @Basic(optional = false)
    @Column(name = "codigo_unidad_ejecutora")
    private String codigoUnidadEjecutora;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "numero")
    private Short numero;
    @Basic(optional = false)
    @Column(name = "telefono_1")
    private String telefono1;
    @Column(name = "telefono_2")
    private String telefono2;
    @Column(name = "movil")
    private String movil;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    //    @Lob
    @Column(name = "logo")
    @Basic(fetch = FetchType.LAZY)
    private byte[] logo;
    @Column(name = "url_logo_reporte")
    private String urlLogoReporte;
    @Column(name = "url_marca_agua")
    private String urlMarcaAgua;
    @Transient
    private String urlLogoCompleta;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @JoinColumn(name = "canton", referencedColumnName = "id")
    @ManyToOne
    private Canton canton;
    @JoinColumn(name = "provincia", referencedColumnName = "id")
    @ManyToOne
    @GsonExcludeField
    private Provincia provincia;

    public DatosGeneralesEntidad() {
        this.estado = Boolean.TRUE;
    }

    public DatosGeneralesEntidad(Long id) {
        this.id = id;
    }

    public DatosGeneralesEntidad(Long id, String nombreEntidad, String abreviatura, String ruc, String codigoMef, String codigoUnidadEjecutora, String direccion, String telefono1, String email, boolean estado) {
        this.id = id;
        this.nombreEntidad = nombreEntidad;
        this.abreviatura = abreviatura;
        this.ruc = ruc;
        this.codigoMef = codigoMef;
        this.codigoUnidadEjecutora = codigoUnidadEjecutora;
        this.direccion = direccion;
        this.telefono1 = telefono1;
        this.email = email;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getCodigoMef() {
        return codigoMef;
    }

    public void setCodigoMef(String codigoMef) {
        this.codigoMef = codigoMef;
    }

    public String getCodigoUnidadEjecutora() {
        return codigoUnidadEjecutora;
    }

    public void setCodigoUnidadEjecutora(String codigoUnidadEjecutora) {
        this.codigoUnidadEjecutora = codigoUnidadEjecutora;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Short getNumero() {
        return numero;
    }

    public void setNumero(Short numero) {
        this.numero = numero;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getUrlLogoReporte() {
        return urlLogoReporte;
    }

    public void setUrlLogoReporte(String urlLogoReporte) {
        this.urlLogoReporte = urlLogoReporte;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getUrlLogoCompleta() {
        return urlLogoCompleta;
    }

    public void setUrlLogoCompleta(String urlLogoCompleta) {
        this.urlLogoCompleta = urlLogoCompleta;
    }

    public String getUrlMarcaAgua() {
        return urlMarcaAgua;
    }

    public void setUrlMarcaAgua(String urlLogoMarcaAgua) {
        this.urlMarcaAgua = urlLogoMarcaAgua;
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
        if (!(object instanceof DatosGeneralesEntidad)) {
            return false;
        }
        DatosGeneralesEntidad other = (DatosGeneralesEntidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DatosGeneralesEntidad[ id=" + id + " ]";
    }

}
