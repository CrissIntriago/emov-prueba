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
@Table(name = "caracteristica_tecnica", schema = "administracion")
@NamedQueries({
    @NamedQuery(name = "CaracteristicaTecnica.findAll", query = "SELECT c FROM CaracteristicaTecnica c")
    , @NamedQuery(name = "CaracteristicaTecnica.findById", query = "SELECT c FROM CaracteristicaTecnica c WHERE c.id = :id")
    , @NamedQuery(name = "CaracteristicaTecnica.findByCaracteristica", query = "SELECT c FROM CaracteristicaTecnica c WHERE c.caracteristica = :caracteristica")
    , @NamedQuery(name = "CaracteristicaTecnica.findByDescripcion", query = "SELECT c FROM CaracteristicaTecnica c WHERE c.descripcion = :descripcion")})
public class CaracteristicaTecnica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "caracteristica")
    private String caracteristica;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    
    @JoinColumn(name = "orden_compra", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private SolicitudOrdenCompra ordenCompra;

    public CaracteristicaTecnica() {
    }

    public CaracteristicaTecnica(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
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
        if (!(object instanceof CaracteristicaTecnica)) {
            return false;
        }
        CaracteristicaTecnica other = (CaracteristicaTecnica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.CaracteristicaTecnica[ id=" + id + " ]";
    }
    
}
