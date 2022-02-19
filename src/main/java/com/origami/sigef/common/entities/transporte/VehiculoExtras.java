/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.transporte;

import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.entities.CatalogoItem;
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
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "vehiculo_extras", schema = "transporte")
@NamedQueries({
    @NamedQuery(name = "VehiculoExtras.findAll", query = "SELECT v FROM VehiculoExtras v")})
public class VehiculoExtras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @JoinColumn(name = "caracteristica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem caracteristica;
    @JoinColumn(name = "vehiculo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Vehiculo vehiculo;

    public VehiculoExtras() {
    }

    public VehiculoExtras(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(CatalogoItem caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
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
        if (!(object instanceof VehiculoExtras)) {
            return false;
        }
        VehiculoExtras other = (VehiculoExtras) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.transporte.VehiculoExtras[ id=" + id + " ]";
    }

}
