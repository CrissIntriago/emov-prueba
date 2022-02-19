package com.origami.sigef.tesoreria.comprobantelectronico.sri.util;

import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.model.Rdep;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.factura.Factura;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notacredito.NotaCredito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notadebito.NotaDebito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.retencion.ComprobanteRetencion;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Java2XML {

    public static Boolean xmlArchivoFactura(Factura comprobante, String pathArchivoSalida) {
        Utils.createDirectoryIfNotExist(pathArchivoSalida);
        try {
            JAXBContext context = JAXBContext.newInstance(Factura.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            marshaller.marshal(comprobante, out);
            OutputStream outputStream = new FileOutputStream(pathArchivoSalida);
            byteArrayOutputStream.writeTo(outputStream);
            byteArrayOutputStream.close();
            outputStream.close();
        } catch (Exception ex) {
            Logger.getLogger(Java2XML.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean xmlArchivoNotaDeCredito(NotaCredito comprobante, String pathArchivoSalida) {
        Utils.createDirectoryIfNotExist(pathArchivoSalida);
        try {
            JAXBContext context = JAXBContext.newInstance(NotaCredito.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(pathArchivoSalida), StandardCharsets.UTF_8);
            marshaller.marshal(comprobante, out);
        } catch (Exception ex) {
            Logger.getLogger(Java2XML.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean xmlArchivoNotaDebito(NotaDebito comprobante, String pathArchivoSalida) {
        Utils.createDirectoryIfNotExist(pathArchivoSalida);
        try {
            JAXBContext context = JAXBContext.newInstance(NotaDebito.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(pathArchivoSalida), StandardCharsets.UTF_8);
            marshaller.marshal(comprobante, out);
        } catch (Exception ex) {
            Logger.getLogger(Java2XML.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean xmlArchivoComprobanteRetencion(ComprobanteRetencion comprobante, String pathArchivoSalida) {
        String respuesta = null;
        Utils.createDirectoryIfNotExist(pathArchivoSalida);
        try {
            JAXBContext context = JAXBContext.newInstance(ComprobanteRetencion.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(pathArchivoSalida), StandardCharsets.UTF_8);
            marshaller.marshal(comprobante, out);
        } catch (Exception ex) {
            Logger.getLogger(Java2XML.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean xmlArchivoFormulario107(Rdep comprobante, String pathArchivoSalida) {
        try {
            Utils.createDirectoryIfNotExist(pathArchivoSalida);
            JAXBContext context = JAXBContext.newInstance(Rdep.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            marshaller.marshal(comprobante, out);
            OutputStream outputStream = new FileOutputStream(pathArchivoSalida);
            byteArrayOutputStream.writeTo(outputStream);
            byteArrayOutputStream.close();
            outputStream.close();
        } catch (Exception ex) {
            Logger.getLogger(Java2XML.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean xmlArchivoATS(IVA ats, String pathArchivoSalida) {
        Utils.createDirectoryIfNotExist(pathArchivoSalida);
        try {
            JAXBContext context = JAXBContext.newInstance(IVA.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
            marshaller.marshal(ats, out);
            OutputStream outputStream = new FileOutputStream(pathArchivoSalida);
            byteArrayOutputStream.writeTo(outputStream);
            byteArrayOutputStream.close();
            outputStream.close();
        } catch (Exception ex) {
            Logger.getLogger(Java2XML.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
