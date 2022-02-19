/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class CatPredioPropieatrioDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Boolean afectaNombreTitulo;
    private Boolean copropietario;
    private String ciuCedRuc;
    private String ente;
    private Boolean esResidente;
    private String estado;
    private Date fecha;
    private Date fechaInscripcionTitulos;
    private String modificado;
    private String observaciones;
    private String observacionCoop;
    private BigInteger porcentajePosecion;
    private BigInteger tipo;
    private String usuario;

    private CatPredioModel predio;

    public CatPredioPropieatrioDTO() {
    }

    public CatPredioPropieatrioDTO(Long id) {
        this.id = id;
    }

    public CatPredioPropieatrioDTO(Long id, String ente) {
        this.id = id;
        this.ente = ente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAfectaNombreTitulo() {
        return afectaNombreTitulo;
    }

    public void setAfectaNombreTitulo(Boolean afectaNombreTitulo) {
        this.afectaNombreTitulo = afectaNombreTitulo;
    }

    public Boolean getCopropietario() {
        return copropietario;
    }

    public void setCopropietario(Boolean copropietario) {
        this.copropietario = copropietario;
    }

    public String getCiuCedRuc() {
        return ciuCedRuc;
    }

    public void setCiuCedRuc(String ciuCedRuc) {
        this.ciuCedRuc = ciuCedRuc;
    }

    public String getEnte() {
        return ente;
    }

    public void setEnte(String ente) {
        this.ente = ente;
    }

    public Boolean getEsResidente() {
        return esResidente;
    }

    public void setEsResidente(Boolean esResidente) {
        this.esResidente = esResidente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaInscripcionTitulos() {
        return fechaInscripcionTitulos;
    }

    public void setFechaInscripcionTitulos(Date fechaInscripcionTitulos) {
        this.fechaInscripcionTitulos = fechaInscripcionTitulos;
    }

    public String getModificado() {
        return modificado;
    }

    public void setModificado(String modificado) {
        this.modificado = modificado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservacionCoop() {
        return observacionCoop;
    }

    public void setObservacionCoop(String observacionCoop) {
        this.observacionCoop = observacionCoop;
    }

    public BigInteger getPorcentajePosecion() {
        return porcentajePosecion;
    }

    public void setPorcentajePosecion(BigInteger porcentajePosecion) {
        this.porcentajePosecion = porcentajePosecion;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public CatPredioModel getPredio() {
        return predio;
    }

    public void setPredio(CatPredioModel predio) {
        this.predio = predio;
    }
    
    
}
