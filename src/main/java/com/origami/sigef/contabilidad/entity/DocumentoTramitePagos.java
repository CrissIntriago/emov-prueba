/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.entity;

import com.origami.sigef.common.entities.ProcedimientoRequisito;
import java.io.Serializable;
import java.math.BigInteger;
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

@Entity
@Table(name = "documento_tramite_pagos")
@XmlRootElement
public class DocumentoTramitePagos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @JoinColumn(name = "proceso_tramite", referencedColumnName = "id")
    @ManyToOne
    private ProcedimientoRequisito procesoTramite;

    public DocumentoTramitePagos() {
    }

    public DocumentoTramitePagos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public ProcedimientoRequisito getProcesoTramite() {
        return procesoTramite;
    }

    public void setProcesoTramite(ProcedimientoRequisito procesoTramite) {
        this.procesoTramite = procesoTramite;
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
        if (!(object instanceof DocumentoTramitePagos)) {
            return false;
        }
        DocumentoTramitePagos other = (DocumentoTramitePagos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entidades.DocumentoTramitePagos[ id=" + id + " ]";
    }
    
}