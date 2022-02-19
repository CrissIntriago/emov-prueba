/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLocalComercial;
import com.origami.sigef.common.util.Utils;
import java.io.InputStream;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ren_local_comercial_foto", schema = Utils.SCHEMA_SGM)
public class RenLocalComercialFoto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "file_oid")
    private Long fileOid;
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "local_comercial", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLocalComercial localComercial;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "sis_enabled")
    private Boolean sisEnabled;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "ruta")
    private String ruta;

    @Transient
    private InputStream inputStream;

    public RenLocalComercialFoto() {
    }

    public RenLocalComercialFoto(Long id) {
        this.id = id;
    }

    public RenLocalComercialFoto(Long id, long fileOid, String nombre) {
        this.id = id;
        this.fileOid = fileOid;
        this.nombre = nombre;
    }

    public RenLocalComercialFoto(Long fileOid, String nombre, String contentType, String descripcion, FinaRenLocalComercial localComercial) {
        this.fileOid = fileOid;
        this.nombre = nombre;
        this.contentType = contentType;
        this.descripcion = descripcion;
        this.localComercial = localComercial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileOid() {
        return fileOid;
    }

    public void setFileOid(Long fileOid) {
        this.fileOid = fileOid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

   

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getSisEnabled() {
        return sisEnabled;
    }

    public void setSisEnabled(Boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

}
