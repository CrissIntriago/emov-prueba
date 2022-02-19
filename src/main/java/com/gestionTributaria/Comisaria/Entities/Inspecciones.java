/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Entities;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Formula;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ordenes", schema = Utils.SCHEMA_COMISARIA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inspecciones.findAll", query = "SELECT c FROM Inspecciones c")})
public class Inspecciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "delegado", referencedColumnName = "id")
    @ManyToOne
    private Cliente delegado;
    @Column(name = "lugar")
    private String lugar;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "origen")
    private String origen;
    @Column(name = "tipo_comisaria")
    private String tipoComisaria;
    @Column(name = "predio")
    private Long predio;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "comisaria", referencedColumnName = "id")
    @ManyToOne
    private Servidor comisario;
    @Column(name = "no_solicitud")
    private Long noSolicitud;
    @JoinColumn(name = "comisaria_user", referencedColumnName = "id")
    @ManyToOne
    private Usuarios comisariaUser;
    @Formula("(select p.clave_cat from catastro.cat_predio p where p.id=predio)")
    private String claveCatastral;
    @Formula("(select p.tipo_predio from catastro.cat_predio p where p.id=predio)")
    private String tipoPredio;

    public Inspecciones() {
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

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getTipoComisaria() {
        return tipoComisaria;
    }

    public void setTipoComisaria(String tipoComisaria) {
        this.tipoComisaria = tipoComisaria;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Servidor getComisario() {
        return comisario;
    }

    public void setComisario(Servidor comisario) {
        this.comisario = comisario;
    }

    public Long getNoSolicitud() {
        return noSolicitud;
    }

    public void setNoSolicitud(Long noSolicitud) {
        this.noSolicitud = noSolicitud;
    }

    public Usuarios getComisariaUser() {
        return comisariaUser;
    }

    public void setComisariaUser(Usuarios comisariaUser) {
        this.comisariaUser = comisariaUser;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

}
