package com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestaSolicitud", propOrder = {"estado", "comprobantes"})
@Entity
@Table(schema = "comprobantes_electronicos", name = "respuesta_solicitud")
public class RespuestaSolicitud implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @XmlTransient
    private Long id;

    @Column
    @XmlTransient
    private Long tramite;
    @Column
    @XmlTransient
    private String response;

    @Column(name = "fecha_ingreso")
    @XmlTransient
    private Date fechaIngreso;

    @Column(name = "codigo_error")
    @XmlTransient
    private String codigoError;
    @Column
    protected String estado;
    
    @Transient
    private Comprobantes comprobantes;

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

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String value) {
        this.estado = value;
    }

    public Comprobantes getComprobantes() {
        return this.comprobantes;
    }

    public void setComprobantes(Comprobantes value) {
        this.comprobantes = value;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"comprobante"})
    public static class Comprobantes {

        protected List<Comprobante> comprobante;

        public List<Comprobante> getComprobante() {
            if (this.comprobante == null) {
                this.comprobante = new ArrayList<>();
            }
            return this.comprobante;
        }

        @Override
        public String toString() {
            return "Comprobantes{"
                    + "comprobante=" + comprobante
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "RespuestaSolicitud{"
                + ", fechaIngreso=" + fechaIngreso
                + ", estado='" + estado + '\''
                + ", comprobantes=" + comprobantes
                + '}';
    }
}
