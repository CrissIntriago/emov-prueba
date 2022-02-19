/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import com.origami.sigef.common.entities.ActividadOperativa;
import java.math.BigDecimal;

/**
 *
 * @author ORIGAMI-EC
 */
public class ReporteDeActividades {

    private ActividadOperativa actividadOperativa;
    private BigDecimal montoTotalDeProductos;
    private BigDecimal diferencia;

    public ReporteDeActividades() {

    }

    public ReporteDeActividades(ActividadOperativa actividadOperativa, BigDecimal montoTotalDeProductos, BigDecimal diferencia) {
        this.actividadOperativa = actividadOperativa;
        this.montoTotalDeProductos = montoTotalDeProductos;
        this.diferencia = diferencia;
    }

    public ActividadOperativa getActividadOperativa() {
        return actividadOperativa;
    }

    public void setActividadOperativa(ActividadOperativa actividadOperativa) {
        this.actividadOperativa = actividadOperativa;
    }

    public BigDecimal getMontoTotalDeProductos() {
        return montoTotalDeProductos;
    }

    public void setMontoTotalDeProductos(BigDecimal montoTotalDeProductos) {
        this.montoTotalDeProductos = montoTotalDeProductos;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }

}
