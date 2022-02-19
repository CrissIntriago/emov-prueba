/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "bien_componentes", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BienComponentes.findAll", query = "SELECT b FROM BienComponentes b"),
    @NamedQuery(name = "BienComponentes.findById", query = "SELECT b FROM BienComponentes b WHERE b.id = :id"),
    @NamedQuery(name = "BienComponentes.findByDescripcion", query = "SELECT b FROM BienComponentes b WHERE b.descripcion = :descripcion"),
    @NamedQuery(name = "BienComponentes.findBySerie", query = "SELECT b FROM BienComponentes b WHERE b.serie = :serie"),
    @NamedQuery(name = "BienComponentes.findByCantidad", query = "SELECT b FROM BienComponentes b WHERE b.cantidad = :cantidad")})
public class BienComponentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 255)
    @Column(name = "serie")
    private String serie;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @JoinColumn(name = "bien_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem bienItem;
    @JoinColumn(name = "componente_bien_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private BienesItem componenteBienItem;

    public BienComponentes() {
    }

    public BienComponentes(Long id) {
        this.id = id;
    }

    public BienComponentes(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BienesItem getBienItem() {
        return bienItem;
    }

    public void setBienItem(BienesItem bienItem) {
        this.bienItem = bienItem;
    }

    public BienesItem getComponenteBienItem() {
        return componenteBienItem;
    }

    public void setComponenteBienItem(BienesItem componenteBienItem) {
        this.componenteBienItem = componenteBienItem;
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
        if (!(object instanceof BienComponentes)) {
            return false;
        }
        BienComponentes other = (BienComponentes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.BienComponentes[ id=" + id + " ]";
    }

}
