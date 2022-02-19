/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.asgard.Entity.FinaRenLiquidacion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class EmisionesRuralesExcelDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String codigoCatastral;
    private String nombre;
    private String apellidos;
    private String ciRuc;
    private String tipoPersona;
    private String utilidadPublica;
    private String nombrePredio;
    private String direccion;
    private String sector;
    private String parroquia;
    private BigInteger codParroquia;
    private BigDecimal avaluoTerreno;
    private BigDecimal avaluoCostruccion;
    private BigDecimal avaluoMejoras;
    private BigDecimal avaluoTotal;
    private Date fechaAvaluo;
    private BigDecimal areaTerreno;
    private BigDecimal areaConstruccion;
    private BigDecimal banda;
    private BigDecimal impuestoPredial;
    private BigDecimal tasaMantenimiento;
    private BigDecimal emision;
    private BigDecimal bomberos;
    private BigDecimal total;
    private String codigoCatastralAnterior;
    private List<FinaRenLiquidacion> renLiquidacionCollection;

    public EmisionesRuralesExcelDTO() {
    }

    public EmisionesRuralesExcelDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoCatastral() {
        return codigoCatastral;
    }

    public void setCodigoCatastral(String codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCiRuc() {
        return ciRuc;
    }

    public void setCiRuc(String ciRuc) {
        this.ciRuc = ciRuc;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getUtilidadPublica() {
        return utilidadPublica;
    }

    public void setUtilidadPublica(String utilidadPublica) {
        this.utilidadPublica = utilidadPublica;
    }

    public String getNombrePredio() {
        return nombrePredio;
    }

    public void setNombrePredio(String nombrePredio) {
        this.nombrePredio = nombrePredio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public BigInteger getCodParroquia() {
        return codParroquia;
    }

    public void setCodParroquia(BigInteger codParroquia) {
        this.codParroquia = codParroquia;
    }

    public BigDecimal getAvaluoTerreno() {
        return avaluoTerreno;
    }

    public void setAvaluoTerreno(BigDecimal avaluoTerreno) {
        this.avaluoTerreno = avaluoTerreno;
    }

    public BigDecimal getAvaluoCostruccion() {
        return avaluoCostruccion;
    }

    public void setAvaluoCostruccion(BigDecimal avaluoCostruccion) {
        this.avaluoCostruccion = avaluoCostruccion;
    }

    public BigDecimal getAvaluoMejoras() {
        return avaluoMejoras;
    }

    public void setAvaluoMejoras(BigDecimal avaluoMejoras) {
        this.avaluoMejoras = avaluoMejoras;
    }

    public BigDecimal getAvaluoTotal() {
        return avaluoTotal;
    }

    public void setAvaluoTotal(BigDecimal avaluoTotal) {
        this.avaluoTotal = avaluoTotal;
    }

    public Date getFechaAvaluo() {
        return fechaAvaluo;
    }

    public void setFechaAvaluo(Date fechaAvaluo) {
        this.fechaAvaluo = fechaAvaluo;
    }

    public BigDecimal getAreaTerreno() {
        return areaTerreno;
    }

    public void setAreaTerreno(BigDecimal areaTerreno) {
        this.areaTerreno = areaTerreno;
    }

    public BigDecimal getAreaConstruccion() {
        return areaConstruccion;
    }

    public void setAreaConstruccion(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public BigDecimal getBanda() {
        return banda;
    }

    public void setBanda(BigDecimal banda) {
        this.banda = banda;
    }

    public BigDecimal getImpuestoPredial() {
        return impuestoPredial;
    }

    public void setImpuestoPredial(BigDecimal impuestoPredial) {
        this.impuestoPredial = impuestoPredial;
    }

    public BigDecimal getTasaMantenimiento() {
        return tasaMantenimiento;
    }

    public void setTasaMantenimiento(BigDecimal tasaMantenimiento) {
        this.tasaMantenimiento = tasaMantenimiento;
    }

    public BigDecimal getEmision() {
        return emision;
    }

    public void setEmision(BigDecimal emision) {
        this.emision = emision;
    }

    public BigDecimal getBomberos() {
        return bomberos;
    }

    public void setBomberos(BigDecimal bomberos) {
        this.bomberos = bomberos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCodigoCatastralAnterior() {
        return codigoCatastralAnterior;
    }

    public void setCodigoCatastralAnterior(String codigoCatastralAnterior) {
        this.codigoCatastralAnterior = codigoCatastralAnterior;
    }

    public List<FinaRenLiquidacion> getRenLiquidacionCollection() {
        return renLiquidacionCollection;
    }

    public void setRenLiquidacionCollection(List<FinaRenLiquidacion> renLiquidacionCollection) {
        this.renLiquidacionCollection = renLiquidacionCollection;
    }

    
    
}
