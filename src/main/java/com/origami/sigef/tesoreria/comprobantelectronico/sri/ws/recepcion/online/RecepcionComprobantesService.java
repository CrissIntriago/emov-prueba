package com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.recepcion.online;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "RecepcionComprobantesService", targetNamespace = "http://ec.gob.sri.ws.recepcion", wsdlLocation = "/META-INF/wsdl/RecepcionComprobantes.wsdl")
public class RecepcionComprobantesService
        extends Service {

    private static final URL RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION;
    private static final WebServiceException RECEPCIONCOMPROBANTESSERVICE_EXCEPTION;
    private static final QName RECEPCIONCOMPROBANTESSERVICE_QNAME = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService");

    static {
        System.out.println("RecepcionComprobantesService.class.getResource(META-INF-wsdl-RecepcionComprobantes.wsdl: " + RecepcionComprobantesService.class.getResource("/META-INF/wsdl/RecepcionComprobantes.wsdl"));
        RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION = RecepcionComprobantesService.class.getResource("/META-INF/wsdl/RecepcionComprobantes.wsdl");
        WebServiceException e = null;
        System.out.println("RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION: " + RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION);
        if (RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find '/META-INF/wsdl/RecepcionComprobantes.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        RECEPCIONCOMPROBANTESSERVICE_EXCEPTION = e;
    }

    public RecepcionComprobantesService() {
        super(__getWsdlLocation(), RECEPCIONCOMPROBANTESSERVICE_QNAME);
    }

    public RecepcionComprobantesService(URL wsdlLocation) {
        super(wsdlLocation, RECEPCIONCOMPROBANTESSERVICE_QNAME);
    }

    public RecepcionComprobantesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    @WebEndpoint(name = "RecepcionComprobantesPort")
    public RecepcionComprobantes getRecepcionComprobantesPort() {
        return (RecepcionComprobantes) super.getPort(new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesPort"), RecepcionComprobantes.class);
    }

    @WebEndpoint(name = "RecepcionComprobantesPort")
    public RecepcionComprobantes getRecepcionComprobantesPort(WebServiceFeature... features) {
        return (RecepcionComprobantes) super.getPort(new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesPort"), RecepcionComprobantes.class, features);
    }

    private static URL __getWsdlLocation() {
        if (RECEPCIONCOMPROBANTESSERVICE_EXCEPTION != null) {
            throw RECEPCIONCOMPROBANTESSERVICE_EXCEPTION;
        }
        return RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION;
    }
}
