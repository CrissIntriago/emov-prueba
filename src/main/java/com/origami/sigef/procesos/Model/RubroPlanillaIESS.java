/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.procesos.Model;

import java.math.BigDecimal;

/**
 *
 * @author OrigamiEC
 */
public class RubroPlanillaIESS{

    private BigDecimal valorTotal;
    private Long cuentaContable;

    public RubroPlanillaIESS(Long cuentaContable, BigDecimal valorTotal) {
        this.cuentaContable = cuentaContable;
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(Long cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

}
