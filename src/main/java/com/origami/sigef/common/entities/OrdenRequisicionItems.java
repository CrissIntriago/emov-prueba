/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @author OrigamiEc
 */
@Entity
@Table(name = "orden_requisicion_items", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenRequisicionItems.findAll", query = "SELECT o FROM OrdenRequisicionItems o"),
    @NamedQuery(name = "OrdenRequisicionItems.findById", query = "SELECT o FROM OrdenRequisicionItems o WHERE o.id = :id"),
    @NamedQuery(name = "OrdenRequisicionItems.findByIdCodigo", query = "SELECT o FROM OrdenRequisicionItems o JOIN o.item i WHERE o.ordenRequisicion = ?1"),
    @NamedQuery(name = "OrdenRequisicionItems.findByItem", query = "SELECT o FROM OrdenRequisicionItems o WHERE o.item = :item"),
    @NamedQuery(name = "OrdenRequisicionItems.findByCantidadSolicitada", query = "SELECT o FROM OrdenRequisicionItems o WHERE o.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "OrdenRequisicionItems.findByCantidadDespachada", query = "SELECT o FROM OrdenRequisicionItems o WHERE o.cantidadDespachada = :cantidadDespachada")})
public class OrdenRequisicionItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DetalleItem item;
    @Column(name = "cantidad_solicitada")
    private Integer cantidadSolicitada;
    @Column(name = "cantidad_despachada")
    private Integer cantidadDespachada;
    @JoinColumn(name = "orden_requisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    
    private OrdenRequisicion ordenRequisicion;
    @Column(name = "revisado")
    private Boolean revisado;

    public OrdenRequisicionItems() {
    }

    public OrdenRequisicionItems(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DetalleItem getItem() {
        return item;
    }

    public void setItem(DetalleItem item) {
        this.item = item;
    }

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Integer getCantidadDespachada() {
        return cantidadDespachada;
    }

    public void setCantidadDespachada(Integer cantidadDespachada) {
        this.cantidadDespachada = cantidadDespachada;
    }

    public OrdenRequisicion getOrdenRequisicion() {
        return ordenRequisicion;
    }

    public void setOrdenRequisicion(OrdenRequisicion ordenRequisicion) {
        this.ordenRequisicion = ordenRequisicion;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
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
        if (!(object instanceof OrdenRequisicionItems)) {
            return false;
        }
        OrdenRequisicionItems other = (OrdenRequisicionItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.OrdenRequisicionItems[ id=" + id + " ]";
    }

}
