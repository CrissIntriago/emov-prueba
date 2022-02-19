/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class CatParroquiaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private BigInteger codigoParroquia;
    private String descripcion;
    private String tipo;
    private Boolean estado = true;
    private Short codNac;
    private String idCanton;

    public CatParroquiaDTO() {
    }

    public CatParroquiaDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getCodigoParroquia() {
        return codigoParroquia;
    }

    public void setCodigoParroquia(BigInteger codigoParroquia) {
        this.codigoParroquia = codigoParroquia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getCodNac() {
        return codNac;
    }

    public void setCodNac(Short codNac) {
        this.codNac = codNac;
    }

    public String getIdCanton() {
        return idCanton;
    }

    public void setIdCanton(String idCanton) {
        this.idCanton = idCanton;
    }
    
    
}
