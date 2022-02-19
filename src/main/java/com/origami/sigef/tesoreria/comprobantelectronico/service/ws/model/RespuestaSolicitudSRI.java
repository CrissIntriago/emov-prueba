package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.io.Serializable;

public class RespuestaSolicitudSRI implements Serializable {

    private static final long serialVersionUID = 1L;

    private String estado;
    private String identificador;
    private String mensaje;
    private String informacionAdicional;
    private String tipo;
    private String numeroComprobantes;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumeroComprobantes() {
        return numeroComprobantes;
    }

    public void setNumeroComprobantes(String numeroComprobantes) {
        this.numeroComprobantes = numeroComprobantes;
    }

}
