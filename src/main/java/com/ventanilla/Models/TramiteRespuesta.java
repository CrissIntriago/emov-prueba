/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Models;

/**
 *
 * @author ricardo
 */
public class TramiteRespuesta {

    private String estado;
    private String mensaje;
    private SolicitudServiciosDTO solicitud;

    public TramiteRespuesta() {
    }

    public TramiteRespuesta(String estado, String mensaje, SolicitudServiciosDTO solicitud) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.solicitud = solicitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public SolicitudServiciosDTO getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServiciosDTO solicitud) {
        this.solicitud = solicitud;
    }

    @Override
    public String toString() {
        return "TramiteRespuesta{" + "estado=" + estado + ", mensaje=" + mensaje + ", solicitud=" + solicitud + '}';
    }

}
