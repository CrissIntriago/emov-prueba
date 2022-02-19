/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial;

/**
 *
 * @author jesus
 */
public class EstructuraArchivosPlanos {

    private String periodo;
    private String cuentaMayor;
    private String cuentaNivel1;
    private String cuentaNivel2;
    private Double saldoInicialDeudor;
    private Double saldoInicialAcreedor;
    private String rucReceptor;
    private String rucOtorgante;
    private Double flujoDeudor;
    private Double flujoAcreedor;
    private Integer cuentaMonetaria;
    private Double sumasAcumuladoDeudor;
    private Double sumasAculadoAcreedor;
    private Double saldoFinalDeudor;
    private Double saldoFinalAcreedor;

    public EstructuraArchivosPlanos() {
    }

    public EstructuraArchivosPlanos(String periodo, String cuentaMayor, String cuentaNivel1, String cuentaNivel2,
            Double saldoInicialDeudor, Double saldoInicialAcreedor) {
        this.periodo = periodo;
        this.cuentaMayor = cuentaMayor;
        this.cuentaNivel1 = cuentaNivel1;
        this.cuentaNivel2 = cuentaNivel2;
        this.saldoInicialDeudor = saldoInicialDeudor;
        this.saldoInicialAcreedor = saldoInicialAcreedor;
    }

//    public EstructuraArchivosPlanos(String periodo, String cuentaMayor, String cuentaNivel1, String cuentaNivel2,
//            ContSaldoInicial saldoInicial) {
//        this.periodo = periodo;
//        this.cuentaMayor = cuentaMayor;
//        this.cuentaNivel1 = cuentaNivel1;
//        this.cuentaNivel2 = cuentaNivel2;
//        if (saldoInicial != null) {
//            saldoInicialDeudor = saldoInicial.getSaldoDebe() != null ? saldoInicial.getSaldoDebe().doubleValue() : 0d;
//            saldoInicialAcreedor = saldoInicial.getSaldoHaber() != null ? saldoInicial.getSaldoHaber().doubleValue() : 0d;
//        } else {
//            this.saldoInicialDeudor = 0d;
//            this.saldoInicialAcreedor = 0d;
//        }
//
//    }

    public EstructuraArchivosPlanos(String periodo, String cuentaMayor, String cuentaNivel1, String cuentaNivel2, String rucReceptor,
            String rucOtorgante, Double flujoDeudor, Double flujoAcreedor, Integer cuentaMonetaria) {
        this.periodo = periodo;
        this.cuentaMayor = cuentaMayor;
        this.cuentaNivel1 = cuentaNivel1;
        this.cuentaNivel2 = cuentaNivel2;
        this.rucReceptor = rucReceptor;
        this.rucOtorgante = rucOtorgante;
        this.flujoDeudor = flujoDeudor;
        this.flujoAcreedor = flujoAcreedor;
        this.cuentaMonetaria = cuentaMonetaria;
    }

    public EstructuraArchivosPlanos(String periodo, String cuentaMayor, String cuentaNivel1, String cuentaNivel2,
            Double saldoInicialDeudor, Double saldoInicialAcreedor, Double flujoDeudor, Double flujoAcreedor, Double sumasAcumuladoDeudor,
            Double sumasAculadoAcreedor, Double saldoFinalDeudor, Double saldoFinalAcreedor) {
        this.periodo = periodo;
        this.cuentaMayor = cuentaMayor;
        this.cuentaNivel1 = cuentaNivel1;
        this.cuentaNivel2 = cuentaNivel2;
        this.saldoInicialDeudor = saldoInicialDeudor;
        this.saldoInicialAcreedor = saldoInicialAcreedor;
        this.flujoDeudor = flujoDeudor;
        this.flujoAcreedor = flujoAcreedor;
        this.sumasAcumuladoDeudor = sumasAcumuladoDeudor;
        this.sumasAculadoAcreedor = sumasAculadoAcreedor;
        this.saldoFinalDeudor = saldoFinalDeudor;
        this.saldoFinalAcreedor = saldoFinalAcreedor;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getCuentaMayor() {
        return cuentaMayor;
    }

    public void setCuentaMayor(String cuentaMayor) {
        this.cuentaMayor = cuentaMayor;
    }

    public String getCuentaNivel1() {
        return cuentaNivel1;
    }

    public void setCuentaNivel1(String cuentaNivel1) {
        this.cuentaNivel1 = cuentaNivel1;
    }

    public String getCuentaNivel2() {
        return cuentaNivel2;
    }

    public void setCuentaNivel2(String cuentaNivel2) {
        this.cuentaNivel2 = cuentaNivel2;
    }

    public Double getSaldoInicialDeudor() {
        return saldoInicialDeudor;
    }

    public void setSaldoInicialDeudor(Double saldoInicialDeudor) {
        this.saldoInicialDeudor = saldoInicialDeudor;
    }

    public Double getSaldoInicialAcreedor() {
        return saldoInicialAcreedor;
    }

    public void setSaldoInicialAcreedor(Double saldoInicialAcreedor) {
        this.saldoInicialAcreedor = saldoInicialAcreedor;
    }

    public String getRucReceptor() {
        return rucReceptor;
    }

    public void setRucReceptor(String rucReceptor) {
        this.rucReceptor = rucReceptor;
    }

    public String getRucOtorgante() {
        return rucOtorgante;
    }

    public void setRucOtorgante(String rucOtorgante) {
        this.rucOtorgante = rucOtorgante;
    }

    public Double getFlujoDeudor() {
        return flujoDeudor;
    }

    public void setFlujoDeudor(Double flujoDeudor) {
        this.flujoDeudor = flujoDeudor;
    }

    public Double getFlujoAcreedor() {
        return flujoAcreedor;
    }

    public void setFlujoAcreedor(Double flujoAcreedor) {
        this.flujoAcreedor = flujoAcreedor;
    }

    public Integer getCuentaMonetaria() {
        return cuentaMonetaria;
    }

    public void setCuentaMonetaria(Integer cuentaMonetaria) {
        this.cuentaMonetaria = cuentaMonetaria;
    }

    public Double getSumasAcumuladoDeudor() {
        return sumasAcumuladoDeudor;
    }

    public void setSumasAcumuladoDeudor(Double sumasAcumuladoDeudor) {
        this.sumasAcumuladoDeudor = sumasAcumuladoDeudor;
    }

    public Double getSumasAculadoAcreedor() {
        return sumasAculadoAcreedor;
    }

    public void setSumasAculadoAcreedor(Double sumasAculadoAcreedor) {
        this.sumasAculadoAcreedor = sumasAculadoAcreedor;
    }

    public Double getSaldoFinalDeudor() {
        return saldoFinalDeudor;
    }

    public void setSaldoFinalDeudor(Double saldoFinalDeudor) {
        this.saldoFinalDeudor = saldoFinalDeudor;
    }

    public Double getSaldoFinalAcreedor() {
        return saldoFinalAcreedor;
    }

    public void setSaldoFinalAcreedor(Double saldoFinalAcreedor) {
        this.saldoFinalAcreedor = saldoFinalAcreedor;
    }

}
