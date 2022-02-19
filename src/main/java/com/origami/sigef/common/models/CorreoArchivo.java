package com.origami.sigef.common.models;

public class CorreoArchivo {

    private String nombreArchivo;
    private String tipoArchivo; //pdf - jpg - png
    private String archivoBase64;

    public CorreoArchivo() {
    }

    public CorreoArchivo(String nombreArchivo, String tipoArchivo, String archivoBase64) {
        this.nombreArchivo = nombreArchivo;
        this.tipoArchivo = tipoArchivo;
        this.archivoBase64 = archivoBase64;
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

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }
}
