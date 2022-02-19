package com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comprobante", propOrder = {"claveAcceso", "mensajes"})
public class Comprobante {

    protected String claveAcceso;
    protected Mensajes mensajes;

    public String getClaveAcceso() {
        return this.claveAcceso;
    }

    public void setClaveAcceso(String value) {
        this.claveAcceso = value;
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

        protected List<com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.Mensaje> mensaje;

        public List<com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.Mensaje> getMensaje() {
            if (this.mensaje == null) {
                this.mensaje = new ArrayList<>();
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
    public String
            toString() {
        return "Comprobantes{"
                + "claveAcceso='" + claveAcceso + '\''
                + ", mensajes=" + mensajes
                + '}';
    }
}
