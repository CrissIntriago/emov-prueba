package com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaComprobante", propOrder = {"claveAccesoConsultada", "numeroComprobantes", "autorizaciones"})
@Entity
@Table(schema = "comprobantes_electronicos", name = "respuesta_comprobante")
public class RespuestaComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @XmlTransient
    private Long id;
    @XmlTransient
    private Long tramite;
    @XmlTransient
    private String response;
    @XmlTransient
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;
    @Transient
    private String claveAccesoConsultada;
    @Transient
    private String numeroComprobantes;
    @Transient
    private Autorizaciones autorizaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getClaveAccesoConsultada() {
        return this.claveAccesoConsultada;
    }

    public void setClaveAccesoConsultada(String value) {
        this.claveAccesoConsultada = value;
    }

    public String getNumeroComprobantes() {
        return this.numeroComprobantes;
    }

    public void setNumeroComprobantes(String value) {
        this.numeroComprobantes = value;
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

        @Override
        public String toString() {
            return "Autorizaciones{"
                    + "autorizacion=" + autorizacion
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "RespuestaComprobante{"
                + ", claveAccesoConsultada='" + claveAccesoConsultada + '\''
                + ", numeroComprobantes='" + numeroComprobantes + '\''
                + ", autorizaciones=" + autorizaciones
                + '}';
    }
}
