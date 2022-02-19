/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class CatPredioRusticoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String regCatastral;
    private String idPredial;
    private String parroquia;
    private Integer poligono;
    private Date fecha;
    private String nombrePredio;
    private BigDecimal superficie;
    private BigDecimal avaluoCatastral;
    private BigInteger tipo;
    private BigInteger influencia;
    private String sitio;
    private Boolean estado;
    private BigDecimal rebaja;
    private String usuarioIngreso;
    private Date fechaIngreso;
    private BigInteger numPredioRustico;
    private String destino;
    private String tenencia;
    private String propietario;

    public CatPredioRusticoDTO() {
    }

    public CatPredioRusticoDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegCatastral() {
        return regCatastral;
    }

    public void setRegCatastral(String regCatastral) {
        this.regCatastral = regCatastral;
    }

    public String getIdPredial() {
        return idPredial;
    }

    public void setIdPredial(String idPredial) {
        this.idPredial = idPredial;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public Integer getPoligono() {
        return poligono;
    }

    public void setPoligono(Integer poligono) {
        this.poligono = poligono;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombrePredio() {
        return nombrePredio;
    }

    public void setNombrePredio(String nombrePredio) {
        this.nombrePredio = nombrePredio;
    }

    public BigDecimal getSuperficie() {
        return superficie;
    }

    public void setSuperficie(BigDecimal superficie) {
        this.superficie = superficie;
    }

    public BigDecimal getAvaluoCatastral() {
        return avaluoCatastral;
    }

    public void setAvaluoCatastral(BigDecimal avaluoCatastral) {
        this.avaluoCatastral = avaluoCatastral;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public BigInteger getInfluencia() {
        return influencia;
    }

    public void setInfluencia(BigInteger influencia) {
        this.influencia = influencia;
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getRebaja() {
        return rebaja;
    }

    public void setRebaja(BigDecimal rebaja) {
        this.rebaja = rebaja;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigInteger getNumPredioRustico() {
        return numPredioRustico;
    }

    public void setNumPredioRustico(BigInteger numPredioRustico) {
        this.numPredioRustico = numPredioRustico;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTenencia() {
        return tenencia;
    }

    public void setTenencia(String tenencia) {
        this.tenencia = tenencia;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

}
