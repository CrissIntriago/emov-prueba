package com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.aut.online;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "AutorizacionComprobantesService", targetNamespace = "http://ec.gob.sri.ws.autorizacion", wsdlLocation = "/META-INF/wsdl/AutorizacionComprobantes.wsdl")
public class AutorizacionComprobantesService
        extends Service {

    private static final URL AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION;
    private static final WebServiceException AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION;
    private static final QName AUTORIZACIONCOMPROBANTESSERVICE_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService");

    static {
        System.out.println("");
        AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION = AutorizacionComprobantesService.class.getResource("/META-INF/wsdl/AutorizacionComprobantes.wsdl");
        WebServiceException e = null;
        if (AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find '/META-INF/wsdl/AutorizacionComprobantes.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION = e;
    }

    public AutorizacionComprobantesService() {
        super(__getWsdlLocation(), AUTORIZACIONCOMPROBANTESSERVICE_QNAME);
    }

    public AutorizacionComprobantesService(URL wsdlLocation) {
        super(wsdlLocation, AUTORIZACIONCOMPROBANTESSERVICE_QNAME);
    }

    public AutorizacionComprobantesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    @WebEndpoint(name = "AutorizacionComprobantesPort")
    public AutorizacionComprobantes getAutorizacionComprobantesPort() {
        return (AutorizacionComprobantes) super.getPort(new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesPort"), AutorizacionComprobantes.class);
    }

    @WebEndpoint(name = "AutorizacionComprobantesPort")
    public AutorizacionComprobantes getAutorizacionComprobantesPort(WebServiceFeature... features) {
        return (AutorizacionComprobantes) super.getPort(new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesPort"), AutorizacionComprobantes.class, features);
    }

    private static URL __getWsdlLocation() {
        if (AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION != null) {
            throw AUTORIZACIONCOMPROBANTESSERVICE_EXCEPTION;
        }
        return AUTORIZACIONCOMPROBANTESSERVICE_WSDL_LOCATION;
    }
}
