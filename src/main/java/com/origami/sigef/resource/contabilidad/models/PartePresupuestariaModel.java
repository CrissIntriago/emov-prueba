/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Criss Intriago
 */
public class PartePresupuestariaModel implements Serializable {

    private Long idtemp;
    private BigDecimal saldodisponible;
    private String partidapresupuestaria;
    private String descripcion;
    private Long idprescatalogopresupuestario;
    private Long idpresplanprogramatico;
    private Long idpresfuentefinanciamiento;
    private BigDecimal montoComprometido;
    private BigDecimal montodisponible;
    private Boolean devengadoTotal;
    private BigDecimal montoDevengado;
    private Long idreserva;
    //adicionales para parte de tesoreria
    private Date fecha_emision;//puede ser la fecha de emisión o de 
    private Integer anio;
    private Long rubro;
    private Long tipo_liquidacion;
    private Long ctaCatera;
    private Long partidaCartera;
    private Long estadoLiquidacion;
    private Boolean municipal;
    private Boolean porcentajeDiv;
    private Boolean tipopresupuesto;

    public PartePresupuestariaModel() {

    }

    public PartePresupuestariaModel(Long idtemp, String partidapresupuestaria, String descripcion, Long idprescatalogopresupuestario,
            Long idpresplanprogramatico, Long idpresfuentefinanciamiento, BigDecimal montoComprometido,
            BigDecimal montodisponible, Boolean devengadoTotal, BigDecimal montoDevengado, Long idreserva) {
        this.idtemp = idtemp;
        this.partidapresupuestaria = partidapresupuestaria;
        this.descripcion = descripcion;
        this.idprescatalogopresupuestario = idprescatalogopresupuestario;
        this.idpresplanprogramatico = idpresplanprogramatico;
        this.idpresfuentefinanciamiento = idpresfuentefinanciamiento;
        this.montoComprometido = montoComprometido;
        this.montodisponible = montodisponible;
        this.devengadoTotal = devengadoTotal;
        this.montoDevengado = montoDevengado;
        this.idreserva = idreserva;
    }

    public PartePresupuestariaModel(Long idtemp, BigDecimal saldodisponible, String partidapresupuestaria, String descripcion,
            Long idprescatalogopresupuestario, Long idpresplanprogramatico, Long idpresfuentefinanciamiento, Boolean tipopresupuesto) {
        this.idtemp = idtemp;
        this.saldodisponible = saldodisponible;
        this.partidapresupuestaria = partidapresupuestaria;
        this.descripcion = descripcion;
        this.idprescatalogopresupuestario = idprescatalogopresupuestario;
        this.idpresplanprogramatico = idpresplanprogramatico;
        this.idpresfuentefinanciamiento = idpresfuentefinanciamiento;
        this.tipopresupuesto = tipopresupuesto;
    }

    public PartePresupuestariaModel(Long idtemp, String partidapresupuestaria, String descripcion, Long idprescatalogopresupuestario, Long idpresplanprogramatico,
            Long idpresfuentefinanciamiento, BigDecimal montoComprometido, BigDecimal montodisponible, Long idreserva) {
        this.idtemp = idtemp;
        this.partidapresupuestaria = partidapresupuestaria;
        this.descripcion = descripcion;
        this.idprescatalogopresupuestario = idprescatalogopresupuestario;
        this.idpresplanprogramatico = idpresplanprogramatico;
        this.idpresfuentefinanciamiento = idpresfuentefinanciamiento;
        this.montoComprometido = montoComprometido;
        this.montodisponible = montodisponible;
        this.idreserva = idreserva;
        this.devengadoTotal = Boolean.FALSE;
        this.montoDevengado = BigDecimal.ZERO;
    }

    public PartePresupuestariaModel(Long idtemp, BigDecimal saldodisponible) {
        this.idtemp = idtemp;
        this.saldodisponible = saldodisponible;
    }

    public PartePresupuestariaModel(Long idtemp, BigDecimal saldodisponible, BigDecimal montodisponible) {
        this.idtemp = idtemp;
        this.saldodisponible = saldodisponible;
        this.montodisponible = montodisponible;
    }

    public PartePresupuestariaModel(Long idprescatalogopresupuestario, Long idpresplanprogramatico, Long idpresfuentefinanciamiento, String partidapresupuestaria, BigDecimal saldodisponible) {
        this.partidapresupuestaria = partidapresupuestaria;
        this.idprescatalogopresupuestario = idprescatalogopresupuestario;
        this.idpresplanprogramatico = idpresplanprogramatico;
        this.idpresfuentefinanciamiento = idpresfuentefinanciamiento;
        this.saldodisponible = saldodisponible;
    }

    //constructor para parte de emisiones de tesoreria :)
    public PartePresupuestariaModel(Long idreserva, BigDecimal saldodisponible, String partidapresupuestaria,
            Long idprescatalogopresupuestario,
            Long idpresfuentefinanciamiento, BigDecimal montoDevengado, Integer anio, Date fecha_emision,
            Long rubro, Long tipo_liquidacion, Long ctaCatera, Long partidaCartera, Long estadoLiquidacion, Boolean municipal) {
        this.saldodisponible = saldodisponible;
        this.partidapresupuestaria = partidapresupuestaria;
        this.idprescatalogopresupuestario = idprescatalogopresupuestario;
        this.idpresfuentefinanciamiento = idpresfuentefinanciamiento;
        this.montoDevengado = montoDevengado;
        this.idreserva = idreserva;
        this.fecha_emision = fecha_emision;
        this.anio = anio;
        this.rubro = rubro;
        this.tipo_liquidacion = tipo_liquidacion;
        this.ctaCatera = ctaCatera;
        this.partidaCartera = partidaCartera;
        this.estadoLiquidacion = estadoLiquidacion;
        this.municipal = municipal;
    }

    //Recaudaciones
    public PartePresupuestariaModel(Long idreserva, BigDecimal saldodisponible, String partidapresupuestaria,
            Long idprescatalogopresupuestario,
            Long idpresfuentefinanciamiento, BigDecimal montoDevengado, Integer anio, Date fecha_emision,
            Long rubro, Long tipo_liquidacion, Long ctaCatera, Long partidaCartera, Long estadoLiquidacion,
            Boolean municipal, Boolean porcentajeDiv) {
        this.saldodisponible = saldodisponible;
        this.partidapresupuestaria = partidapresupuestaria;
        this.idprescatalogopresupuestario = idprescatalogopresupuestario;
        this.idpresfuentefinanciamiento = idpresfuentefinanciamiento;
        this.montoDevengado = montoDevengado;
        this.idreserva = idreserva;
        this.fecha_emision = fecha_emision;
        this.anio = anio;
        this.rubro = rubro;
        this.tipo_liquidacion = tipo_liquidacion;
        this.ctaCatera = ctaCatera;
        this.partidaCartera = partidaCartera;
        this.estadoLiquidacion = estadoLiquidacion;
        this.municipal = municipal;
        this.porcentajeDiv = porcentajeDiv;
    }

    public Long getIdtemp() {
        return idtemp;
    }

    public void setIdtemp(Long idtemp) {
        this.idtemp = idtemp;
    }

    public BigDecimal getSaldodisponible() {
        return saldodisponible;
    }

    public void setSaldodisponible(BigDecimal saldodisponible) {
        this.saldodisponible = saldodisponible;
    }

    public String getPartidapresupuestaria() {
        return partidapresupuestaria;
    }

    public void setPartidapresupuestaria(String partidapresupuestaria) {
        this.partidapresupuestaria = partidapresupuestaria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdprescatalogopresupuestario() {
        return idprescatalogopresupuestario;
    }

    public void setIdprescatalogopresupuestario(Long idprescatalogopresupuestario) {
        this.idprescatalogopresupuestario = idprescatalogopresupuestario;
    }

    public Long getIdpresplanprogramatico() {
        return idpresplanprogramatico;
    }

    public void setIdpresplanprogramatico(Long idpresplanprogramatico) {
        this.idpresplanprogramatico = idpresplanprogramatico;
    }

    public Long getIdpresfuentefinanciamiento() {
        return idpresfuentefinanciamiento;
    }

    public void setIdpresfuentefinanciamiento(Long idpresfuentefinanciamiento) {
        this.idpresfuentefinanciamiento = idpresfuentefinanciamiento;
    }

    public BigDecimal getMontoComprometido() {
        return montoComprometido;
    }

    public void setMontoComprometido(BigDecimal montoComprometido) {
        this.montoComprometido = montoComprometido;
    }

    public BigDecimal getMontoDisponible() {
        return montodisponible;
    }

    public void setMontoDisponible(BigDecimal montodisponible) {
        this.montodisponible = montodisponible;
    }

    public Boolean getDevengadoTotal() {
        return devengadoTotal;
    }

    public void setDevengadoTotal(Boolean devengadoTotal) {
        this.devengadoTotal = devengadoTotal;
    }

    public BigDecimal getMontoDevengado() {
        return montoDevengado;
    }

    public void setMontoDevengado(BigDecimal montoDevengado) {
        this.montoDevengado = montoDevengado;
    }

    public Long getIdreserva() {
        return idreserva;
    }

    public void setIdreserva(Long idreserva) {
        this.idreserva = idreserva;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getRubro() {
        return rubro;
    }

    public void setRubro(Long rubro) {
        this.rubro = rubro;
    }

    public Long getTipo_liquidacion() {
        return tipo_liquidacion;
    }

    public void setTipo_liquidacion(Long tipo_liquidacion) {
        this.tipo_liquidacion = tipo_liquidacion;
    }

    public Long getCtaCatera() {
        return ctaCatera;
    }

    public void setCtaCatera(Long ctaCatera) {
        this.ctaCatera = ctaCatera;
    }

    public Long getPartidaCartera() {
        return partidaCartera;
    }

    public void setPartidaCartera(Long partidaCartera) {
        this.partidaCartera = partidaCartera;
    }

    public Long getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(Long estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public Boolean getMunicipal() {
        return municipal;
    }

    public void setMunicipal(Boolean municipal) {
        this.municipal = municipal;
    }

    public Boolean getTipoPresupuesto() {
        return tipopresupuesto;
    }

    public void setTipoPresupuesto(Boolean tipopresupuesto) {
        this.tipopresupuesto = tipopresupuesto;
    }

    @Override
    public String toString() {
        return "PartePresupuestariaModel{" + "idtemp=" + idtemp + ", saldodisponible=" + saldodisponible + ", partidapresupuestaria=" + partidapresupuestaria + ", descripcion=" + descripcion + ", idprescatalogopresupuestario=" + idprescatalogopresupuestario + ", idpresplanprogramatico=" + idpresplanprogramatico + ", idpresfuentefinanciamiento=" + idpresfuentefinanciamiento + ", montoComprometido=" + montoComprometido + ", montodisponible=" + montodisponible + ", devengadoTotal=" + devengadoTotal + ", montoDevengado=" + montoDevengado + ", idreserva=" + idreserva + ", fecha_emision=" + fecha_emision + ", anio=" + anio + ", rubro=" + rubro + ", tipo_liquidacion=" + tipo_liquidacion + ", ctaCatera=" + ctaCatera + ", partidaCatera=" + partidaCartera + '}';
    }

}
