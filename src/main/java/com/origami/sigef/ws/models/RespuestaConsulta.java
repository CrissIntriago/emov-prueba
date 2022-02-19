/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ws.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class RespuestaConsulta implements Serializable {

    private String referencia;
    private String contrapartida;
    private String idDeuda;
    private String identificacion;
    private String nombres;
    private Integer periodo;
    private BigDecimal valor;
    private String detalle;
    private String codigoRespuesta;
    private String mensajeRespuesta;

    public RespuestaConsulta() {
        codigoRespuesta = "001";
        mensajeRespuesta = "TRANSACCION OK.";
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getContrapartida() {
        return contrapartida;
    }

    public void setContrapartida(String contrapartida) {
        this.contrapartida = contrapartida;
    }

    public String getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(String idDeuda) {
        this.idDeuda = idDeuda;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConsultaDeuda{referencia=").append(referencia);
        sb.append(", contrapartida=").append(contrapartida);
        sb.append(", idDeuda=").append(idDeuda);
        sb.append(", identificacion=").append(identificacion);
        sb.append(", nombres=").append(nombres);
        sb.append(", periodo=").append(periodo);
        sb.append(", valor=").append(valor);
        sb.append(", detalle=").append(detalle);
        sb.append(", codigoRespuesta=").append(codigoRespuesta);
        sb.append(", mensajeRespuesta=").append(mensajeRespuesta);
        sb.append('}');
        return sb.toString();
    }

}
