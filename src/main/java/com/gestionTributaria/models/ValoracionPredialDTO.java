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
 * //@author Administrator
 */
public class ValoracionPredialDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private long numVersion;

    private int periodo;

    private long numPredio;

    private Long idPredio;

    private String claveMz;

    private String codigoCatastral;

    private Boolean matriz;

    private BigInteger numeroMatriz;

    private BigDecimal areaSolar;

    private BigDecimal areaConstruccion;

    private BigDecimal areaSolarTipo;

    private BigDecimal frenteSolar;

    private BigDecimal frenteSolarTipo;

    private Integer cantServicios;

    private Integer subsector;

    private BigDecimal valorM2Subsector;

    private BigDecimal alfa;

    private BigDecimal beta;

    private BigDecimal y;

    private BigDecimal factExt;

    private BigDecimal factFrente;

    private BigDecimal factGeometrico;

    private BigDecimal factServicio;

    private BigDecimal factCorrelacion;

    private BigDecimal bandaImpositiva;

    private BigDecimal ipAnt;

    private BigDecimal ipAct;
    private BigDecimal rmjAnt;
    private BigDecimal rmjAct;
    private BigDecimal sneAnt;
    private BigDecimal sneAct;
    private BigDecimal rbAnt;
    private BigDecimal rbAct;
    private BigDecimal emisionAnt;
    private BigDecimal emisionAct;
    private BigDecimal avsolAnt;
    private BigDecimal avedifAnt;
    private BigDecimal avmunAnt;
    private BigDecimal tasaMant;
    private BigDecimal alicuota;
    private BigDecimal avaluoSolar;
    private BigDecimal avaluoEdificacion;
    private BigDecimal avaluoMunicipal;
    private BigDecimal avaluoMunicipalMatriz;
    private Date fecCre;

    private String usrCre;

    private String contribuyente;

    private Long liquidacion;

    public ValoracionPredialDTO() {
    }

    public ValoracionPredialDTO(Long id) {
        this.id = id;
    }

    public ValoracionPredialDTO(Long id, long numVersion, int periodo, long numPredio) {
        this.id = id;
        this.numVersion = numVersion;
        this.periodo = periodo;
        this.numPredio = numPredio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNumVersion() {
        return numVersion;
    }

    public void setNumVersion(long numVersion) {
        this.numVersion = numVersion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public long getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(long numPredio) {
        this.numPredio = numPredio;
    }

    public Long getIdPredio() {
        return idPredio;
    }

    public void setIdPredio(Long idPredio) {
        this.idPredio = idPredio;
    }

    public String getClaveMz() {
        return claveMz;
    }

    public void setClaveMz(String claveMz) {
        this.claveMz = claveMz;
    }

    public String getCodigoCatastral() {
        return codigoCatastral;
    }

    public void setCodigoCatastral(String codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public Boolean getMatriz() {
        return matriz;
    }

    public void setMatriz(Boolean matriz) {
        this.matriz = matriz;
    }

    public BigInteger getNumeroMatriz() {
        return numeroMatriz;
    }

    public void setNumeroMatriz(BigInteger numeroMatriz) {
        this.numeroMatriz = numeroMatriz;
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

    public BigDecimal getAreaSolarTipo() {
        return areaSolarTipo;
    }

    public void setAreaSolarTipo(BigDecimal areaSolarTipo) {
        this.areaSolarTipo = areaSolarTipo;
    }

    public BigDecimal getFrenteSolar() {
        return frenteSolar;
    }

    public void setFrenteSolar(BigDecimal frenteSolar) {
        this.frenteSolar = frenteSolar;
    }

    public BigDecimal getFrenteSolarTipo() {
        return frenteSolarTipo;
    }

    public void setFrenteSolarTipo(BigDecimal frenteSolarTipo) {
        this.frenteSolarTipo = frenteSolarTipo;
    }

    public Integer getCantServicios() {
        return cantServicios;
    }

    public void setCantServicios(Integer cantServicios) {
        this.cantServicios = cantServicios;
    }

    public Integer getSubsector() {
        return subsector;
    }

    public void setSubsector(Integer subsector) {
        this.subsector = subsector;
    }

    public BigDecimal getValorM2Subsector() {
        return valorM2Subsector;
    }

    public void setValorM2Subsector(BigDecimal valorM2Subsector) {
        this.valorM2Subsector = valorM2Subsector;
    }

    public BigDecimal getAlfa() {
        return alfa;
    }

    public void setAlfa(BigDecimal alfa) {
        this.alfa = alfa;
    }

    public BigDecimal getBeta() {
        return beta;
    }

    public void setBeta(BigDecimal beta) {
        this.beta = beta;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getFactExt() {
        return factExt;
    }

    public void setFactExt(BigDecimal factExt) {
        this.factExt = factExt;
    }

    public BigDecimal getFactFrente() {
        return factFrente;
    }

    public void setFactFrente(BigDecimal factFrente) {
        this.factFrente = factFrente;
    }

    public BigDecimal getFactGeometrico() {
        return factGeometrico;
    }

    public void setFactGeometrico(BigDecimal factGeometrico) {
        this.factGeometrico = factGeometrico;
    }

    public BigDecimal getFactServicio() {
        return factServicio;
    }

    public void setFactServicio(BigDecimal factServicio) {
        this.factServicio = factServicio;
    }

    public BigDecimal getFactCorrelacion() {
        return factCorrelacion;
    }

    public void setFactCorrelacion(BigDecimal factCorrelacion) {
        this.factCorrelacion = factCorrelacion;
    }

    public BigDecimal getBandaImpositiva() {
        return bandaImpositiva;
    }

    public void setBandaImpositiva(BigDecimal bandaImpositiva) {
        this.bandaImpositiva = bandaImpositiva;
    }

    public BigDecimal getIpAnt() {
        return ipAnt;
    }

    public void setIpAnt(BigDecimal ipAnt) {
        this.ipAnt = ipAnt;
    }

    public BigDecimal getIpAct() {
        return ipAct;
    }

    public void setIpAct(BigDecimal ipAct) {
        this.ipAct = ipAct;
    }

    public BigDecimal getRmjAnt() {
        return rmjAnt;
    }

    public void setRmjAnt(BigDecimal rmjAnt) {
        this.rmjAnt = rmjAnt;
    }

    public BigDecimal getRmjAct() {
        return rmjAct;
    }

    public void setRmjAct(BigDecimal rmjAct) {
        this.rmjAct = rmjAct;
    }

    public BigDecimal getSneAnt() {
        return sneAnt;
    }

    public void setSneAnt(BigDecimal sneAnt) {
        this.sneAnt = sneAnt;
    }

    public BigDecimal getSneAct() {
        return sneAct;
    }

    public void setSneAct(BigDecimal sneAct) {
        this.sneAct = sneAct;
    }

    public BigDecimal getRbAnt() {
        return rbAnt;
    }

    public void setRbAnt(BigDecimal rbAnt) {
        this.rbAnt = rbAnt;
    }

    public BigDecimal getRbAct() {
        return rbAct;
    }

    public void setRbAct(BigDecimal rbAct) {
        this.rbAct = rbAct;
    }

    public BigDecimal getEmisionAnt() {
        return emisionAnt;
    }

    public void setEmisionAnt(BigDecimal emisionAnt) {
        this.emisionAnt = emisionAnt;
    }

    public BigDecimal getEmisionAct() {
        return emisionAct;
    }

    public void setEmisionAct(BigDecimal emisionAct) {
        this.emisionAct = emisionAct;
    }

    public BigDecimal getAvsolAnt() {
        return avsolAnt;
    }

    public void setAvsolAnt(BigDecimal avsolAnt) {
        this.avsolAnt = avsolAnt;
    }

    public BigDecimal getAvedifAnt() {
        return avedifAnt;
    }

    public void setAvedifAnt(BigDecimal avedifAnt) {
        this.avedifAnt = avedifAnt;
    }

    public BigDecimal getAvmunAnt() {
        return avmunAnt;
    }

    public void setAvmunAnt(BigDecimal avmunAnt) {
        this.avmunAnt = avmunAnt;
    }

    public BigDecimal getTasaMant() {
        return tasaMant;
    }

    public void setTasaMant(BigDecimal tasaMant) {
        this.tasaMant = tasaMant;
    }

    public BigDecimal getAlicuota() {
        return alicuota;
    }

    public void setAlicuota(BigDecimal alicuota) {
        this.alicuota = alicuota;
    }

    public BigDecimal getAvaluoSolar() {
        return avaluoSolar;
    }

    public void setAvaluoSolar(BigDecimal avaluoSolar) {
        this.avaluoSolar = avaluoSolar;
    }

    public BigDecimal getAvaluoEdificacion() {
        return avaluoEdificacion;
    }

    public void setAvaluoEdificacion(BigDecimal avaluoEdificacion) {
        this.avaluoEdificacion = avaluoEdificacion;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public BigDecimal getAvaluoMunicipalMatriz() {
        return avaluoMunicipalMatriz;
    }

    public void setAvaluoMunicipalMatriz(BigDecimal avaluoMunicipalMatriz) {
        this.avaluoMunicipalMatriz = avaluoMunicipalMatriz;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public String getUsrCre() {
        return usrCre;
    }

    public void setUsrCre(String usrCre) {
        this.usrCre = usrCre;
    }

    public String getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }

    public Long getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(Long liquidacion) {
        this.liquidacion = liquidacion;
    }

    
    
    
}
