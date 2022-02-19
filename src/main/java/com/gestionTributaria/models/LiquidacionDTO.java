/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class LiquidacionDTO implements Serializable {

    private Long id;
    private String ipDireccion;
    private Long idCliente;
    private Long idTipoLiquidacion;
    private String macAdress;
    private String idLiquidacion;
    private Long idPredio;
    // se requieren los avaluos en caso de ser necesario por los años cambian los avaluos
//    private BigDecimal avaluoMunicipal;
//    private BigDecimal avaluoconstruccion;
//    private BigDecimal avaluoSolar;
    private Integer anio;
    private Long idlocal;
    private String descripcionExoneracion;
    private Long tipoExoneracion;
    private String liquidadorReposanble;
    private String liquidadorAprobador;
//    private Date fechaEmision;
//    private Date fechaAprobador;
    private BigDecimal totalPago;
    private List<DetaLiquidacionDTO> detalle;

    public LiquidacionDTO() {

    }

    public String getIpDireccion() {
        return ipDireccion;
    }

    public void setIpDireccion(String ipDireccion) {
        this.ipDireccion = ipDireccion;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdTipoLiquidacion() {
        return idTipoLiquidacion;
    }

    public void setIdTipoLiquidacion(Long idTipoLiquidacion) {
        this.idTipoLiquidacion = idTipoLiquidacion;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public Long getIdPredio() {
        return idPredio;
    }

    public void setIdPredio(Long idPredio) {
        this.idPredio = idPredio;
    }
//
//    public BigDecimal getAvaluoMunicipal() {
//        return avaluoMunicipal;
//    }
//
//    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
//        this.avaluoMunicipal = avaluoMunicipal;
//    }
//
//    public BigDecimal getAvaluoconstruccion() {
//        return avaluoconstruccion;
//    }
//
//    public void setAvaluoconstruccion(BigDecimal avaluoconstruccion) {
//        this.avaluoconstruccion = avaluoconstruccion;
//    }
//
//    public BigDecimal getAvaluoSolar() {
//        return avaluoSolar;
//    }
//
//    public void setAvaluoSolar(BigDecimal avaluoSolar) {
//        this.avaluoSolar = avaluoSolar;
//    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getIdlocal() {
        return idlocal;
    }

    public void setIdlocal(Long idlocal) {
        this.idlocal = idlocal;
    }

    public String getDescripcionExoneracion() {
        return descripcionExoneracion;
    }

    public void setDescripcionExoneracion(String descripcionExoneracion) {
        this.descripcionExoneracion = descripcionExoneracion;
    }

    public Long getTipoExoneracion() {
        return tipoExoneracion;
    }

    public void setTipoExoneracion(Long tipoExoneracion) {
        this.tipoExoneracion = tipoExoneracion;
    }

    public List<DetaLiquidacionDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetaLiquidacionDTO> detalle) {
        this.detalle = detalle;
    }

    public String getLiquidadorReposanble() {
        return liquidadorReposanble;
    }

    public void setLiquidadorReposanble(String liquidadorReposanble) {
        this.liquidadorReposanble = liquidadorReposanble;
    }

    public String getLiquidadorAprobador() {
        return liquidadorAprobador;
    }

    public void setLiquidadorAprobador(String liquidadorAprobador) {
        this.liquidadorAprobador = liquidadorAprobador;
    }

//    public Date getFechaEmision() {
//        return fechaEmision;
//    }
//
//    public void setFechaEmision(Date fechaEmision) {
//        this.fechaEmision = fechaEmision;
//    }
//
//    public Date getFechaAprobador() {
//        return fechaAprobador;
//    }
//
//    public void setFechaAprobador(Date fechaAprobador) {
//        this.fechaAprobador = fechaAprobador;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(String idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    @Override
    public String toString() {
        return "LiquidacionDTO{" + "id=" + id + ", ipDireccion=" + ipDireccion + ", idCliente=" + idCliente + ", idTipoLiquidacion=" + idTipoLiquidacion + ", macAdress=" + macAdress + ", idLiquidacion=" + idLiquidacion + ", idPredio=" + idPredio + ", anio=" + anio + ", idlocal=" + idlocal + ", descripcionExoneracion=" + descripcionExoneracion + ", tipoExoneracion=" + tipoExoneracion + ", liquidadorReposanble=" + liquidadorReposanble + ", liquidadorAprobador=" + liquidadorAprobador + ", totalPago=" + totalPago + ", detalle=" + detalle + '}';
    }

}
