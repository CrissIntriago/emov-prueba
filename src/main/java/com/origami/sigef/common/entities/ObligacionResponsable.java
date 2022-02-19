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
import javax.validation.constraints.Size;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "obligacion_responsable", schema = "administracion")
@NamedQueries({
    @NamedQuery(name = "ObligacionResponsable.findAll", query = "SELECT o FROM ObligacionResponsable o"),
    @NamedQuery(name = "ObligacionResponsable.findById", query = "SELECT o FROM ObligacionResponsable o WHERE o.id = :id"),
    @NamedQuery(name = "ObligacionResponsable.findByDescripcion", query = "SELECT o FROM ObligacionResponsable o WHERE o.descripcion = :descripcion")})
public class ObligacionResponsable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private Short numero;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "orden_compra", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudOrdenCompra ordenCompra;

    public ObligacionResponsable() {
    }

    public ObligacionResponsable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getNumero() {
        return numero;
    }

    public void setNumero(Short numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public SolicitudOrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(SolicitudOrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
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
        if (!(object instanceof ObligacionResponsable)) {
            return false;
        }
        ObligacionResponsable other = (ObligacionResponsable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ObligacionResponsable[ id=" + id + " ]";
    }

}
