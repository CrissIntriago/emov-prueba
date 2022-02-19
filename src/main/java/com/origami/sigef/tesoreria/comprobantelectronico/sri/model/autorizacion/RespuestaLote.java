package com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaLote", propOrder = {"claveAccesoLoteConsultada", "numeroComprobantesLote", "autorizaciones"})
public class RespuestaLote {

    protected String claveAccesoLoteConsultada;
    protected String numeroComprobantesLote;
    protected Autorizaciones autorizaciones;

    public String getClaveAccesoLoteConsultada() {
        return this.claveAccesoLoteConsultada;
    }

    public void setClaveAccesoLoteConsultada(String value) {
        this.claveAccesoLoteConsultada = value;
    }

    public String getNumeroComprobantesLote() {
        return this.numeroComprobantesLote;
    }

    public void setNumeroComprobantesLote(String value) {
        this.numeroComprobantesLote = value;
    }

    public Autorizaciones getAutorizaciones() {
        return this.autorizaciones;
    }

    public void setAutorizaciones(Autorizaciones value) {
        this.autorizaciones = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"autorizacion"})
    public static class Autorizaciones {

        protected List<Autorizacion> autorizacion;

        public List<Autorizacion> getAutorizacion() {
            if (this.autorizacion == null) {
                this.autorizacion = new ArrayList<>();
            }
            return this.autorizacion;
        }
    }
}
