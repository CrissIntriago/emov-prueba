package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.math.BigDecimal;

public class ImpuestoComprobanteElectronico {

    private String ambiente;
    private String codigo;
    ///CODIGO PORCENTAJES ES CUALL CDOIGO DE PORCENTAJE SE APLICA 10% - 100% 60% ETC.. SENGUN LAS TBLAS DEL SRI =/
    private String codigoPorcentaje;
    private BigDecimal tarifa;
    private BigDecimal baseImponible;
    private BigDecimal valor;

    //Variables para Comprobantes de Rentencion
    private String fechaEmisionDocumento;
    private String codigoDocumento;
    private String numDocumento;
    private BigDecimal porcentajeRetencion;
    //SE LLENAN DESPUES DE CREAR EL COMPROBANTE ES PARA ELREPORTE
    private String descripcionDocumentoModificado;
    private String descripcionImpuestoRetenido;

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String value) {
        this.codigo = value;
    }

    public String getCodigoPorcentaje() {
        return this.codigoPorcentaje;
    }

    public void setCodigoPorcentaje(String value) {
        this.codigoPorcentaje = value;
    }

    public BigDecimal getTarifa() {
        return this.tarifa;
    }

    public void setTarifa(BigDecimal value) {
        this.tarifa = value;
    }

    public BigDecimal getBaseImponible() {
        return this.baseImponible;
    }

    public void setBaseImponible(BigDecimal value) {
        this.baseImponible = value;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public void setValor(BigDecimal value) {
        this.valor = value;
    }

    public String getFechaEmisionDocumento() {
        return fechaEmisionDocumento;
    }

    public void setFechaEmisionDocumento(String fechaEmisionDocumento) {
        this.fechaEmisionDocumento = fechaEmisionDocumento;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public BigDecimal getPorcentajeRetencion() {
        return porcentajeRetencion;
    }

    public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
    }

    public String getDescripcionDocumentoModificado() {
        return descripcionDocumentoModificado;
    }

    public void setDescripcionDocumentoModificado(String descripcionDocumentoModificado) {
        this.descripcionDocumentoModificado = descripcionDocumentoModificado;
    }

    public String getDescripcionImpuestoRetenido() {
        return descripcionImpuestoRetenido;
    }

    public void setDescripcionImpuestoRetenido(String descripcionImpuestoRetenido) {
        this.descripcionImpuestoRetenido = descripcionImpuestoRetenido;
    }
}
