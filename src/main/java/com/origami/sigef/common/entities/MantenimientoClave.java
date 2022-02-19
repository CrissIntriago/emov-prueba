/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "mantenimiento_clave", schema = "auth")
@NamedQueries({
    @NamedQuery(name = "MantenimientoClave.findAll", query = "SELECT m FROM MantenimientoClave m")
    , @NamedQuery(name = "MantenimientoClave.findById", query = "SELECT m FROM MantenimientoClave m WHERE m.id = :id")
    , @NamedQuery(name = "MantenimientoClave.findByUserName", query = "SELECT m FROM MantenimientoClave m WHERE m.userName = :userName")
    , @NamedQuery(name = "MantenimientoClave.findByCodigoVerificacion", query = "SELECT m FROM MantenimientoClave m WHERE m.codigoVerificacion = :codigoVerificacion")
    , @NamedQuery(name = "MantenimientoClave.findByUrlGenerada", query = "SELECT m FROM MantenimientoClave m WHERE m.urlGenerada = :urlGenerada")
    , @NamedQuery(name = "MantenimientoClave.findByFechaGeneracion", query = "SELECT m FROM MantenimientoClave m WHERE m.fechaGeneracion = :fechaGeneracion")
    , @NamedQuery(name = "MantenimientoClave.findByTiempoValidez", query = "SELECT m FROM MantenimientoClave m WHERE m.tiempoValidez = :tiempoValidez")})
public class MantenimientoClave implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 2147483647)
    @Column(name = "codigo_verificacion")
    private String codigoVerificacion;
    @Column(name = "servicio")
    private Boolean servicio;
    @Size(max = 2147483647)
    @Column(name = "url_generada")
    private String urlGenerada;
    @Column(name = "fecha_generacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    @Column(name = "tiempo_validez")
    private BigInteger tiempoValidez;
    @Column(name = "correo_enviado")
    private String correoEnviado;
    @Column(name = "activo")
    private Boolean activo;

    public MantenimientoClave() {
    }

    public MantenimientoClave(String userName, String codigoVerificacion, Boolean servicio, Date fechaGeneracion, BigInteger tiempoValidez, String correoEnviado) {
        this.userName = userName;
        this.codigoVerificacion = codigoVerificacion;
        this.servicio = servicio;
        this.fechaGeneracion = fechaGeneracion;
        this.tiempoValidez = tiempoValidez;
        this.activo = Boolean.TRUE;
        this.correoEnviado = correoEnviado;
    }

    public MantenimientoClave(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Boolean getServicio() {
        return servicio;
    }

    public void setServicio(Boolean servicio) {
        this.servicio = servicio;
    }

    public String getUrlGenerada() {
        return urlGenerada;
    }

    public void setUrlGenerada(String urlGenerada) {
        this.urlGenerada = urlGenerada;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public BigInteger getTiempoValidez() {
        return tiempoValidez;
    }

    public void setTiempoValidez(BigInteger tiempoValidez) {
        this.tiempoValidez = tiempoValidez;
    }

    public String getCorreoEnviado() {
        return correoEnviado;
    }
    
    public String getCorreoCodificado() {
        if (correoEnviado != null && !correoEnviado.isEmpty()) {
            return correoEnviado.replaceAll("(?<=.{3}).(?=.*@)", "*");
        }
        return correoEnviado;
    }

    public void setCorreoEnviado(String correoEnviado) {
        this.correoEnviado = correoEnviado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
        if (!(object instanceof MantenimientoClave)) {
            return false;
        }
        MantenimientoClave other = (MantenimientoClave) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.contabilidad.entity.MantenimientoClave[ id=" + id + " ]";
    }

}
