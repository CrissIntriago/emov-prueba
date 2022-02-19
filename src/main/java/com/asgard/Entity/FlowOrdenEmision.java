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
@Table(name = "flow_orden_emision", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowOrdenEmision.findAll", query = "SELECT f FROM FlowOrdenEmision f"),
    @NamedQuery(name = "FlowOrdenEmision.findById", query = "SELECT f FROM FlowOrdenEmision f WHERE f.id = :id"),
    @NamedQuery(name = "FlowOrdenEmision.findByAnio", query = "SELECT f FROM FlowOrdenEmision f WHERE f.anio = :anio"),
    @NamedQuery(name = "FlowOrdenEmision.findByAsunto", query = "SELECT f FROM FlowOrdenEmision f WHERE f.asunto = :asunto"),
    @NamedQuery(name = "FlowOrdenEmision.findByBaseImponible", query = "SELECT f FROM FlowOrdenEmision f WHERE f.baseImponible = :baseImponible"),
    @NamedQuery(name = "FlowOrdenEmision.findByClaveCatastral", query = "SELECT f FROM FlowOrdenEmision f WHERE f.claveCatastral = :claveCatastral"),
    @NamedQuery(name = "FlowOrdenEmision.findByClaveCatastralAnt", query = "SELECT f FROM FlowOrdenEmision f WHERE f.claveCatastralAnt = :claveCatastralAnt"),
    @NamedQuery(name = "FlowOrdenEmision.findByElaborado", query = "SELECT f FROM FlowOrdenEmision f WHERE f.elaborado = :elaborado"),
    @NamedQuery(name = "FlowOrdenEmision.findByFecha", query = "SELECT f FROM FlowOrdenEmision f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FlowOrdenEmision.findByFechaIngreso", query = "SELECT f FROM FlowOrdenEmision f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FlowOrdenEmision.findByIdentificacionPropietario", query = "SELECT f FROM FlowOrdenEmision f WHERE f.identificacionPropietario = :identificacionPropietario"),
    @NamedQuery(name = "FlowOrdenEmision.findByJefe", query = "SELECT f FROM FlowOrdenEmision f WHERE f.jefe = :jefe"),
    @NamedQuery(name = "FlowOrdenEmision.findByNombres", query = "SELECT f FROM FlowOrdenEmision f WHERE f.nombres = :nombres"),
    @NamedQuery(name = "FlowOrdenEmision.findByNumOrdenEmision", query = "SELECT f FROM FlowOrdenEmision f WHERE f.numOrdenEmision = :numOrdenEmision"),
    @NamedQuery(name = "FlowOrdenEmision.findByPropietario", query = "SELECT f FROM FlowOrdenEmision f WHERE f.propietario = :propietario"),
    @NamedQuery(name = "FlowOrdenEmision.findBySecuencia", query = "SELECT f FROM FlowOrdenEmision f WHERE f.secuencia = :secuencia"),
    @NamedQuery(name = "FlowOrdenEmision.findBySolicitante", query = "SELECT f FROM FlowOrdenEmision f WHERE f.solicitante = :solicitante"),
    @NamedQuery(name = "FlowOrdenEmision.findByTipoEmision", query = "SELECT f FROM FlowOrdenEmision f WHERE f.tipoEmision = :tipoEmision"),
    @NamedQuery(name = "FlowOrdenEmision.findByUbicacion", query = "SELECT f FROM FlowOrdenEmision f WHERE f.ubicacion = :ubicacion"),
    @NamedQuery(name = "FlowOrdenEmision.findByPara", query = "SELECT f FROM FlowOrdenEmision f WHERE f.para = :para"),
    @NamedQuery(name = "FlowOrdenEmision.findByTramite", query = "SELECT f FROM FlowOrdenEmision f WHERE f.tramite = :tramite"),
    @NamedQuery(name = "FlowOrdenEmision.findByUsuario", query = "SELECT f FROM FlowOrdenEmision f WHERE f.usuario = :usuario")})
public class FlowOrdenEmision implements Serializable {

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
    @Column(name = "base_imponible")
    private Serializable baseImponible;
    @Size(max = 255)
    @Column(name = "clave_catastral")
    private String claveCatastral;
    @Size(max = 255)
    @Column(name = "clave_catastral_ant")
    private String claveCatastralAnt;
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
    @Column(name = "identificacion_propietario")
    private String identificacionPropietario;
    @Size(max = 255)
    @Column(name = "jefe")
    private String jefe;
    @Size(max = 255)
    @Column(name = "nombres")
    private String nombres;
    @Size(max = 255)
    @Column(name = "num_orden_emision")
    private String numOrdenEmision;
    @Size(max = 255)
    @Column(name = "propietario")
    private String propietario;
    @Column(name = "secuencia")
    private Integer secuencia;
    @Size(max = 255)
    @Column(name = "solicitante")
    private String solicitante;
    @Size(max = 255)
    @Column(name = "tipo_emision")
    private String tipoEmision;
    @Size(max = 255)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Column(name = "para")
    private BigInteger para;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Column(name = "usuario")
    private BigInteger usuario;

    public FlowOrdenEmision() {
    }

    public FlowOrdenEmision(Long id) {
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

    public Serializable getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(Serializable baseImponible) {
        this.baseImponible = baseImponible;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getClaveCatastralAnt() {
        return claveCatastralAnt;
    }

    public void setClaveCatastralAnt(String claveCatastralAnt) {
        this.claveCatastralAnt = claveCatastralAnt;
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

    public String getIdentificacionPropietario() {
        return identificacionPropietario;
    }

    public void setIdentificacionPropietario(String identificacionPropietario) {
        this.identificacionPropietario = identificacionPropietario;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNumOrdenEmision() {
        return numOrdenEmision;
    }

    public void setNumOrdenEmision(String numOrdenEmision) {
        this.numOrdenEmision = numOrdenEmision;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlowOrdenEmision)) {
            return false;
        }
        FlowOrdenEmision other = (FlowOrdenEmision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowOrdenEmision[ id=" + id + " ]";
    }
    
}
