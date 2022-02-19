package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class PagoLiquidacionModel implements Serializable {

    private Long idLiquidacion;
    private BigDecimal totalPago;
    private Long tipoPago;
    private Long entidadBancaria;
    private Long cajero;
    private String titularPago;
    private String observacion;
    private String numeroComprobante;
    private String estadoPago;
    private Integer estadoCodigo;

    public PagoLiquidacionModel() {
    }

    public Long getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(Long idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public Long getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Long tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Long getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(Long entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

    public Long getCajero() {
        return cajero;
    }

    public void setCajero(Long cajero) {
        this.cajero = cajero;
    }

    public String getTitularPago() {
        return titularPago;
    }

    public void setTitularPago(String titularPago) {
        this.titularPago = titularPago;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Integer getEstadoCodigo() {
        return estadoCodigo;
    }

    public void setEstadoCodigo(Integer estadoCodigo) {
        this.estadoCodigo = estadoCodigo;
    }

    @Override
    public String toString() {
        return "PagoLiquidacionModel{" + "idLiquidacion=" + idLiquidacion + ", totalPago=" + totalPago + ", tipoPago=" + tipoPago + ", entidadBancaria=" + entidadBancaria + ", cajero=" + cajero + ", titularPago=" + titularPago + ", observacion=" + observacion + '}';
    }

}
