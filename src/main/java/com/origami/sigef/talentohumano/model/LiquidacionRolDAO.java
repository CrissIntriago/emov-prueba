/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.model;

import com.origami.sigef.common.entities.LiquidacionRol;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.ValoresRoles;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author ORIGAMI2
 */
public class LiquidacionRolDAO implements Serializable{

    public static final long serialVersionUID = 1L;
    
    private LiquidacionRol liquidacionRol;
    private ParametrosTalentoHumano valorParametro;
    private BigDecimal valorRubro;
    private ValoresRoles valorAsignacion;

    public ValoresRoles getValorAsignacion() {
        return valorAsignacion;
    }

    public void setValorAsignacion(ValoresRoles valorAsignacion) {
        this.valorAsignacion = valorAsignacion;
    }

    public LiquidacionRol getLiquidacionRol() {
        return liquidacionRol;
    }

    public void setLiquidacionRol(LiquidacionRol liquidacionRol) {
        this.liquidacionRol = liquidacionRol;
    }

    public ParametrosTalentoHumano getValorParametro() {
        return valorParametro;
    }

    public void setValorParametro(ParametrosTalentoHumano valorParametro) {
        this.valorParametro = valorParametro;
    }

    public BigDecimal getValorRubro() {
        return valorRubro;
    }

    public void setValorRubro(BigDecimal valorRubro) {
        this.valorRubro = valorRubro;
    }

}
