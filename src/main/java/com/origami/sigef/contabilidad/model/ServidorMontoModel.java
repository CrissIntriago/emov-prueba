/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.math.BigDecimal;

/**
 *
 * @author Criss Intriago
 */
public class ServidorMontoModel {
    private Servidor servidor;
    private BigDecimal monto;

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
}
