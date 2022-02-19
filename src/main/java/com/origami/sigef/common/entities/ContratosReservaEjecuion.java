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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "contratos_reserva_ejecuion", schema = "certificacion_presupuestaria_anual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContratosReservaEjecuion.findAll", query = "SELECT c FROM ContratosReservaEjecuion c"),
    @NamedQuery(name = "ContratosReservaEjecuion.findById", query = "SELECT c FROM ContratosReservaEjecuion c WHERE c.id = :id"),
    @NamedQuery(name = "ContratosReservaEjecuion.findByContrato", query = "SELECT c FROM ContratosReservaEjecuion c WHERE c.contrato = :contrato")})
public class ContratosReservaEjecuion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "contrato", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Adquisiciones contrato;
    @JoinColumn(name = "reserva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudReservaCompromiso reserva;

    public ContratosReservaEjecuion() {
    }

    public ContratosReservaEjecuion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adquisiciones getContrato() {
        return contrato;
    }

    public void setContrato(Adquisiciones contrato) {
        this.contrato = contrato;
    }

    public SolicitudReservaCompromiso getReserva() {
        return reserva;
    }

    public void setReserva(SolicitudReservaCompromiso reserva) {
        this.reserva = reserva;
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
        if (!(object instanceof ContratosReservaEjecuion)) {
            return false;
        }
        ContratosReservaEjecuion other = (ContratosReservaEjecuion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.certificacion_presupuesto_anual.entity.ContratosReservaEjecuion[ id=" + id + " ]";
    }

}
