/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "flow_nota_devolutiva", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowNotaDevolutiva.findAll", query = "SELECT f FROM FlowNotaDevolutiva f"),
    @NamedQuery(name = "FlowNotaDevolutiva.findById", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.id = :id"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByAnio", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.anio = :anio"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByAsunto", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.asunto = :asunto"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByDetalle", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.detalle = :detalle"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByEditable", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.editable = :editable"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByElaborado", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.elaborado = :elaborado"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByFecha", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByFechaIngreso", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByNumNotaDevolutiva", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.numNotaDevolutiva = :numNotaDevolutiva"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByObservaciones", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.observaciones = :observaciones"),
    @NamedQuery(name = "FlowNotaDevolutiva.findBySecuencia", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.secuencia = :secuencia"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByPara", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.para = :para"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByTramite", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.tramite = :tramite"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByUsuario", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.usuario = :usuario"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByClaveCatastral", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.claveCatastral = :claveCatastral"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByParroquia", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.parroquia = :parroquia"),
    @NamedQuery(name = "FlowNotaDevolutiva.findByImagenes", query = "SELECT f FROM FlowNotaDevolutiva f WHERE f.imagenes = :imagenes")})
public class FlowNotaDevolutiva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    @Size(max = 255)
    @Column(name = "asunto")
    private String asunto;
    @Size(max = 2147483647)
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "editable")
    private Boolean editable;
    @Size(max = 255)
    @Column(name = "elaborado")
    private String elaborado;
    @Size(max = 255)
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 255)
    @Column(name = "num_nota_devolutiva")
    private String numNotaDevolutiva;
    @Size(max = 2147483647)
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "secuencia")
    private Integer secuencia;
    @Column(name = "para")
    private BigInteger para;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Column(name = "usuario")
    private BigInteger usuario;
    @Size(max = 255)
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Size(max = 255)
    @Column(name = "parroquia")
    private String parroquia;
    @Column(name = "imagenes")
    private Serializable imagenes;

    public FlowNotaDevolutiva() {
    }

    public FlowNotaDevolutiva(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getElaborado() {
        return elaborado;
    }

    public void setElaborado(String elaborado) {
        this.elaborado = elaborado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getNumNotaDevolutiva() {
        return numNotaDevolutiva;
    }

    public void setNumNotaDevolutiva(String numNotaDevolutiva) {
        this.numNotaDevolutiva = numNotaDevolutiva;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getPara() {
        return para;
    }

    public void setPara(BigInteger para) {
        this.para = para;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public BigInteger getUsuario() {
        return usuario;
    }

    public void setUsuario(BigInteger usuario) {
        this.usuario = usuario;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public Serializable getImagenes() {
        return imagenes;
    }

    public void setImagenes(Serializable imagenes) {
        this.imagenes = imagenes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlowNotaDevolutiva)) {
            return false;
        }
        FlowNotaDevolutiva other = (FlowNotaDevolutiva) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowNotaDevolutiva[ id=" + id + " ]";
    }
    
}
