/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Table(name = "firmas_electronicas", schema = "public")
@Entity
@XmlRootElement
public class FirmaElectronica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "archivo")
    private String archivo; //FIRMA ELECTRONICA .P12
    @Column(name = "clave")
    private String clave; // CLAVE DE LA FIRMA ELECTRONICA
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Usuarios usuario;
    @Column(name = "cn")
    private String cn;
    @Column(name = "emision")
    private String emision;
    @Column(name = "estado_firma")
    private String estadoFirma; //Certificado revocado - Certificado caducado  - Certificado emitido por entidad certificadora
    @Column(name = "isuser")
    private String isuser;
    @Column(name = "tipo_firma")
    private String tipoFirma; // information2 - QR { INFORMACION SALE SOLO INFO D LA FIRMA QR SALE LA INFO D LA FIRMA EN EL QR MAS LA URL DE CONSULTA }
    @Column(name = "uid")
    private String uid;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "fecha_expiracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;
    @Column(name = "firma_digital")
    private String firmaDigital; //ARCHIVO DE RUTA DE IMAGEN DE LA FIRMA DIGITAL DE LA PERSONA
    @Column(name = "ubicacion")
    private String ubicacion; //DESDE DONDE SE FIRMA =V

    @Transient
    private Integer numeroFirma; //Numero de veces que se ha firmado el documento por defecto debe ser 0
    @Transient
    private String motivo; //NOMBRE DEL TRAMITE
    @Transient
    private String archivoFirmar; //ARCHIVO EN PDF
    @Transient
    private String archivoFirmado; //ARCHIVO EN PDF FIRMADO
    @Transient
    private String urlArchivoFirmado; //URL DE ARCHIVO EN PDF FIRMADO //desabilitado
    @Transient
    private String urlQr; //URL DEL ARCHIVO
    @Transient
    private Integer numeroPagina; //NUMERO DE LA PAGINA QUE NECESITA SER FIRMADA -- SI NO TIENE POR DEFAULT COJE LA ULTIMA
    /*
        Posicion de la firma
     */
    @Transient
    private String posicionX1;
    @Transient
    private String posicionX2;
    @Transient
    private String posicionY1;
    @Transient
    private String posicionY2;

    public FirmaElectronica() {
    }

    public FirmaElectronica(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(String tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getArchivoFirmar() {
        return archivoFirmar;
    }

    public void setArchivoFirmar(String archivoFirmar) {
        this.archivoFirmar = archivoFirmar;
    }

    public String getUrlQr() {
        return urlQr;
    }

    public void setUrlQr(String urlQr) {
        this.urlQr = urlQr;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getIsuser() {
        return isuser;
    }

    public void setIsuser(String isuser) {
        this.isuser = isuser;
    }

    public String getFirmaDigital() {
        return firmaDigital;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Integer getNumeroFirma() {
        return numeroFirma;
    }

    public void setNumeroFirma(Integer numeroFirma) {
        this.numeroFirma = numeroFirma;
    }

    public String getArchivoFirmado() {
        return archivoFirmado;
    }

    public void setArchivoFirmado(String archivoFirmado) {
        this.archivoFirmado = archivoFirmado;
    }

    public String getPosicionX1() {
        return posicionX1;
    }

    public void setPosicionX1(String posicionX1) {
        this.posicionX1 = posicionX1;
    }

    public String getPosicionX2() {
        return posicionX2;
    }

    public void setPosicionX2(String posicionX2) {
        this.posicionX2 = posicionX2;
    }

    public String getPosicionY1() {
        return posicionY1;
    }

    public void setPosicionY1(String posicionY1) {
        this.posicionY1 = posicionY1;
    }

    public String getPosicionY2() {
        return posicionY2;
    }

    public void setPosicionY2(String posicionY2) {
        this.posicionY2 = posicionY2;
    }

    public String getUrlArchivoFirmado() {
        return urlArchivoFirmado;
    }

    public void setUrlArchivoFirmado(String urlArchivoFirmado) {
        this.urlArchivoFirmado = urlArchivoFirmado;
    }

    @Override
    public String toString() {
        return "FirmaElectronica{" + "id=" + id + ", archivo=" + archivo + ", clave=" + clave + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + ", tipoFirma=" + tipoFirma + ", ubicacion=" + ubicacion + ", motivo=" + motivo + ", archivoFirmar=" + archivoFirmar + ", archivoFirmado=" + archivoFirmado + ", urlArchivoFirmado=" + urlArchivoFirmado + ", urlQr=" + urlQr + ", numeroPagina=" + numeroPagina + ", firmaDigital=" + firmaDigital + ", numeroFirma=" + numeroFirma + ", estadoFirma=" + estadoFirma + ", uid=" + uid + ", cn=" + cn + ", emision=" + emision + ", fechaEmision=" + fechaEmision + ", fechaExpiracion=" + fechaExpiracion + ", isuser=" + isuser + ", posicionX1=" + posicionX1 + ", posicionX2=" + posicionX2 + ", posicionY1=" + posicionY1 + ", posicionY2=" + posicionY2 + '}';
    }
}
