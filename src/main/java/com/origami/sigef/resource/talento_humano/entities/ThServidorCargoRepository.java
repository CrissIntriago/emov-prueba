/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_servidor_cargo_repository",schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThServidorCargoRepository.findAll", query = "SELECT t FROM ThServidorCargoRepository t"),
    @NamedQuery(name = "ThServidorCargoRepository.findById", query = "SELECT t FROM ThServidorCargoRepository t WHERE t.id = :id"),
    @NamedQuery(name = "ThServidorCargoRepository.findByIdServidor", query = "SELECT t FROM ThServidorCargoRepository t WHERE t.idServidor = :idServidor"),
    @NamedQuery(name = "ThServidorCargoRepository.findByCargo", query = "SELECT t FROM ThServidorCargoRepository t WHERE t.cargo = :cargo"),
    @NamedQuery(name = "ThServidorCargoRepository.findByDetalleCargo", query = "SELECT t FROM ThServidorCargoRepository t WHERE t.detalleCargo = :detalleCargo"),
    @NamedQuery(name = "ThServidorCargoRepository.findByFechaInicio", query = "SELECT t FROM ThServidorCargoRepository t WHERE t.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "ThServidorCargoRepository.findByFechaFin", query = "SELECT t FROM ThServidorCargoRepository t WHERE t.fechaFin = :fechaFin")})
public class ThServidorCargoRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_servidor")
    private BigInteger idServidor;
    @Size(max = 2147483647)
    @Column(name = "cargo")
    private String cargo;
    @Size(max = 2147483647)
    @Column(name = "detalle_cargo")
    private String detalleCargo;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    public ThServidorCargoRepository() {
    }

    public ThServidorCargoRepository(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(BigInteger idServidor) {
        this.idServidor = idServidor;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDetalleCargo() {
        return detalleCargo;
    }

    public void setDetalleCargo(String detalleCargo) {
        this.detalleCargo = detalleCargo;
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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThServidorCargoRepository)) {
            return false;
        }
        ThServidorCargoRepository other = (ThServidorCargoRepository) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThServidorCargoRepository[ id=" + id + " ]";
    }
    
}
