/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.math.BigDecimal;

/**
 *
 * @author Criss Intriago
 */
public class SaldoDebeHaberDTO {

    private BigDecimal saldoDebe;
    private BigDecimal saldoHaber;

    public SaldoDebeHaberDTO() {
        saldoDebe = BigDecimal.ZERO;
        saldoHaber = BigDecimal.ZERO;
    }

    public SaldoDebeHaberDTO(BigDecimal saldoDebe, BigDecimal saldoHaber) {
        this.saldoDebe = saldoDebe;
        this.saldoHaber = saldoHaber;
    }

    public BigDecimal getSaldoDebe() {
        return saldoDebe;
    }

    public void setSaldoDebe(BigDecimal saldoDebe) {
        this.saldoDebe = saldoDebe;
    }

    public BigDecimal getSaldoHaber() {
        return saldoHaber;
    }

    public void setSaldoHaber(BigDecimal saldoHaber) {
        this.saldoHaber = saldoHaber;
    }

    @Override
    public String toString() {
        return "SaldoDebeHaberDTO{" + "saldoDebe=" + saldoDebe + ", saldoHaber=" + saldoHaber + '}';
    }

}
