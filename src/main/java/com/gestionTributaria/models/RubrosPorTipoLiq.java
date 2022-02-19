/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class RubrosPorTipoLiq implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String descripcion;
    private String tipo;
    private BigDecimal valor;
    private FinaRenRubrosLiquidacion rubro;

    public RubrosPorTipoLiq(FinaRenRubrosLiquidacion rubro, BigDecimal val) {
        this.descripcion = rubro.getDescripcion();
        this.rubro = rubro;
        this.valor = val;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

}
