/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "solicitud_requisito", schema = "certificacion_presupuestaria_anual")
@NamedQueries({
    @NamedQuery(name = "SolicitudRequisito.findAll", query = "SELECT s FROM SolicitudRequisito s"),
    @NamedQuery(name = "SolicitudRequisito.findById", query = "SELECT s FROM SolicitudRequisito s WHERE s.id = :id"),
    @NamedQuery(name = "SolicitudRequisito.findByUrl", query = "SELECT s FROM SolicitudRequisito s WHERE s.url = :url")})
public class SolicitudRequisito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "id_procedimiento_requisito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ProcedimientoRequisito idProcedimientoRequisito;
    @JoinColumn(name = "id_solicitud_reserva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudReservaCompromiso idSolicitudReserva;

    public SolicitudRequisito() {
    }

    public SolicitudRequisito(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProcedimientoRequisito getIdProcedimientoRequisito() {
        return idProcedimientoRequisito;
    }

    public void setIdProcedimientoRequisito(ProcedimientoRequisito idProcedimientoRequisito) {
        this.idProcedimientoRequisito = idProcedimientoRequisito;
    }

    public SolicitudReservaCompromiso getIdSolicitudReserva() {
        return idSolicitudReserva;
    }

    public void setIdSolicitudReserva(SolicitudReservaCompromiso idSolicitudReserva) {
        this.idSolicitudReserva = idSolicitudReserva;
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
        if (!(object instanceof SolicitudRequisito)) {
            return false;
        }
        SolicitudRequisito other = (SolicitudRequisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.SolicitudRequisito[ id=" + id + " ]";
    }

}
