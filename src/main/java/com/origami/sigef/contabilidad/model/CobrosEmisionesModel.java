/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.Presupuesto;
import java.math.BigDecimal;

/**
 *
 * @author Criss Intriago
 */
public class CobrosEmisionesModel {
    
    private CuentaContable cuentacontable;
    private BigDecimal montototal;
    private Presupuesto itempresupuestario;

    public CuentaContable getCuentacontable() {
        return cuentacontable;
    }

    public void setCuentacontable(CuentaContable cuentacontable) {
        this.cuentacontable = cuentacontable;
    }

    public BigDecimal getMontototal() {
        return montototal;
    }

    public void setMontototal(BigDecimal montototal) {
        this.montototal = montototal;
    }

    public Presupuesto getItempresupuestario() {
        return itempresupuestario;
    }

    public void setItempresupuestario(Presupuesto itempresupuestario) {
        this.itempresupuestario = itempresupuestario;
    }

}
