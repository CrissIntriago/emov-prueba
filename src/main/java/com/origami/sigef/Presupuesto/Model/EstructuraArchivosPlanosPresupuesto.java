/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

/**
 *
 * @author Sandra Arroba
 */
public class EstructuraArchivosPlanosPresupuesto {
    
    private String periodo;
    private String tipoPresupuesto;
    private String grupo;
    private String subgrupo;
    private String item; 
    private String funcion; /*6ta columna de gastos de CP*/
    private String orientacionGasto;
    private Double valor; /*valor inicial para CP*/
    private Double reforma;
    private Double codificado;
    private Double compromiso; /*solo para gastos de CP*/
    private Double devengado; 
    private Double recaudadoPagado;
    private Double saldoPorComprometer; /*solo para gastos de CP*/
    private Double saldoPorDevengar;
    
    private String codigo;
    
    public EstructuraArchivosPlanosPresupuesto() {
        this.funcion = "000";
        this.codigo = grupo+subgrupo+item;
    }
    
    /*Presupuesto Inicial Ingresos*/
    public EstructuraArchivosPlanosPresupuesto(String periodo, String tipoPresupuesto, String grupo, String subgrupo, String item, Double valor) {
        this.periodo = periodo;
        this.tipoPresupuesto = tipoPresupuesto;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.item = item;
        this.valor = valor;
        this.codigo = grupo + subgrupo + item;
    }
    
    /*Presupuesto Inicial Egresos*/
    public EstructuraArchivosPlanosPresupuesto(String periodo, String tipoPresupuesto, String grupo, String subgrupo, String item, String funcion, String orientacionGasto, Double valor) {
        this.periodo = periodo;
        this.tipoPresupuesto = tipoPresupuesto;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.item = item;
        this.funcion = "000";
        this.orientacionGasto = orientacionGasto;
        this.valor = valor;
        this.codigo = grupo + subgrupo + item;
    }

    public EstructuraArchivosPlanosPresupuesto(String periodo, String tipoPresupuesto, String grupo, String subgrupo, String item, Double valor, Double reforma, Double codificado, Double devengado, Double recaudadoPagado, Double saldoPorDevengar) {
        this.periodo = periodo;
        this.tipoPresupuesto = tipoPresupuesto;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.item = item;
        this.valor = valor;
        this.reforma = reforma;
        this.codificado = codificado;
        this.devengado = devengado;
        this.recaudadoPagado = recaudadoPagado;
        this.saldoPorDevengar = saldoPorDevengar;
        this.codigo = grupo + subgrupo + item;
    }

    public EstructuraArchivosPlanosPresupuesto(String periodo, String tipoPresupuesto, String grupo, String subgrupo, String item, String funcion, String orientacionGasto, Double valor, Double reforma, Double codificado, Double compromiso, Double devengado, Double recaudadoPagado, Double saldoPorComprometer, Double saldoPorDevengar) {
        this.periodo = periodo;
        this.tipoPresupuesto = tipoPresupuesto;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.item = item;
        this.funcion = "000";
        this.orientacionGasto = orientacionGasto;
        this.valor = valor;
        this.reforma = reforma;
        this.codificado = codificado;
        this.compromiso = compromiso;
        this.devengado = devengado;
        this.recaudadoPagado = recaudadoPagado;
        this.saldoPorComprometer = saldoPorComprometer;
        this.saldoPorDevengar = saldoPorDevengar;
        this.codigo = grupo + subgrupo + item;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getTipoPresupuesto() {
        return tipoPresupuesto;
    }

    public void setTipoPresupuesto(String tipoPresupuesto) {
        this.tipoPresupuesto = tipoPresupuesto;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public String getOrientacionGasto() {
        return orientacionGasto;
    }

    public void setOrientacionGasto(String orientacionGasto) {
        this.orientacionGasto = orientacionGasto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getReforma() {
        return reforma;
    }

    public void setReforma(Double reforma) {
        this.reforma = reforma;
    }

    public Double getCodificado() {
        return codificado;
    }

    public void setCodificado(Double codificado) {
        this.codificado = codificado;
    }

    public Double getCompromiso() {
        return compromiso;
    }

    public void setCompromiso(Double compromiso) {
        this.compromiso = compromiso;
    }

    public Double getDevengado() {
        return devengado;
    }

    public void setDevengado(Double devengado) {
        this.devengado = devengado;
    }

    public Double getRecaudadoPagado() {
        return recaudadoPagado;
    }

    public void setRecaudadoPagado(Double recaudadoPagado) {
        this.recaudadoPagado = recaudadoPagado;
    }

    public Double getSaldoPorComprometer() {
        return saldoPorComprometer;
    }

    public void setSaldoPorComprometer(Double saldoPorComprometer) {
        this.saldoPorComprometer = saldoPorComprometer;
    }

    public Double getSaldoPorDevengar() {
        return saldoPorDevengar;
    }

    public void setSaldoPorDevengar(Double saldoPorDevengar) {
        this.saldoPorDevengar = saldoPorDevengar;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
    
    
}
