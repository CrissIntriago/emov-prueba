/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class PredioModelWs implements Serializable {

    private Short sector;
    private Short mz;
    private Short cdla;
    private Short mzdla;
    private Short solar;
    private Short div1;
    private Short div2;
    private Short div3;
    private Short div4;
    private Short div5;
    private Short div6;
    private Short div7;
    private Short div8;
    private Short div9;
    private Short phv;
    private Short phh;
    private Short zona;
    private Short lote;
    private Short bloque;
    private Short piso;
    private Short unidad;
    private String canton;
    private String provincia;
    private String parroquia;
    private String codigoPredial;
    private String regCatastral;
    private String direccion;
    private String[] datosPropietario;
    private String ciudadela;
    private String urbanizacion;
    private Boolean habilitado;
    private Short numDepartamento;
    private String predialAnterior;
    private BigDecimal areaSolar;
    private BigDecimal areaConstruccion;
    private Integer anioDesde;
    private Integer anioHasta;
    private String tipoPredio;
    private BigDecimal avaSolar;
    private BigDecimal avaConstruccion;
    private String[] imgPredioUrl;
    private String[] imgCartografiaUrl;

    public void PredioModelWs() {

    }

    public Short getSector() {
        return sector;
    }

    public void setSector(Short sector) {
        this.sector = sector;
    }

    public Short getMz() {
        return mz;
    }

    public void setMz(Short mz) {
        this.mz = mz;
    }

    public Short getCdla() {
        return cdla;
    }

    public void setCdla(Short cdla) {
        this.cdla = cdla;
    }

    public Short getMzdla() {
        return mzdla;
    }

    public void setMzdla(Short mzdla) {
        this.mzdla = mzdla;
    }

    public Short getSolar() {
        return solar;
    }

    public void setSolar(Short solar) {
        this.solar = solar;
    }

    public Short getDiv1() {
        return div1;
    }

    public void setDiv1(Short div1) {
        this.div1 = div1;
    }

    public Short getDiv2() {
        return div2;
    }

    public void setDiv2(Short div2) {
        this.div2 = div2;
    }

    public Short getDiv3() {
        return div3;
    }

    public void setDiv3(Short div3) {
        this.div3 = div3;
    }

    public Short getDiv4() {
        return div4;
    }

    public void setDiv4(Short div4) {
        this.div4 = div4;
    }

    public Short getDiv5() {
        return div5;
    }

    public void setDiv5(Short div5) {
        this.div5 = div5;
    }

    public Short getDiv6() {
        return div6;
    }

    public void setDiv6(Short div6) {
        this.div6 = div6;
    }

    public Short getDiv7() {
        return div7;
    }

    public void setDiv7(Short div7) {
        this.div7 = div7;
    }

    public Short getDiv8() {
        return div8;
    }

    public void setDiv8(Short div8) {
        this.div8 = div8;
    }

    public Short getDiv9() {
        return div9;
    }

    public void setDiv9(Short div9) {
        this.div9 = div9;
    }

    public Short getPhv() {
        return phv;
    }

    public void setPhv(Short phv) {
        this.phv = phv;
    }

    public Short getPhh() {
        return phh;
    }

    public void setPhh(Short phh) {
        this.phh = phh;
    }

    public Short getZona() {
        return zona;
    }

    public void setZona(Short zona) {
        this.zona = zona;
    }

    public Short getLote() {
        return lote;
    }

    public void setLote(Short lote) {
        this.lote = lote;
    }

    public Short getBloque() {
        return bloque;
    }

    public void setBloque(Short bloque) {
        this.bloque = bloque;
    }

    public Short getPiso() {
        return piso;
    }

    public void setPiso(Short piso) {
        this.piso = piso;
    }

    public Short getUnidad() {
        return unidad;
    }

    public void setUnidad(Short unidad) {
        this.unidad = unidad;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getCodigoPredial() {
        return codigoPredial;
    }

    public void setCodigoPredial(String codigoPredial) {
        this.codigoPredial = codigoPredial;
    }

    public String getRegCatastral() {
        return regCatastral;
    }

    public void setRegCatastral(String regCatastral) {
        this.regCatastral = regCatastral;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String[] getDatosPropietario() {
        return datosPropietario;
    }

    public void setDatosPropietario(String[] datosPropietario) {
        this.datosPropietario = datosPropietario;
    }

    public String getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(String ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Short getNumDepartamento() {
        return numDepartamento;
    }

    public void setNumDepartamento(Short numDepartamento) {
        this.numDepartamento = numDepartamento;
    }

    public String getPredialAnterior() {
        return predialAnterior;
    }

    public void setPredialAnterior(String predialAnterior) {
        this.predialAnterior = predialAnterior;
    }

    public BigDecimal getAreaSolar() {
        return areaSolar;
    }

    public void setAreaSolar(BigDecimal areaSolar) {
        this.areaSolar = areaSolar;
    }

    public BigDecimal getAreaConstruccion() {
        return areaConstruccion;
    }

    public void setAreaConstruccion(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer anioHasta) {
        this.anioHasta = anioHasta;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public BigDecimal getAvaSolar() {
        return avaSolar;
    }

    public void setAvaSolar(BigDecimal avaSolar) {
        this.avaSolar = avaSolar;
    }

    public BigDecimal getAvaConstruccion() {
        return avaConstruccion;
    }

    public void setAvaConstruccion(BigDecimal avaConstruccion) {
        this.avaConstruccion = avaConstruccion;
    }

    public String[] getImgPredioUrl() {
        return imgPredioUrl;
    }

    public void setImgPredioUrl(String[] imgPredioUrl) {
        this.imgPredioUrl = imgPredioUrl;
    }

    public String[] getImgCartografiaUrl() {
        return imgCartografiaUrl;
    }

    public void setImgCartografiaUrl(String[] imgCartografiaUrl) {
        this.imgCartografiaUrl = imgCartografiaUrl;
    }
    
    

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
