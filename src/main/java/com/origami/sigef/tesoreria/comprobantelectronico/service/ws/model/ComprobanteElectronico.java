package com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

//@XmlRootElement
public class ComprobanteElectronico implements Serializable {

    //PARA COMPROBANTE DE FACTURA - NOTA DE CREDITO - NOTA DEBITO
    private Long idLiquidacion;
    private Long idPago;
    private String tipoLiquidacion; //TRACING CONTROL - PARKING-CONTROL
    private Long tramite;
    private Boolean isOnline;
    private Boolean generaLiquidacion;
    private Boolean reenvioVerificacion = Boolean.FALSE; //CAMPO UTILZADO PARA VERIFICAR SI LA CLAVE DE ACCESO FUE REALMENTE
    // AUTORIZADA
    private String ambiente;
    private String puntoEmision;
    private String rucEntidad;
    private String comprobanteCodigo;
    private BigDecimal descuentoAdicional;
    private Cabecera cabecera;
    //private Detalles detalles;
    private List<Detalle> detalle;
    private List<DetallePago> detallePagos;
    private List<DetalleRubro> rubroMunicipios;
    private List<DetalleRubro> rubroTercero;

    //PARA NOTAS DE CREDITO
    private String numComprobanteModifica;
    private String motivoNotaCredito;
    private String tipoDocumentoModifica;
    private String fechaEmisionDocumentoModifica;

    //PARA NOTAS DEBITO
    private List<ImpuestoComprobanteElectronico> impuestosNotaDebitos;
    private List<MotivoNotaDebito> motivosNotaDebito;
    private BigDecimal valorTotalNotaDebito;

    ///PARA COMPROBANTE DE RETENCION
    private String mes;
    private String anio;
    private List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion;

    ///PARA REENVIO DE COMPROBANTES
    private BigInteger numComprobante;
    private String claveAcceso;

    public ComprobanteElectronico() {
    }

    public String getAmbiente() {
        return ambiente;
    }

    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public Cabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(Cabecera cabecera) {
        this.cabecera = cabecera;
    }
//
//    public Detalles getDetalles() {
//        return detalles;
//    }
//
//    public void setDetalles(Detalles detalles) {
//        this.detalles = detalles;
//    }

    public List<DetalleRubro> getRubroMunicipios() {
        return rubroMunicipios;
    }

    public void setRubroMunicipios(List<DetalleRubro> rubroMunicipios) {
        this.rubroMunicipios = rubroMunicipios;
    }

    public List<DetalleRubro> getRubroTercero() {
        return rubroTercero;
    }

    public void setRubroTercero(List<DetalleRubro> rubroTercero) {
        this.rubroTercero = rubroTercero;
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public String getRucEntidad() {
        return rucEntidad;
    }

    public void setRucEntidad(String rucEntidad) {
        this.rucEntidad = rucEntidad;
    }

    public String getComprobanteCodigo() {
        return comprobanteCodigo;
    }

    public void setComprobanteCodigo(String comprobanteCodigo) {
        this.comprobanteCodigo = comprobanteCodigo;
    }

    public List<DetallePago> getDetallePagos() {
        return detallePagos;
    }

    public void setDetallePagos(List<DetallePago> detallePagos) {
        this.detallePagos = detallePagos;
    }

    public String getNumComprobanteModifica() {
        return numComprobanteModifica;
    }

    public void setNumComprobanteModifica(String numComprobanteModifica) {
        this.numComprobanteModifica = numComprobanteModifica;
    }

    public String getMotivoNotaCredito() {
        return motivoNotaCredito;
    }

    public void setMotivoNotaCredito(String motivoNotaCredito) {
        this.motivoNotaCredito = motivoNotaCredito;
    }

    public String getTipoDocumentoModifica() {
        return tipoDocumentoModifica;
    }

    public void setTipoDocumentoModifica(String tipoDocumentoModifica) {
        this.tipoDocumentoModifica = tipoDocumentoModifica;
    }

    public String getFechaEmisionDocumentoModifica() {
        return fechaEmisionDocumentoModifica;
    }

    public void setFechaEmisionDocumentoModifica(String fechaEmisionDocumentoModifica) {
        this.fechaEmisionDocumentoModifica = fechaEmisionDocumentoModifica;
    }

    public List<ImpuestoComprobanteElectronico> getImpuestosNotaDebitos() {
        return impuestosNotaDebitos;
    }

    public void setImpuestosNotaDebitos(List<ImpuestoComprobanteElectronico> impuestosNotaDebitos) {
        this.impuestosNotaDebitos = impuestosNotaDebitos;
    }

    public List<MotivoNotaDebito> getMotivosNotaDebito() {
        return motivosNotaDebito;
    }

    public void setMotivosNotaDebito(List<MotivoNotaDebito> motivosNotaDebito) {
        this.motivosNotaDebito = motivosNotaDebito;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public List<ImpuestoComprobanteElectronico> getImpuestoComprobanteRetencion() {
        return impuestoComprobanteRetencion;
    }

    public void setImpuestoComprobanteRetencion(List<ImpuestoComprobanteElectronico> impuestoComprobanteRetencion) {
        this.impuestoComprobanteRetencion = impuestoComprobanteRetencion;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public BigDecimal getDescuentoAdicional() {
        if (descuentoAdicional == null) {
            descuentoAdicional = BigDecimal.ZERO;
        }
        return descuentoAdicional;
    }

    public void setDescuentoAdicional(BigDecimal descuentoAdicional) {
        this.descuentoAdicional = descuentoAdicional;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Long getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(Long idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(String tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public Boolean getReenvioVerificacion() {
        return reenvioVerificacion;
    }

    public void setReenvioVerificacion(Boolean reenvioVerificacion) {
        this.reenvioVerificacion = reenvioVerificacion;
    }

    public Boolean getGeneraLiquidacion() {
        return generaLiquidacion;
    }

    public void setGeneraLiquidacion(Boolean generaLiquidacion) {
        this.generaLiquidacion = generaLiquidacion;
    }

    public BigDecimal getValorTotalNotaDebito() {
        return valorTotalNotaDebito;
    }

    public void setValorTotalNotaDebito(BigDecimal valorTotalNotaDebito) {
        this.valorTotalNotaDebito = valorTotalNotaDebito;
    }

    @Override
    public String toString() {
        return "ComprobanteElectronico{" + "'idLiquidacion':" + idLiquidacion
                + ", tipoLiquidacion:" + tipoLiquidacion + ", "
                + "tramite:" + tramite
                + ", isOnline:" + isOnline
                + ", generaLiquidacion:" + generaLiquidacion
                + ", reenvioVerificacion:" + reenvioVerificacion
                + ", ambiente:" + ambiente
                + ", puntoEmision:" + puntoEmision
                + ", rucEntidad:" + rucEntidad
                + ", comprobanteCodigo:" + comprobanteCodigo
                + ", descuentoAdicional:" + descuentoAdicional
                + ", cabecera:" + cabecera.toString()
                + ", detalle:" + detalle
                + ", detallePagos:" + detallePagos
                + ", numComprobanteModifica:" + numComprobanteModifica
                + ", motivoNotaCredito:" + motivoNotaCredito
                + ", tipoDocumentoModifica:" + tipoDocumentoModifica
                + ", fechaEmisionDocumentoModifica:" + fechaEmisionDocumentoModifica
                + ", impuestosNotaDebitos:" + impuestosNotaDebitos
                + ", motivosNotaDebito:" + motivosNotaDebito
                + ", valorTotalNotaDebito:" + valorTotalNotaDebito
                + ", mes:" + mes
                + ", anio:" + anio
                + ", impuestoComprobanteRetencion:" + impuestoComprobanteRetencion
                + ", numComprobante:" + numComprobante
                + ", claveAcceso:" + claveAcceso + '}';
    }

}
