package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class PagoArriendo implements Serializable {

    private Boolean pagado;
    private String nombreBanco;
    private String fechaPago; //
    private String numComprobante;
    private String fechaComprobante;
    private String idSolicitud;
    private BigDecimal valorPagado;
    private BigDecimal saldo;

    public PagoArriendo() {
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(String numComprobante) {
        this.numComprobante = numComprobante;
    }

    public String getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(String fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.idSolicitud);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PagoArriendo other = (PagoArriendo) obj;
        if (!Objects.equals(this.idSolicitud, other.idSolicitud)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PagoArriendo{" + "pagado=" + pagado + ", nombreBanco=" + nombreBanco + ", fechaPago=" + fechaPago
                + ", numComprobante=" + numComprobante + ", fechaComprobante=" + fechaComprobante
                + ", idSolicitud=" + idSolicitud + ", valorPagado=" + valorPagado + ", saldo=" + saldo + '}';
    }

}
