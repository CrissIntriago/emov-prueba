package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.math.BigDecimal;

public class ComprobantePagoSRI {

    private String descripcion;
    private BigDecimal valor;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
