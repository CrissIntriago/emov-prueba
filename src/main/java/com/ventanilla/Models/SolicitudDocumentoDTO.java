/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Models;

/**
 *
 * @author ricardo
 */
public class SolicitudDocumentoDTO {

    private Long id;
    private Long requisitoId;
    private String descripcion;
    private String nombreArchivo;
    private String tipoArchivo;
    private String estado;
    private String rutaArchivo;

    public SolicitudDocumentoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequisitoId() {
        return requisitoId;
    }

    public void setRequisitoId(Long requisitoId) {
        this.requisitoId = requisitoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public String toString() {
        return "SolicitudDocumentoDTO{" + "id=" + id + ", requisitoId=" + requisitoId + ", descripcion=" + descripcion + ", nombreArchivo=" + nombreArchivo + ", tipoArchivo=" + tipoArchivo + ", estado=" + estado + ", rutaArchivo=" + rutaArchivo + '}';
    }

}
