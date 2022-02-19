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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "flow_regp_tareas_tramite", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowRegpTareasTramite.findAll", query = "SELECT f FROM FlowRegpTareasTramite f"),
    @NamedQuery(name = "FlowRegpTareasTramite.findById", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.id = :id"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByTramite", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.tramite = :tramite"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByFechaFin", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.fechaFin = :fechaFin"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByRealizado", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.realizado = :realizado"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByEstado", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.estado = :estado"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByFecha", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByDocumento", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.documento = :documento"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByCorregido", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.corregido = :corregido"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByFicha", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.ficha = :ficha"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByRevisado", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.revisado = :revisado"),
    @NamedQuery(name = "FlowRegpTareasTramite.findByFechaRevision", query = "SELECT f FROM FlowRegpTareasTramite f WHERE f.fechaRevision = :fechaRevision")})
public class FlowRegpTareasTramite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "tramite")
    private BigInteger tramite;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "realizado")
    private Boolean realizado;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "documento")
    private Boolean documento;
    @Column(name = "corregido")
    private Boolean corregido;
    @Column(name = "ficha")
    private Boolean ficha;
    @Column(name = "revisado")
    private Boolean revisado;
    @Column(name = "fecha_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRevision;
    @JoinColumn(name = "detalle", referencedColumnName = "id")
    @ManyToOne
    private FlowRegpLiquidacionDetalles detalle;

    public FlowRegpTareasTramite() {
    }

    public FlowRegpTareasTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getTramite() {
        return tramite;
    }

    public void setTramite(BigInteger tramite) {
        this.tramite = tramite;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getRealizado() {
        return realizado;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
    }

    public Boolean getCorregido() {
        return corregido;
    }

    public void setCorregido(Boolean corregido) {
        this.corregido = corregido;
    }

    public Boolean getFicha() {
        return ficha;
    }

    public void setFicha(Boolean ficha) {
        this.ficha = ficha;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public FlowRegpLiquidacionDetalles getDetalle() {
        return detalle;
    }

    public void setDetalle(FlowRegpLiquidacionDetalles detalle) {
        this.detalle = detalle;
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
        if (!(object instanceof FlowRegpTareasTramite)) {
            return false;
        }
        FlowRegpTareasTramite other = (FlowRegpTareasTramite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowRegpTareasTramite[ id=" + id + " ]";
    }
    
}
