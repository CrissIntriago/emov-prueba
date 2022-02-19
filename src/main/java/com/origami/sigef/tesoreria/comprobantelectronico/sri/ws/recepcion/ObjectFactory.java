package com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.recepcion;

import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Mensaje;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.Comprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.RespuestaSolicitud;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.ValidarComprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.ValidarComprobanteResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private static final QName _ValidarComprobante_QNAME = new QName("http://ec.gob.sri.ws.recepcion", "validarComprobante");
    private static final QName _ValidarComprobanteResponse_QNAME = new QName("http://ec.gob.sri.ws.recepcion", "validarComprobanteResponse");
    private static final QName _Mensaje_QNAME = new QName("http://ec.gob.sri.ws.recepcion", "mensaje");
    private static final QName _RespuestaSolicitud_QNAME = new QName("http://ec.gob.sri.ws.recepcion", "RespuestaSolicitud");
    private static final QName _Comprobante_QNAME = new QName("http://ec.gob.sri.ws.recepcion", "comprobante");

    public Comprobante.Mensajes createComprobanteMensajes() {
        return new Comprobante.Mensajes();
    }

    public ValidarComprobante createValidarComprobante() {
        return new ValidarComprobante();
    }

    public RespuestaSolicitud createRespuestaSolicitud() {
        return new RespuestaSolicitud();
    }

    public Comprobante createComprobante() {
        return new Comprobante();
    }

    public ValidarComprobanteResponse createValidarComprobanteResponse() {
        return new ValidarComprobanteResponse();
    }

    public Mensaje createMensaje() {
        return new Mensaje();
    }

    public RespuestaSolicitud.Comprobantes createRespuestaSolicitudComprobantes() {
        return new RespuestaSolicitud.Comprobantes();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "validarComprobante")
    public JAXBElement<ValidarComprobante> createValidarComprobante(ValidarComprobante value) {
        return new JAXBElement(_ValidarComprobante_QNAME, ValidarComprobante.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "validarComprobanteResponse")
    public JAXBElement<ValidarComprobanteResponse> createValidarComprobanteResponse(ValidarComprobanteResponse value) {
        return new JAXBElement(_ValidarComprobanteResponse_QNAME, ValidarComprobanteResponse.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "mensaje")
    public JAXBElement<Mensaje> createMensaje(Mensaje value) {
        return new JAXBElement(_Mensaje_QNAME, Mensaje.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "RespuestaSolicitud")
    public JAXBElement<RespuestaSolicitud> createRespuestaSolicitud(RespuestaSolicitud value) {
        return new JAXBElement(_RespuestaSolicitud_QNAME, RespuestaSolicitud.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.recepcion", name = "comprobante")
    public JAXBElement<Comprobante> createComprobante(Comprobante value) {
        return new JAXBElement(_Comprobante_QNAME, Comprobante.class, null, value);
    }
}
