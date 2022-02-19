/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Models;

import java.util.List;

/**
 *
 * @author ricardo
 */
public class SolicitudServiciosDTO {

    private Long id;
    private String descripcion;
    private Long clienteId;
    private Long tramiteId;
    private String tipoContribuyente;
    private Long predio;
    private Long liquidacionId;
    private String estado;
    private Long servicioTipoId;
    private List<SolicitudDocumentoDTO> solicitudDocumento;

    //PARA EL REPORTE DE LA VENTANILLA XD
    private String nombre;
    private Integer cantidad;

    public SolicitudServiciosDTO() {
    }

    public SolicitudServiciosDTO(String nombre, Integer cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getServicioTipoId() {
        return servicioTipoId;
    }

    public void setServicioTipoId(Long servicioTipoId) {
        this.servicioTipoId = servicioTipoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getTramiteId() {
        return tramiteId;
    }

    public void setTramiteId(Long tramiteId) {
        this.tramiteId = tramiteId;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public Long getLiquidacionId() {
        return liquidacionId;
    }

    public void setLiquidacionId(Long liquidacionId) {
        this.liquidacionId = liquidacionId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(String tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<SolicitudDocumentoDTO> getSolicitudDocumento() {
        return solicitudDocumento;
    }

    public void setSolicitudDocumento(List<SolicitudDocumentoDTO> solicitudDocumento) {
        this.solicitudDocumento = solicitudDocumento;
    }

    @Override
    public String toString() {
        return "SolicitudServiciosDTO{" + "id=" + id + ", descripcion=" + descripcion + ", clienteId=" + clienteId + ", tramiteId=" + tramiteId + ", tipoContribuyente=" + tipoContribuyente + ", predio=" + predio + ", liquidacionId=" + liquidacionId + ", estado=" + estado + ", servicioTipoId=" + servicioTipoId + '}';
    }

}
