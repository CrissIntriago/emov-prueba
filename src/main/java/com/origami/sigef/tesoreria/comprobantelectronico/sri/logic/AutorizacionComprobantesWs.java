package com.origami.sigef.tesoreria.comprobantelectronico.sri.logic;

import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Autorizacion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Mensaje;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaComprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaLote;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.aut.offline.AutorizacionComprobantesOffline;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.aut.offline.AutorizacionComprobantesOfflineService;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.aut.online.AutorizacionComprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.ws.aut.online.AutorizacionComprobantesService;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

public class AutorizacionComprobantesWs {

    private AutorizacionComprobantesService service;
    private AutorizacionComprobantesOfflineService serviceOffline;

    public AutorizacionComprobantesWs(String wsdlLocation, Boolean isOnline) throws MalformedURLException {
        URL url = new URL(wsdlLocation);
        QName qname = new QName("http://ec.gob.sri.ws.autorizacion",
                isOnline ? "AutorizacionComprobantesService" : "AutorizacionComprobantesOfflineService");
        if (isOnline) {
            service = new AutorizacionComprobantesService(url, qname);
        } else {
            serviceOffline = new AutorizacionComprobantesOfflineService(url, qname);
        }
    }

    public void autorizarComprobanteLote(String claveDeAcceso, String nombreArchivo, String nombreArchivoNA)
            throws FileNotFoundException, Exception {
        try {
            RespuestaLote respuesta = null;
            AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
            respuesta = port.autorizacionComprobanteLote(claveDeAcceso);
            List<Autorizacion> listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
            if (listaAutorizaciones.isEmpty()) {
                System.out.println("Lista vacía 1");
            } else {
                for (Autorizacion autorizacion : listaAutorizaciones) {
                    String estado = autorizacion.getEstado();
                    if (estado.toUpperCase().compareTo("AUTORIZADO") == 0) {
                        // Guardar comprobante autorizado
                        autorizacion.setComprobante((new StringBuilder()).append("<![CDATA[")
                                .append(autorizacion.getComprobante()).append("]]>").toString());
                        JAXBContext jc = JAXBContext.newInstance(Autorizacion.class);
                        Marshaller marshaller = jc.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                        Writer writer = new FileWriter(nombreArchivo);
                        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        marshaller.marshal(new JAXBElement<Autorizacion>(new QName("autorizacion"), Autorizacion.class,
                                autorizacion), writer);
                        writer.close();
                    } else {
                        List<Mensaje> mensajes = autorizacion.getMensajes().getMensaje();
                        if (mensajes.isEmpty()) {
                            System.out.println("Lista vacía de no autorizado.");
                        } else {
                            // Guardar comprobante no autorizado
                            autorizacion.setComprobante((new StringBuilder()).append("<![CDATA[")
                                    .append(autorizacion.getComprobante()).append("]]>").toString());
                            JAXBContext jc = JAXBContext.newInstance(Autorizacion.class);
                            Marshaller marshaller = jc.createMarshaller();
                            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                            Writer writer = new FileWriter(nombreArchivoNA);
                            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                            marshaller.marshal(new JAXBElement<Autorizacion>(new QName("autorizacion"),
                                    Autorizacion.class, autorizacion), writer);
                            writer.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RespuestaComprobante autorizarComprobanteOffline(String claveDeAcceso, String nombreArchivo) {
        RespuestaComprobante respuesta = null;
        try {

            for (int i = 0; i < 5; i++) {
                AutorizacionComprobantesOffline port = serviceOffline.getAutorizacionComprobantesOfflinePort();
                respuesta = port.autorizacionComprobante(claveDeAcceso);

                if (!respuesta.getAutorizaciones().getAutorizacion().isEmpty()) {
                    break;
                }
                Thread.currentThread();
                Thread.sleep(300L);
            }

            if (respuesta != null) {
                List<Autorizacion> listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                if (listaAutorizaciones.isEmpty()) {
                    System.out.println("No autorizado, error interno.");
                } else {
                    guardarComprobanteAutorizado(listaAutorizaciones, nombreArchivo);
                }
            } else {
                System.out.println("La respuesta es nula.");
            }
        } catch (Exception ex) {
            respuesta = null;
        }
        return respuesta;
    }

    public RespuestaComprobante autorizarComprobante(String claveDeAcceso, String nombreArchivo)
            throws FileNotFoundException, Exception {
        try {
            RespuestaComprobante respuesta = null;
            AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
            respuesta = port.autorizacionComprobante(claveDeAcceso);
            if (respuesta != null) {
                List<Autorizacion> listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                if (listaAutorizaciones.isEmpty()) {
                    System.out.println("No autorizado, error interno.");
                } else {
                    guardarComprobanteAutorizado(listaAutorizaciones, nombreArchivo);
                }
            } else {
                System.out.println("La respuesta es nula.");
            }
            return respuesta;
        } catch (Exception ex) {
            return null;
        }
    }

    private void guardarComprobanteAutorizado(List<Autorizacion> listaAutorizaciones, String nombreArchivo)
            throws FileNotFoundException, Exception {
        for (Autorizacion autorizacion : listaAutorizaciones) {
            String estado = autorizacion.getEstado();
            if (estado.toUpperCase().compareTo("AUTORIZADO") == 0) {
                autorizacion.setComprobante((new StringBuilder()).append("<![CDATA[")
                        .append(autorizacion.getComprobante()).append("]]>").toString());

                JAXBContext jc = JAXBContext.newInstance(Autorizacion.class);
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                Writer writer = new FileWriter(nombreArchivo);
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                marshaller.marshal(
                        new JAXBElement<Autorizacion>(new QName("autorizacion"), Autorizacion.class, autorizacion),
                        writer);
                writer.close();
            } else {
                // Guadar comprobante no autorizado
                List<Mensaje> mensajes = autorizacion.getMensajes().getMensaje();
                if (mensajes.isEmpty()) {
                    System.out.println("No autorizado, error interno.");
                } else {
                    autorizacion.setComprobante((new StringBuilder()).append("<![CDATA[")
                            .append(autorizacion.getComprobante()).append("]]>").toString());
                    JAXBContext jc = JAXBContext.newInstance(Autorizacion.class);
                    Marshaller marshaller = jc.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                    Writer writer = new FileWriter(nombreArchivo);
                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    marshaller.marshal(
                            new JAXBElement<Autorizacion>(new QName("autorizacion"), Autorizacion.class, autorizacion),
                            writer);
                    writer.close();
                }
            }
        }
    }

}
