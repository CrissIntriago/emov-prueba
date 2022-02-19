/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.ws.qr_services;

import java.io.Serializable;

/**
 *
 * @author jesus
 */
public class DetalleQrResponse implements Serializable {

    private String urlImagen;//url para mostrar la imagen
    private String nombreImagen;//nombre la imagen q se genero.
    private String idAndCodigo;
    private String rutaArchivo;

    public DetalleQrResponse() {
    }

    public DetalleQrResponse(String urlImagen, String nombreImagen) {
        this.urlImagen = urlImagen;
        this.nombreImagen = nombreImagen;
    }

    public DetalleQrResponse(String urlImagen, String nombreImagen, String idAndCodigo, String rutaArchivo) {
        this.urlImagen = urlImagen;
        this.nombreImagen = nombreImagen;
        this.idAndCodigo = idAndCodigo;
        this.rutaArchivo = rutaArchivo;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getIdAndCodigo() {
        return idAndCodigo;
    }

    public void setIdAndCodigo(String idAndCodigo) {
        this.idAndCodigo = idAndCodigo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public String toString() {
        return "DetalleQrResponse{"
                + "urlImagen='" + urlImagen + '\''
                + ", nombreImagen='" + nombreImagen + '\''
                + '}';
    }

}
