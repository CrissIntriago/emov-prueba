package com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.recepcion.online;

import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.RespuestaSolicitud;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.aut.ObjectFactory;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "RecepcionComprobantes", targetNamespace = "http://ec.gob.sri.ws.recepcion")
@XmlSeeAlso({ObjectFactory.class})
public abstract interface RecepcionComprobantes {

    @WebMethod
    @WebResult(name = "RespuestaRecepcionComprobante", targetNamespace = "")
    @RequestWrapper(localName = "validarComprobante", targetNamespace = "http://ec.gob.sri.ws.recepcion", className = "com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.ValidarComprobante")
    @ResponseWrapper(localName = "validarComprobanteResponse", targetNamespace = "http://ec.gob.sri.ws.recepcion", className = "com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.ValidarComprobanteResponse")
    public abstract RespuestaSolicitud validarComprobante(@WebParam(name = "xml", targetNamespace = "") byte[] paramArrayOfByte);
}
