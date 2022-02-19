/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLocalComercial;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ORIGAMIEC
 */
@Entity
@Table(name = "ordenes", schema = Utils.SCHEMA_COMISARIA)
@XmlRootElement
public class Ordenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "delegado", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente delegado;
    @Column(name = "lugar")
    private String lugar;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @Column(name = "tipo_tramite")
    private BigInteger tipoTramite;
    @Column(name = "origen")
    private String origen;
    @Column(name = "comisaria")
    private BigInteger comisaria;
    @Column(name = "tipo_comisaria")
    private String tipoComisaria;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatPredio predio;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "no_solicitud")
    private BigInteger noSolicitud;
    @Column(name = "comisaria_user")
    private BigInteger comisariaUser;
    @JoinColumn(name = "local_comercial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenLocalComercial localComercial;
    @Column(name = "actividad_local")
    private BigInteger actividadLocal;

    public Ordenes() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getDelegado() {
        return delegado;
    }

    public void setDelegado(Cliente delegado) {
        this.delegado = delegado;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public BigInteger getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(BigInteger tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public BigInteger getComisaria() {
        return comisaria;
    }

    public void setComisaria(BigInteger comisaria) {
        this.comisaria = comisaria;
    }

    public String getTipoComisaria() {
        return tipoComisaria;
    }

    public void setTipoComisaria(String tipoComisaria) {
        this.tipoComisaria = tipoComisaria;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigInteger getNoSolicitud() {
        return noSolicitud;
    }

    public void setNoSolicitud(BigInteger noSolicitud) {
        this.noSolicitud = noSolicitud;
    }

    public BigInteger getComisariaUser() {
        return comisariaUser;
    }

    public void setComisariaUser(BigInteger comisariaUser) {
        this.comisariaUser = comisariaUser;
    }

    public String toSting() {
        return "id" + id;
    }

    public FinaRenLocalComercial getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(FinaRenLocalComercial localComercial) {
        this.localComercial = localComercial;
    }

    public BigInteger getActividadLocal() {
        return actividadLocal;
    }

    public void setActividadLocal(BigInteger actividadLocal) {
        this.actividadLocal = actividadLocal;
    }

}
