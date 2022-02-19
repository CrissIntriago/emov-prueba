/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.PlanProgramatico;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Criss Intriago
 */
public class CuentaContablePresupuestoModel implements Serializable{
    private int idTemporal;
    private CuentaContable cuentaContable;
    private CatalogoItem fuenteDirecta;
    private CatalogoPresupuesto catalogoPresupuesto;
    private PlanProgramatico planProgramatico;
    private CatalogoItem parcialTotal;
    private BigDecimal monto_1;
    private BigDecimal monto_2;
    private BigDecimal monto_3;
    private BigDecimal monto_4;
    private String partidaPresupuestaria;
    private String cuentaPartidaPresupuestaria;
    private Boolean devengadoTotal;
    private DetalleSolicitudCompromiso idDetalleReserva;
    
    public int getIdTemporal() {
        return idTemporal;
    }

    public void setIdTemporal(int idTemporal) {
        this.idTemporal = idTemporal;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public CatalogoItem getFuenteDirecta() {
        return fuenteDirecta;
    }

    public void setFuenteDirecta(CatalogoItem fuenteDirecta) {
        this.fuenteDirecta = fuenteDirecta;
    }

    public CatalogoPresupuesto getCatalogoPresupuesto() {
        return catalogoPresupuesto;
    }

    public void setCatalogoPresupuesto(CatalogoPresupuesto catalogoPresupuesto) {
        this.catalogoPresupuesto = catalogoPresupuesto;
    }

    public PlanProgramatico getPlanProgramatico() {
        return planProgramatico;
    }

    public void setPlanProgramatico(PlanProgramatico planProgramatico) {
        this.planProgramatico = planProgramatico;
    }

    public CatalogoItem getParcialTotal() {
        return parcialTotal;
    }

    public void setParcialTotal(CatalogoItem parcialTotal) {
        this.parcialTotal = parcialTotal;
    }

    public BigDecimal getMonto_1() {
        return monto_1;
    }

    public void setMonto_1(BigDecimal monto_1) {
        this.monto_1 = monto_1;
    }

    public BigDecimal getMonto_2() {
        return monto_2;
    }

    public void setMonto_2(BigDecimal monto_2) {
        this.monto_2 = monto_2;
    }

    public BigDecimal getMonto_3() {
        return monto_3;
    }

    public void setMonto_3(BigDecimal monto_3) {
        this.monto_3 = monto_3;
    }

    public BigDecimal getMonto_4() {
        return monto_4;
    }

    public void setMonto_4(BigDecimal monto_4) {
        this.monto_4 = monto_4;
    }

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public String getCuentaPartidaPresupuestaria() {
        return cuentaPartidaPresupuestaria;
    }

    public void setCuentaPartidaPresupuestaria(String cuentaPartidaPresupuestaria) {
        this.cuentaPartidaPresupuestaria = cuentaPartidaPresupuestaria;
    }

    public Boolean getDevengadoTotal() {
        return devengadoTotal;
    }

    public void setDevengadoTotal(Boolean devengadoTotal) {
        this.devengadoTotal = devengadoTotal;
    }

    public DetalleSolicitudCompromiso getIdDetalleReserva() {
        return idDetalleReserva;
    }

    public void setIdDetalleReserva(DetalleSolicitudCompromiso idDetalleReserva) {
        this.idDetalleReserva = idDetalleReserva;
    }

}
