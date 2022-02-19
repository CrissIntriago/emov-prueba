package com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "autorizacion", propOrder = {"ambiente", "estado", "numeroAutorizacion", "fechaAutorizacion",
    "comprobante", "mensajes"})
public class Autorizacion {

    protected String ambiente;
    protected String estado;
    protected String numeroAutorizacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAutorizacion;
    protected String comprobante;
    protected Mensajes mensajes;

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String value) {
        this.estado = value;
    }

    public String getNumeroAutorizacion() {
        return this.numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String value) {
        this.numeroAutorizacion = value;
    }

    public XMLGregorianCalendar getFechaAutorizacion() {
        return this.fechaAutorizacion;
    }

    public void setFechaAutorizacion(XMLGregorianCalendar value) {
        this.fechaAutorizacion = value;
    }

    public String getComprobante() {
        return this.comprobante;
    }

    public void setComprobante(String value) {
        this.comprobante = value;
    }

    public Mensajes getMensajes() {
        return this.mensajes;
    }

    public void setMensajes(Mensajes value) {
        this.mensajes = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"mensaje"})
    public static class Mensajes {

        protected List<com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Mensaje> mensaje;

        public List<com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Mensaje> getMensaje() {
            if (this.mensaje == null) {
                this.mensaje = new ArrayList();
            }
            return this.mensaje;
        }

        @Override
        public String toString() {
            return "Mensajes{"
                    + "mensaje=" + mensaje
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "Autorizacion{"
                + "estado='" + estado + '\''
                + ", numeroAutorizacion='" + numeroAutorizacion + '\''
                + ", fechaAutorizacion=" + fechaAutorizacion
                + ", comprobante='" + comprobante + '\''
                + ", mensajes=" + mensajes
                + '}';
    }
}
