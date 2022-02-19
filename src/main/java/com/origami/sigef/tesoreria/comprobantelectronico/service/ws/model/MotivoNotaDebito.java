package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.math.BigDecimal;

public class MotivoNotaDebito {

    private String razon;
    private BigDecimal valor;

    public MotivoNotaDebito(String razon, BigDecimal valor) {
        this.razon = razon;
        this.valor = valor;
    }

    public String getRazon() {
        return this.razon;
    }

    public void setRazon(String value) {
        this.razon = value;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public void setValor(BigDecimal value) {
        this.valor = value;
    }

}
