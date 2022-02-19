/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recap", propOrder = {"detalleRecap"})
@XmlRootElement
public class Recap {

    @XmlElement(name = "detalleRecap")
    private List<DetalleRecap> detalleRecap;

    public Recap() {
    }

    public Recap(List<DetalleRecap> detalleRecap) {
        this.detalleRecap = detalleRecap;
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<DetalleRecap> getDetalleRecap() {
        return detalleRecap;
    }

    public void setDetalleRecap(List<DetalleRecap> detalleRecap) {
        this.detalleRecap = detalleRecap;
    }
//</editor-fold>

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "detalleRecap", propOrder = {"establecimientoRecap", "identificacionRecap", "parteRelRec", "tipoComprobante",
        "numeroRecap", "fechaPago", "tarjetaCredito", "fechaEmisionRecap", "consumoCero", "consumoGravado", "totalConsumo",
        "montoIva", "compensaciones", "comision", "numeroVouchers", "valRetBien10", "valRetServ20", "valorRetBienes",
        "valRetServ50", "valorRetServicios", "valRetServ100", "pagoExterior", "air", "establecimiento", "puntoEmision",
        "secuencial", "autorizacion", "fechaEmision"})
    @XmlRootElement
    public class DetalleRecap {

        private String establecimientoRecap;
        private String identificacionRecap;
        private String parteRelRec;
        private String tipoComprobante;
        private String numeroRecap;
        private String fechaPago;
        private String tarjetaCredito;
        private String fechaEmisionRecap;
        private Double consumoCero;
        private Double consumoGravado;
        private Double totalConsumo;
        private Double montoIva;
        @XmlElement(name = "compensaciones")
        private Compensaciones compensaciones;
        private Double comision;
        private Integer numeroVouchers;
        private Double valRetBien10;
        private Double valRetServ20;
        private Double valorRetBienes;
        private Double valRetServ50;
        private Double valorRetServicios;
        private Double valRetServ100;
        @XmlElement(name = "pagoExterior")
        private PagoExteriorModel pagoExterior;
        @XmlElement(name = "air")
        private Air air;
        private String establecimiento;
        private String puntoEmision;
        private Integer secuencial;
        private Integer autorizacion;
        private String fechaEmision;

        public DetalleRecap() {
        }

        public DetalleRecap(String establecimientoRecap, String identificacionRecap, String parteRelRec,
                String tipoComprobante, String numeroRecap, String fechaPago, String tarjetaCredito,
                String fechaEmisionRecap, Double consumoCero, Double consumoGravado, Double totalConsumo,
                Double montoIva, Compensaciones compensaciones, Double comision, Integer numeroVouchers,
                Double valRetBien10, Double valRetServ20, Double valorRetBienes, Double valRetServ50, Double valorRetServicios,
                Double valRetServ100, PagoExteriorModel pagoExterior, Air air, String establecimiento, String puntoEmision,
                Integer secuencial, Integer autorizacion, String fechaEmision) {
            this.establecimientoRecap = establecimientoRecap;
            this.identificacionRecap = identificacionRecap;
            this.parteRelRec = parteRelRec;
            this.tipoComprobante = tipoComprobante;
            this.numeroRecap = numeroRecap;
            this.fechaPago = fechaPago;
            this.tarjetaCredito = tarjetaCredito;
            this.fechaEmisionRecap = fechaEmisionRecap;
            this.consumoCero = consumoCero;
            this.consumoGravado = consumoGravado;
            this.totalConsumo = totalConsumo;
            this.montoIva = montoIva;
            this.compensaciones = compensaciones;
            this.comision = comision;
            this.numeroVouchers = numeroVouchers;
            this.valRetBien10 = valRetBien10;
            this.valRetServ20 = valRetServ20;
            this.valorRetBienes = valorRetBienes;
            this.valRetServ50 = valRetServ50;
            this.valorRetServicios = valorRetServicios;
            this.valRetServ100 = valRetServ100;
            this.pagoExterior = pagoExterior;
            this.air = air;
            this.establecimiento = establecimiento;
            this.puntoEmision = puntoEmision;
            this.secuencial = secuencial;
            this.autorizacion = autorizacion;
            this.fechaEmision = fechaEmision;
        }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
        public String getEstablecimientoRecap() {
            return establecimientoRecap;
        }

        public void setEstablecimientoRecap(String establecimientoRecap) {
            this.establecimientoRecap = establecimientoRecap;
        }

        public String getIdentificacionRecap() {
            return identificacionRecap;
        }

        public void setIdentificacionRecap(String identificacionRecap) {
            this.identificacionRecap = identificacionRecap;
        }

        public String getParteRelRec() {
            return parteRelRec;
        }

        public void setParteRelRec(String parteRelRec) {
            this.parteRelRec = parteRelRec;
        }

        public String getTipoComprobante() {
            return tipoComprobante;
        }

        public void setTipoComprobante(String tipoComprobante) {
            this.tipoComprobante = tipoComprobante;
        }

        public String getNumeroRecap() {
            return numeroRecap;
        }

        public void setNumeroRecap(String numeroRecap) {
            this.numeroRecap = numeroRecap;
        }

        public String getFechaPago() {
            return fechaPago;
        }

        public void setFechaPago(String fechaPago) {
            this.fechaPago = fechaPago;
        }

        public String getTarjetaCredito() {
            return tarjetaCredito;
        }

        public void setTarjetaCredito(String tarjetaCredito) {
            this.tarjetaCredito = tarjetaCredito;
        }

        public String getFechaEmisionRecap() {
            return fechaEmisionRecap;
        }

        public void setFechaEmisionRecap(String fechaEmisionRecap) {
            this.fechaEmisionRecap = fechaEmisionRecap;
        }

        public Double getConsumoCero() {
            return consumoCero;
        }

        public void setConsumoCero(Double consumoCero) {
            this.consumoCero = consumoCero;
        }

        public Double getConsumoGravado() {
            return consumoGravado;
        }

        public void setConsumoGravado(Double consumoGravado) {
            this.consumoGravado = consumoGravado;
        }

        public Double getTotalConsumo() {
            return totalConsumo;
        }

        public void setTotalConsumo(Double totalConsumo) {
            this.totalConsumo = totalConsumo;
        }

        public Double getMontoIva() {
            return montoIva;
        }

        public void setMontoIva(Double montoIva) {
            this.montoIva = montoIva;
        }

        public Compensaciones getCompensaciones() {
            return compensaciones;
        }

        public void setCompensaciones(Compensaciones compensaciones) {
            this.compensaciones = compensaciones;
        }

        public Double getComision() {
            return comision;
        }

        public void setComision(Double comision) {
            this.comision = comision;
        }

        public Integer getNumeroVouchers() {
            return numeroVouchers;
        }

        public void setNumeroVouchers(Integer numeroVouchers) {
            this.numeroVouchers = numeroVouchers;
        }

        public Double getValRetBien10() {
            return valRetBien10;
        }

        public void setValRetBien10(Double valRetBien10) {
            this.valRetBien10 = valRetBien10;
        }

        public Double getValRetServ20() {
            return valRetServ20;
        }

        public void setValRetServ20(Double valRetServ20) {
            this.valRetServ20 = valRetServ20;
        }

        public Double getValorRetBienes() {
            return valorRetBienes;
        }

        public void setValorRetBienes(Double valorRetBienes) {
            this.valorRetBienes = valorRetBienes;
        }

        public Double getValRetServ50() {
            return valRetServ50;
        }

        public void setValRetServ50(Double valRetServ50) {
            this.valRetServ50 = valRetServ50;
        }

        public Double getValorRetServicios() {
            return valorRetServicios;
        }

        public void setValorRetServicios(Double valorRetServicios) {
            this.valorRetServicios = valorRetServicios;
        }

        public Double getValRetServ100() {
            return valRetServ100;
        }

        public void setValRetServ100(Double valRetServ100) {
            this.valRetServ100 = valRetServ100;
        }

        public PagoExteriorModel getPagoExterior() {
            return pagoExterior;
        }

        public void setPagoExterior(PagoExteriorModel pagoExterior) {
            this.pagoExterior = pagoExterior;
        }

        public Air getAir() {
            return air;
        }

        public void setAir(Air air) {
            this.air = air;
        }

        public String getEstablecimiento() {
            return establecimiento;
        }

        public void setEstablecimiento(String establecimiento) {
            this.establecimiento = establecimiento;
        }

        public String getPuntoEmision() {
            return puntoEmision;
        }

        public void setPuntoEmision(String puntoEmision) {
            this.puntoEmision = puntoEmision;
        }

        public Integer getSecuencial() {
            return secuencial;
        }

        public void setSecuencial(Integer secuencial) {
            this.secuencial = secuencial;
        }

        public Integer getAutorizacion() {
            return autorizacion;
        }

        public void setAutorizacion(Integer autorizacion) {
            this.autorizacion = autorizacion;
        }

        public String getFechaEmision() {
            return fechaEmision;
        }

        public void setFechaEmision(String fechaEmision) {
            this.fechaEmision = fechaEmision;
        }
//</editor-fold>
    }
}
