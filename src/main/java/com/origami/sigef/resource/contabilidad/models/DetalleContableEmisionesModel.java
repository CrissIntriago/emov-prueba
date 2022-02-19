/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Criss Intriago
 */
public class DetalleContableEmisionesModel implements Serializable {

    private Long id_cont_cuentas;
    private BigDecimal debe;
    private BigDecimal haber;
    private Long rubro_liquidacion;
    private Long tipo_registro;
    private Long id_pres_catalogo_presupuestario;
    private Long id_pres_plan_programatico;
    private Long id_pres_fuente_financiamiento;
    private String partida_presupuestaria;
    private BigDecimal devengado;
    private BigDecimal ejecutado;
    private Long numeracion;
    private String descripcion;
    private String cod_tipo;
    private String cod_cuenta;

    public DetalleContableEmisionesModel() {

    }

    public DetalleContableEmisionesModel(Long id_cont_cuentas, BigDecimal debe, BigDecimal haber, 
            Long rubro_liquidacion, Long tipo_registro, 
            Long id_pres_catalogo_presupuestario, Long id_pres_plan_programatico, Long id_pres_fuente_financiamiento, 
            String partida_presupuestaria, 
            BigDecimal devengado, BigDecimal ejecutado, Long numeracion, String descripcion, String cod_tipo, String cod_cuenta) {
        this.id_cont_cuentas = id_cont_cuentas;
        this.debe = debe;
        this.haber = haber;
        this.rubro_liquidacion = rubro_liquidacion;
        this.tipo_registro = tipo_registro;
        this.id_pres_catalogo_presupuestario = id_pres_catalogo_presupuestario;
        this.id_pres_plan_programatico = id_pres_plan_programatico;
        this.id_pres_fuente_financiamiento = id_pres_fuente_financiamiento;
        this.partida_presupuestaria = partida_presupuestaria;
        this.devengado = devengado;
        this.ejecutado = ejecutado;
        this.numeracion = numeracion;
        this.descripcion = descripcion;
        this.cod_tipo = cod_tipo;
        this.cod_cuenta = cod_cuenta;
    }

    public Long getId_cont_cuentas() {
        return id_cont_cuentas;
    }

    public void setId_cont_cuentas(Long id_cont_cuentas) {
        this.id_cont_cuentas = id_cont_cuentas;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public Long getRubro_liquidacion() {
        return rubro_liquidacion;
    }

    public void setRubro_liquidacion(Long rubro_liquidacion) {
        this.rubro_liquidacion = rubro_liquidacion;
    }

    public Long getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(Long tipo_registro) {
        this.tipo_registro = tipo_registro;
    }

    public Long getId_pres_catalogo_presupuestario() {
        return id_pres_catalogo_presupuestario;
    }

    public void setId_pres_catalogo_presupuestario(Long id_pres_catalogo_presupuestario) {
        this.id_pres_catalogo_presupuestario = id_pres_catalogo_presupuestario;
    }

    public Long getId_pres_plan_programatico() {
        return id_pres_plan_programatico;
    }

    public void setId_pres_plan_programatico(Long id_pres_plan_programatico) {
        this.id_pres_plan_programatico = id_pres_plan_programatico;
    }

    public Long getId_pres_fuente_financiamiento() {
        return id_pres_fuente_financiamiento;
    }

    public void setId_pres_fuente_financiamiento(Long id_pres_fuente_financiamiento) {
        this.id_pres_fuente_financiamiento = id_pres_fuente_financiamiento;
    }

    public String getPartida_presupuestaria() {
        return partida_presupuestaria;
    }

    public void setPartida_presupuestaria(String partida_presupuestaria) {
        this.partida_presupuestaria = partida_presupuestaria;
    }

    public BigDecimal getDevengado() {
        return devengado;
    }

    public void setDevengado(BigDecimal devengado) {
        this.devengado = devengado;
    }

    public BigDecimal getEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(BigDecimal ejecutado) {
        this.ejecutado = ejecutado;
    }

    public Long getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Long numeracion) {
        this.numeracion = numeracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCod_tipo() {
        return cod_tipo;
    }

    public void setCod_tipo(String cod_tipo) {
        this.cod_tipo = cod_tipo;
    }

    public String getCod_cuenta() {
        return cod_cuenta;
    }

    public void setCod_cuenta(String cod_cuenta) {
        this.cod_cuenta = cod_cuenta;
    }

}
