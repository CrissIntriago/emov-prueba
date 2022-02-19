/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CatPredioModel {

    private Long id;
    private String codigoCatastral;
    private BigInteger codigoPredial;
    private String cedulaContribuyente;
    private String contribuyente;
    private String direccion;
    private BigDecimal totalPagar;
    private BigDecimal totalRubros;
    private BigDecimal totalDescuento;
    private BigDecimal totalInteres;
    private BigDecimal totalRecargo;
    private BigDecimal totalCoactiva;
    private BigDecimal totalAbonado;
    private List<FinaRenLiquidacionModel> liquidaciones;

    public CatPredioModel() {
    }

    public CatPredioModel(Long id, String codigoCatastral, BigInteger codigoPredial, String cedulaContribuyente,
            String contribuyente, String direccion, BigDecimal totalPagar, BigDecimal totalRubros, BigDecimal totalDescuento,
            BigDecimal totalInteres, BigDecimal totalRecargo, BigDecimal totalCoactiva, BigDecimal totalAbonado, List<FinaRenLiquidacionModel> liquidaciones) {
        this.id = id;
        this.totalAbonado = totalAbonado;
        this.codigoCatastral = codigoCatastral;
        this.codigoPredial = codigoPredial;
        this.cedulaContribuyente = cedulaContribuyente;
        this.contribuyente = contribuyente;
        this.direccion = direccion;
        this.totalPagar = totalPagar;
        this.totalRubros = totalRubros;
        this.totalDescuento = totalDescuento;
        this.totalInteres = totalInteres;
        this.totalRecargo = totalRecargo;
        this.totalCoactiva = totalCoactiva;
        this.liquidaciones = liquidaciones;
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

    public BigInteger getCodigoPredial() {
        return codigoPredial;
    }

    public void setCodigoPredial(BigInteger codigoPredial) {
        this.codigoPredial = codigoPredial;
    }

    public String getCedulaContribuyente() {
        return cedulaContribuyente;
    }

    public void setCedulaContribuyente(String cedulaContribuyente) {
        this.cedulaContribuyente = cedulaContribuyente;
    }

    public String getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getTotalRubros() {
        return totalRubros;
    }

    public void setTotalRubros(BigDecimal totalRubros) {
        this.totalRubros = totalRubros;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public BigDecimal getTotalInteres() {
        return totalInteres;
    }

    public void setTotalInteres(BigDecimal totalInteres) {
        this.totalInteres = totalInteres;
    }

    public BigDecimal getTotalRecargo() {
        return totalRecargo;
    }

    public void setTotalRecargo(BigDecimal totalRecargo) {
        this.totalRecargo = totalRecargo;
    }

    public BigDecimal getTotalCoactiva() {
        return totalCoactiva;
    }

    public void setTotalCoactiva(BigDecimal totalCoactiva) {
        this.totalCoactiva = totalCoactiva;
    }

    public BigDecimal getTotalAbonado() {
        return totalAbonado;
    }

    public void setTotalAbonado(BigDecimal totalAbonado) {
        this.totalAbonado = totalAbonado;
    }

    public List<FinaRenLiquidacionModel> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacionModel> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    @Override
    public String toString() {
        return "CatPredioModel{" + "id=" + id + ", codigoCatastral=" + codigoCatastral
                + ", codigoPredial=" + codigoPredial + ", cedulaContribuyente=" + cedulaContribuyente
                + ", contribuyente=" + contribuyente + ", direccion=" + direccion + ", totalPagar=" + totalPagar
                + ", totalRubros=" + totalRubros + ", totalDescuento=" + totalDescuento + ", totalInteres=" + totalInteres
                + ", totalRecargo=" + totalRecargo + ", totalCoactiva=" + totalCoactiva + ", totalAbonado=" + totalAbonado + '}';
    }

}
