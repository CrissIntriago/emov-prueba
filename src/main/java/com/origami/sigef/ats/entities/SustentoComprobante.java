/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ORIGAMI
 */
@Entity
@Table(name = "sustento_comprobante", schema = "ats")
public class SustentoComprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "tipo_sustento")
    private String tipoSustento;
    @Column(name = "codigo_tipo_comprobante")
    private String codigoTipoComprobante;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;

    public SustentoComprobante() {
    }

    public SustentoComprobante(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipoSustento() {
        return tipoSustento;
    }

    public void setTipoSustento(String tipoSustento) {
        this.tipoSustento = tipoSustento;
    }

    public String getCodigoTipoComprobante() {
        return codigoTipoComprobante;
    }

    public void setCodigoTipoComprobante(String codigoTipoComprobante) {
        this.codigoTipoComprobante = codigoTipoComprobante;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.ats.entities.SustentoComprobante{" + "id=" + id + ", "
                + "codigo=" + codigo + ", tipoSustento=" + tipoSustento + ", "
                + "codigoTipoComprobante=" + codigoTipoComprobante + ", fechaInicio=" + fechaInicio + ", "
                + "fechaFin=" + fechaFin + '}';
    }

}
