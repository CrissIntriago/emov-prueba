package com.origami.sigef.tesoreria.comprobantelectronico.sri.logic;

import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaComprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.RespuestaSolicitud;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.CertificadosSSL;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.DocumentosUtil;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.recepcion.offline.RecepcionComprobantesOffline;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.recepcion.offline.RecepcionComprobantesOfflineService;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.recepcion.online.RecepcionComprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.recepcion.online.RecepcionComprobantesService;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

public class EnvioComprobantesWs {

    private static final String VERSION = "1.0.0";
    private static RecepcionComprobantesService service;

    private static RecepcionComprobantesOfflineService serviceOffLine;

    public EnvioComprobantesWs(String wsdlLocation, Boolean isOnline) throws MalformedURLException {

        URL url = new URL(wsdlLocation);
        QName qname = new QName("http://ec.gob.sri.ws.recepcion",
                isOnline ? "RecepcionComprobantesService" : "RecepcionComprobantesOfflineService");
        if (isOnline) {
            service = new RecepcionComprobantesService(url, qname);
        } else {
            serviceOffLine = new RecepcionComprobantesOfflineService(url, qname);
        }
    }

    public RespuestaSolicitud enviarComprobante(File xmlFile, Boolean isOnline) throws Throwable {
        RespuestaSolicitud response = null;
        try {
            if (isOnline) {
                RecepcionComprobantes portOnline = service.getRecepcionComprobantesPort();
                response = portOnline.validarComprobante(DocumentosUtil.convertirArchivoAByteArray(xmlFile));
            } else {
//                System.out.println("xmlFile: " + xmlFile);
                RecepcionComprobantesOffline portOffLine = serviceOffLine.getRecepcionComprobantesOfflinePort();
                response = portOffLine.validarComprobante(DocumentosUtil.convertirArchivoAByteArray(xmlFile));
                System.out.println("response: " + response.toString());
            }
        } catch (Exception e) {
            throw e;
        }
        return response;
    }

    public static RespuestaSolicitud enviarArchivoXMLSRI(String xmlfirmado, String Recepcion, Boolean isOnline)
            throws Throwable {
        CertificadosSSL.instalarCertificados();
        // Con la URL del WSDL especificas si se debe conectar a los servicios de
        // pruebas o producción
        EnvioComprobantesWs ec = new EnvioComprobantesWs(Recepcion, isOnline);
        RespuestaSolicitud response;
        response = ec.enviarComprobante(new File(xmlfirmado), isOnline);
        return response;
    }

    public static RespuestaComprobante autorizarArchivoXMLSRI(String claveacceso, String OutAutorizados,
            String OutRechazados, String autorizacionWsdl, Boolean isOnline) throws Exception {
        CertificadosSSL.instalarCertificados();
        System.out.println("claveacceso " + claveacceso);
        System.out.println("OutAutorizados " + OutAutorizados);
        System.out.println("OutRechazados " + OutRechazados);
        System.out.println("autorizacionWsdl " + autorizacionWsdl);
        AutorizacionComprobantesWs ec = new AutorizacionComprobantesWs(autorizacionWsdl, isOnline);
        if (isOnline) {
            System.out.println("isOnline: " + isOnline);
            return ec.autorizarComprobante(claveacceso, OutAutorizados);
        } else {
            return ec.autorizarComprobanteOffline(claveacceso, OutAutorizados);
        }
    }

}
