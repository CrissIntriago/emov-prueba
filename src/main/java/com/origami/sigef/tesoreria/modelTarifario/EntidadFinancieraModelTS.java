/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.modelTarifario;

import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.CuentaContable;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Transient;

/**
 *
 * @author OrigamiEC
 */
public class EntidadFinancieraModelTS implements Serializable {

    @Transient
    private String banco;
    @Transient
    private Long identidadfinanciera;
    private Banco entidadBancaria;
    private BigDecimal valorTotal;
    private CuentaContable cuenta;

    public EntidadFinancieraModelTS(String banco, Long identidadfinanciera, BigDecimal valorTotal, CuentaContable cuenta) {
        this.banco = banco;
        this.identidadfinanciera = identidadfinanciera;
        this.valorTotal = valorTotal;
        this.cuenta = cuenta;
    }
 
    public EntidadFinancieraModelTS() {
        this.valorTotal = BigDecimal.ZERO;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public CuentaContable getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaContable cuenta) {
        this.cuenta = cuenta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Long getIdentidadfinanciera() {
        return identidadfinanciera;
    }

    public void setIdentidadfinanciera(Long identidadfinanciera) {
        this.identidadfinanciera = identidadfinanciera;
    }

    public Banco getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(Banco entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

}
