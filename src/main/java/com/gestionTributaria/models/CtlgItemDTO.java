/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class CtlgItemDTO implements Serializable {

    private BigInteger referencia;
    private BigInteger padre;
    private Long id;
    private String valor;
    private String codename;
    private BigDecimal factor;
    private BigDecimal rangoDesde;
    private BigDecimal rangoHasta;
    private Integer orden;
    private BigInteger hijo;
    private Boolean isDefault = false;
    private String catalogo;
    
    public CtlgItemDTO(){
    }

    public BigInteger getReferencia() {
        return referencia;
    }

    public void setReferencia(BigInteger referencia) {
        this.referencia = referencia;
    }

    public BigInteger getPadre() {
        return padre;
    }

    public void setPadre(BigInteger padre) {
        this.padre = padre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public BigDecimal getRangoDesde() {
        return rangoDesde;
    }

    public void setRangoDesde(BigDecimal rangoDesde) {
        this.rangoDesde = rangoDesde;
    }

    public BigDecimal getRangoHasta() {
        return rangoHasta;
    }

    public void setRangoHasta(BigDecimal rangoHasta) {
        this.rangoHasta = rangoHasta;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public BigInteger getHijo() {
        return hijo;
    }

    public void setHijo(BigInteger hijo) {
        this.hijo = hijo;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }
    
    
}
