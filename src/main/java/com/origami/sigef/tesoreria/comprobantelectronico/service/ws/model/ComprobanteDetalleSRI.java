package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import com.google.gson.annotations.Expose;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ComprobanteDetalleSRI {

    private String codigoPrincipal;
    private String codigoAuxiliar;
    private Integer cantidad;
    private String descripcion;
    private String detalleAdicional1;
    private String detalleAdicional2;
    private String detalleAdicional3;
    private BigDecimal precioUnitario;
    private BigDecimal subsidio;
    private BigDecimal precioSinSubsidio;
    private BigDecimal descuento = BigDecimal.ZERO;
    private BigDecimal precioTotal;
    private Short anio;
    private BigDecimal interes;
    private BigDecimal coactiva;
    private BigDecimal recargo;
    @Expose(deserialize = false, serialize = false)
    private List<ComprobantePagoSRI> pagoDetalle;

    public String getCodigoPrincipal() {
        return codigoPrincipal;
    }

    public void setCodigoPrincipal(String codigoPrincipal) {
        this.codigoPrincipal = codigoPrincipal;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalleAdicional1() {
        return detalleAdicional1;
    }

    public void setDetalleAdicional1(String detalleAdicional1) {
        this.detalleAdicional1 = detalleAdicional1;
    }

    public String getDetalleAdicional2() {
        return detalleAdicional2;
    }

    public void setDetalleAdicional2(String detalleAdicional2) {
        this.detalleAdicional2 = detalleAdicional2;
    }

    public String getDetalleAdicional3() {
        return detalleAdicional3;
    }

    public void setDetalleAdicional3(String detalleAdicional3) {
        this.detalleAdicional3 = detalleAdicional3;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario.setScale(2, RoundingMode.HALF_UP);
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubsidio() {
        return subsidio.setScale(2, RoundingMode.HALF_UP);
    }

    public void setSubsidio(BigDecimal subsidio) {
        this.subsidio = subsidio;
    }

    public BigDecimal getPrecioSinSubsidio() {
        return precioSinSubsidio.setScale(2, RoundingMode.HALF_UP);
    }

    public void setPrecioSinSubsidio(BigDecimal precioSinSubsidio) {
        this.precioSinSubsidio = precioSinSubsidio;
    }

    public BigDecimal getDescuento() {
        return descuento.setScale(2, RoundingMode.HALF_UP);
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public BigDecimal getInteres() {
        return interes.setScale(2, RoundingMode.HALF_UP);
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getCoactiva() {
        return coactiva.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCoactiva(BigDecimal coactiva) {
        this.coactiva = coactiva;
    }

    public BigDecimal getRecargo() {
        return recargo.setScale(2, RoundingMode.HALF_UP);
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public List<ComprobantePagoSRI> getPagoDetalle() {
        if (pagoDetalle == null) {
            pagoDetalle = new ArrayList<>();
        }
        return pagoDetalle;
    }

    public void setPagoDetalle(List<ComprobantePagoSRI> pagoDetalle) {
        this.pagoDetalle = pagoDetalle;
    }

}
