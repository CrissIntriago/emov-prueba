package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.util.List;

public class Detalles {

    private List<Detalle> detalle;

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "ClassPojo [detalle = " + detalle + "]";
    }
}
