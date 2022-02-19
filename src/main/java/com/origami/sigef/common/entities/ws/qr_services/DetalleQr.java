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
public class DetalleQr implements Serializable {

    private String archivo;//archivo donde se va almacenar
    private String idAndCodigo; // nombre de la imagen, barcode.
    private String codigoItem;
    private String bodega; //nombre de la bodega ubicado
    private String titulo; //titulo en la imagen a mostrar
    private String descripcion; // descripcion en la imagen a mostrar
    private String rutaArchivoPlantilla; //ruta de la plantilla para construir la imagen
    private String nombreImagen;
    private String tipo;

    public DetalleQr() {
    }

    public DetalleQr(String archivo, String idAndCodigo, String title, String descripcion, String rutaArchivoPlantilla, String nombreImagen) {
        this.archivo = archivo;
        this.idAndCodigo = idAndCodigo;
        this.titulo = title;
        this.descripcion = descripcion;
        this.rutaArchivoPlantilla = rutaArchivoPlantilla;
        this.nombreImagen = nombreImagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getRutaArchivoPlantilla() {
        return rutaArchivoPlantilla;
    }

    public void setRutaArchivoPlantilla(String rutaArchivoPlantilla) {
        this.rutaArchivoPlantilla = rutaArchivoPlantilla;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getIdAndCodigo() {
        return idAndCodigo;
    }

    public void setIdAndCodigo(String idAndCodigo) {
        this.idAndCodigo = idAndCodigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "DetalleQr{"
                + "archivo='" + archivo + '\''
                + ", idAndCodigo='" + idAndCodigo + '\''
                + ", titulo='" + titulo + '\''
                + ", descripcion='" + descripcion + '\''
                + ", rutaArchivoPlantilla='" + rutaArchivoPlantilla + '\''
                + '}';
    }

}
