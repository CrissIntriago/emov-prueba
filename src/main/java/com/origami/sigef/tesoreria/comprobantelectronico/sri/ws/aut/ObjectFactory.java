package com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.aut;

import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Autorizacion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.AutorizacionComprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.AutorizacionComprobanteLote;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Mensaje;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaComprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaLote;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private static final QName _AutorizacionComprobanteLote_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "autorizacionComprobanteLote");
    private static final QName _RespuestaAutorizacion_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "RespuestaAutorizacion");
    private static final QName _AutorizacionComprobanteResponse_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "autorizacionComprobanteResponse");
    private static final QName _AutorizacionComprobanteLoteResponse_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "autorizacionComprobanteLoteResponse");
    private static final QName _AutorizacionComprobante_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "autorizacionComprobante");
    private static final QName _Autorizacion_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "autorizacion");
    private static final QName _Mensaje_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "mensaje");

    public Autorizacion createAutorizacion() {
        return new Autorizacion();
    }

    public RespuestaLote createRespuestaLote() {
        return new RespuestaLote();
    }

    public Autorizacion.Mensajes createAutorizacionMensajes() {
        return new Autorizacion.Mensajes();
    }

    public RespuestaLote.Autorizaciones createRespuestaLoteAutorizaciones() {
        return new RespuestaLote.Autorizaciones();
    }

    public Mensaje createMensaje() {
        return new Mensaje();
    }

    public AutorizacionComprobanteLote createAutorizacionComprobanteLote() {
        return new AutorizacionComprobanteLote();
    }

    public RespuestaComprobante createRespuestaComprobante() {
        return new RespuestaComprobante();
    }

    public AutorizacionComprobanteResponse createAutorizacionComprobanteResponse() {
        return new AutorizacionComprobanteResponse();
    }

    public AutorizacionComprobante createAutorizacionComprobante() {
        return new AutorizacionComprobante();
    }

    public RespuestaComprobante.Autorizaciones createRespuestaComprobanteAutorizaciones() {
        return new RespuestaComprobante.Autorizaciones();
    }

    public AutorizacionComprobanteLoteResponse createAutorizacionComprobanteLoteResponse() {
        return new AutorizacionComprobanteLoteResponse();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobanteLote")
    public JAXBElement<AutorizacionComprobanteLote> createAutorizacionComprobanteLote(AutorizacionComprobanteLote value) {
        return new JAXBElement(_AutorizacionComprobanteLote_QNAME, AutorizacionComprobanteLote.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "RespuestaAutorizacion")
    public JAXBElement<Object> createRespuestaAutorizacion(Object value) {
        return new JAXBElement(_RespuestaAutorizacion_QNAME, Object.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobanteResponse")
    public JAXBElement<AutorizacionComprobanteResponse> createAutorizacionComprobanteResponse(AutorizacionComprobanteResponse value) {
        return new JAXBElement(_AutorizacionComprobanteResponse_QNAME, AutorizacionComprobanteResponse.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobanteLoteResponse")
    public JAXBElement<AutorizacionComprobanteLoteResponse> createAutorizacionComprobanteLoteResponse(AutorizacionComprobanteLoteResponse value) {
        return new JAXBElement(_AutorizacionComprobanteLoteResponse_QNAME, AutorizacionComprobanteLoteResponse.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacionComprobante")
    public JAXBElement<AutorizacionComprobante> createAutorizacionComprobante(AutorizacionComprobante value) {
        return new JAXBElement(_AutorizacionComprobante_QNAME, AutorizacionComprobante.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "autorizacion")
    public JAXBElement<Autorizacion> createAutorizacion(Autorizacion value) {
        return new JAXBElement(_Autorizacion_QNAME, Autorizacion.class, null, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @XmlElementDecl(namespace = "http://ec.gob.sri.ws.autorizacion", name = "mensaje")
    public JAXBElement<Mensaje> createMensaje(Mensaje value) {
        return new JAXBElement(_Mensaje_QNAME, Mensaje.class, null, value);
    }
}
