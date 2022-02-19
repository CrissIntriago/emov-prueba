/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "papp_proceso")
@NamedQueries({
    @NamedQuery(name = "PappProceso.findAll", query = "SELECT p FROM PappProceso p"),
    @NamedQuery(name = "PappProceso.findById", query = "SELECT p FROM PappProceso p WHERE p.id = :id"),
    @NamedQuery(name = "PappProceso.findByNumeroTramite", query = "SELECT p FROM PappProceso p WHERE p.numeroTramite = :numeroTramite"),
    @NamedQuery(name = "PappProceso.findByFechaTramite", query = "SELECT p FROM PappProceso p WHERE p.fechaTramite = :fechaTramite")})
public class PappProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero_tramite")
    private BigInteger numeroTramite;
    @Column(name = "usuario_solicita")
    private String usuarioSolicita;
    @Column(name = "email")
    private String email;
    @Column(name = "fecha_tramite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTramite;
    @Column(name = "fecha_aprovacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprovacion;
    @JoinColumn(name = "estado_proceso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoProceso;
    @JoinColumn(name = "papp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanAnualProgramaProyecto papp;
    @JoinColumn(name = "unidad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidad;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "secuencial")
    private Short secuencial;
    @JoinColumn(name = "actividad_operativa", referencedColumnName = "id")
    @ManyToOne
    private ActividadOperativa actividadOperativa;
    @Column(name = "ruta_certificado")
    private String rutaCertificado;
    @Column(name = "solicitado")
    private BigDecimal solicitado;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem estado;

    public PappProceso() {
        solicitado = BigDecimal.ZERO;
    }

    public PappProceso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuarioSolicita() {
        return usuarioSolicita;
    }

    public void setUsuarioSolicita(String usuarioSolicita) {
        this.usuarioSolicita = usuarioSolicita;
    }

    public Date getFechaAprovacion() {
        return fechaAprovacion;
    }

    public void setFechaAprovacion(Date fechaAprovacion) {
        this.fechaAprovacion = fechaAprovacion;
    }

    public BigInteger getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(BigInteger numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Date getFechaTramite() {
        return fechaTramite;
    }

    public void setFechaTramite(Date fechaTramite) {
        this.fechaTramite = fechaTramite;
    }

    public CatalogoItem getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(CatalogoItem estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public PlanAnualProgramaProyecto getPapp() {
        return papp;
    }

    public void setPapp(PlanAnualProgramaProyecto papp) {
        this.papp = papp;
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Short getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Short secuencial) {
        this.secuencial = secuencial;
    }

    public ActividadOperativa getActividadOperativa() {
        return actividadOperativa;
    }

    public void setActividadOperativa(ActividadOperativa actividadOperativa) {
        this.actividadOperativa = actividadOperativa;
    }

    public String getRutaCertificado() {
        return rutaCertificado;
    }

    public void setRutaCertificado(String rutaCertificado) {
        this.rutaCertificado = rutaCertificado;
    }

    public BigDecimal getSolicitado() {
        return solicitado;
    }

    public void setSolicitado(BigDecimal solicitado) {
        this.solicitado = solicitado;
    }

    public CatalogoItem getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItem estado) {
        this.estado = estado;
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
        if (!(object instanceof PappProceso)) {
            return false;
        }
        PappProceso other = (PappProceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.PappProceso[ id=" + id + " ]";
    }

}
