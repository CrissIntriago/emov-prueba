package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class DetalleRubro {

    private String descripcion;
    private BigDecimal valor;

    public DetalleRubro() {
    }

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
