/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.model;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DescuentoRubroValor;
import com.origami.sigef.common.entities.OtroDescuento;
import com.origami.sigef.common.entities.ValoresRoles;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author ORIGAMI2
 */
public class ValoresRubroDAO implements Serializable{

    private ValoresRoles valoresRoles;
    private OtroDescuento otroDescuento;
    private DescuentoRubroValor valoresRubro;
    private BigDecimal valor;
    private Cliente beneficiario;

    public Cliente getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Cliente beneficiario) {
        this.beneficiario = beneficiario;
    }

    public OtroDescuento getOtroDescuento() {
        return otroDescuento;
    }

    public void setOtroDescuento(OtroDescuento otroDescuento) {
        this.otroDescuento = otroDescuento;
    }

    public ValoresRoles getValoresRoles() {
        return valoresRoles;
    }

    public void setValoresRoles(ValoresRoles valoresRoles) {
        this.valoresRoles = valoresRoles;
    }

    public DescuentoRubroValor getValoresRubro() {
        return valoresRubro;
    }

    public void setValoresRubro(DescuentoRubroValor valoresRubro) {
        this.valoresRubro = valoresRubro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
